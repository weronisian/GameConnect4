import java.util.Scanner;

public class Game {
    private IEvaluationFunction function;
    private Board board;
    private char lastPlayer;
    private int nextMoveLocation;
    private int MODE;
    private int depth;
    private Scanner scanner;
    int turn = 0;   //turn 1 -> o; turn 2 -> x

    public Game(int mode){
        this.board = new Board();
        this.lastPlayer = 'x';
        this.MODE = mode;
        scanner = new Scanner(System.in);
        start();
    }

    public Game(int mode, int depth, IEvaluationFunction function){
        this.function = function;
        this.board = new Board();
        this.lastPlayer = 'x';
        this.nextMoveLocation = 3;
        this.MODE = mode;
        this.depth = depth;
        scanner = new Scanner(System.in);
        start();
    }

    public void start(){
        switch (MODE) {
            case 0:
                board.displayBoard();
                while (board.isEmptyField()) {
                    lastPlayer = getOtherPlayer(lastPlayer);
                    humanMove();
                    board.displayBoard();
                    if (board.checkIfWin(lastPlayer, board)) {
                        displayScores();
                        break;
                    }
                }
                if(!board.isEmptyField())
                    System.out.println("Draw!");
                break;

            case 1:
                board.displayBoard();
                while(board.isEmptyField()){
                    turn = 1;
                    lastPlayer = getOtherPlayer(lastPlayer);
                    humanMove();
                    board.displayBoard();
                    if(board.checkIfWin(lastPlayer, board)) {
                        System.out.println("You Win!");
                        break;
                    }

                    turn = 2;
                    lastPlayer = getOtherPlayer(lastPlayer);
                    AIMove();
                    board.displayBoard();
                    if(board.checkIfWin(lastPlayer, board)) {
                        System.out.println("AI Wins!");
                        break;
                    }
                }
                if(!board.isEmptyField())
                    System.out.println("Draw!");
                break;
        }
    }

    public void humanMove(){
        System.out.println("Your move, select the column ");
        int move = scanner.nextInt();
        while(move<0 || move > 6){
            System.out.println("Invalid move.\n\nYour move - columns 0 to 6: ");
            move = scanner.nextInt();
        }
        while(!makeMove(move, lastPlayer, board)) {
            System.out.println("Select other column");
            move = scanner.nextInt();
        }
    }

    public void AIMove(){
        char player = lastPlayer;
        Board board = new Board(this.board);

//        int bestEvaluation = Integer.MIN_VALUE;
//        int bestCol = 0;
//        for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
//            makeMove(i, player,board);
            int res = minimax(0, true, player, board);
//            if(bestEvaluation < res) {
//                bestEvaluation = res;
//                bestCol = i;
//            }
//            System.out.println("Score for column " + i + " = " + res);
//        }

        System.out.println("Next move: " + nextMoveLocation);
        makeMove(nextMoveLocation, lastPlayer, this.board);
    }

    private boolean makeMove(int col, char lastPlayer, Board board){
        int field = board.firstEmptyInCol(col);
        if(field != -1) {
            board.fields[field][col] = lastPlayer;
        } else {
            return false;
        }
        return true;
    }

    public int minimax(int level, boolean isMax, char player, Board board) {
//        board.displayBoard();
        if(level == depth || !board.isEmptyField()){
//            if(checkIfWin('x', board))
//                return 10;
//            if (checkIfWin('o', board))
//                return -10;
//            return 0;
            return function.evalueStatus(board, player, turn);
        }

//        for(int j = 0; j < Board.BOARD_COLUMNS; j++) {
            if (isMax) {
//                System.out.println("TERAZ MAX");
                int maxScore = Integer.MIN_VALUE;
                int value = 0;

                for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
                    Board temp = new Board(board);
                    if (makeMove(i, player, temp)) {
                        value = minimax(level + 1, false, getOtherPlayer(player), temp);
//                        System.out.println("Score for location "+i+" = "+value);
                        if(value > maxScore)
                            nextMoveLocation = i;
                    }
                    if(level == 0){
                        System.out.println("Score for location "+i+" = "+value);
                    }
                    maxScore = Math.max(value, maxScore);
                }

//                System.out.println("Max value: " + maxScore);
                return maxScore;
            } else {
//                System.out.println("TERAZ MIN");
                int minScore = Integer.MAX_VALUE;
                int value = 0;

                for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
                    Board temp = new Board(board);
                    if (makeMove(i, player, temp)) {
                        value = minimax(level + 1, true, getOtherPlayer(player), temp);
                    }
                    minScore = Math.min(value, minScore);
                }

//                System.out.println("Min value: " + minScore);
                return minScore;
            }
//        }
//        return -1;
    }



//    public int minimax(int level, boolean isMax, char player, Board board) {
//        board.displayBoard();
//        if(level == depth || !board.isEmptyField()){
////            if(checkIfWin('x', board))
////                return 10;
////            if (checkIfWin('o', board))
////                return -10;
////            return 0;
//            return function.evalueStatus(board);
//        }
//
//        if(isMax){
//            System.out.println("TERAZ MAX");
//            int value = Integer.MIN_VALUE;
//
//            for(int i=0; i<Board.BOARD_COLUMNS; i++){
//                Board temp = new Board(board);
//                if(makeMove(i, player, temp)) {
//                    value = Math.max(value, minimax(level + 1, false, getOtherPlayer(player), temp));
//                }
//            }
//            System.out.println("Max value: "+ value);
//            return value;
//        }
//        else{
//            System.out.println("TERAZ MIN");
//            int value = Integer.MAX_VALUE;
//            for(int i=0; i<Board.BOARD_COLUMNS; i++){
//                Board temp = new Board(board);
//                if(makeMove(i, player, temp)) {
//                    value = Math.min(value, minimax(level + 1, true, getOtherPlayer(player), temp));
//                }
//            }
//            System.out.println("Min value: "+ value);
//            return value;
//        }
//
//    }


//    private boolean checkIfWin(char lastPlayer, Board board){
////        System.out.println("Lastfield: "+ lastField.x + " " + lastField.y);
////        // vertical
////        int minRow = Math.max(0, lastField.y - 3);
////        int maxRow = Math.min(5, lastField.y + 3);
////
////        for(int i=minRow; i<maxRow-3; i++){
////            if(board.fields[i][lastField.x] == lastPlayer && board.fields[i+1][lastField.x] == lastPlayer
////                    && board.fields[i+2][lastField.x] == lastPlayer && board.fields[i+3][lastField.x] == lastPlayer)
////                return true;
////        }
////
////        // horizontal
////        int minCol = Math.max(0, lastField.x - 3);
////        int maxCol = Math.min(6, lastField.x + 3);
////
////        for(int i=minCol; i<=maxCol-3; i++){
////            if(board.fields[lastField.y][i] == lastPlayer && board.fields[lastField.y][i+1] == lastPlayer
////                    && board.fields[lastField.y][i+2] == lastPlayer && board.fields[lastField.y][i+3] == lastPlayer)
////                return true;
////        }
//
////        int j=minCol;
////        int k = Math.min(6, lastField.x + lastField.y);
////        for(int i=minRow; i<maxRow-3; i++){
////            if(board.fields[i][j] == lastPlayer && board.fields[i+1][j+1] == lastPlayer
////                    && board.fields[i+2][j+2] == lastPlayer && board.fields[i+3][j+3] == lastPlayer)
////                return true;
////            j++;
////        }
////
////        for(int i=minRow; i<=maxRow-3; i++){
////            if( k >= 3) {
////                if (board.fields[i][k] == lastPlayer && board.fields[i + 1][k - 1] == lastPlayer
////                        && board.fields[i + 2][k - 2] == lastPlayer && board.fields[i + 3][k - 3] == lastPlayer) {
////                    return true;
////                }
////            }
////            k--;
////        }
//
//        // horizontalCheck
//        for (int i = 0; i<Board.BOARD_ROWS; i++){
//            for (int j = 0; j<Board.BOARD_COLUMNS-3 ; j++ ){
//                if (board.fields[i][j] == lastPlayer && board.fields[i][j+1] == lastPlayer
//                        && board.fields[i][j+2] == lastPlayer && board.fields[i][j+3] == lastPlayer){
//                    return true;
//                }
//            }
//        }
//        // verticalCheck
//        for (int i = 0; i<Board.BOARD_ROWS-3 ; i++ ){
//            for (int j = 0; j<Board.BOARD_COLUMNS; j++){
//                if (board.fields[i][j] == lastPlayer && board.fields[i+1][j] == lastPlayer
//                        && board.fields[i+2][j] == lastPlayer && board.fields[i+3][j] == lastPlayer){
//                    return true;
//                }
//            }
//        }
//
//        // diagonally
//        for (int i=3; i<Board.BOARD_ROWS; i++){
//            for (int j=0; j<Board.BOARD_COLUMNS-3; j++){
//                if (board.fields[i][j] == lastPlayer && board.fields[i-1][j+1] == lastPlayer
//                        && board.fields[i-2][j+2] == lastPlayer && board.fields[i-3][j+3] == lastPlayer)
//                    return true;
//            }
//        }
//        for (int i=3; i< Board.BOARD_ROWS; i++){
//            for (int j=3; j<Board.BOARD_COLUMNS; j++){
//                if (board.fields[i][j] == lastPlayer && board.fields[i-1][j-1] == lastPlayer
//                        && board.fields[i-2][j-2] == lastPlayer && board.fields[i-3][j-3] == lastPlayer)
//                    return true;
//            }
//        }
//        return false;
//    }

    private char setPlayer(char lastPlayer){
        if(lastPlayer == 'x')
            lastPlayer = 'o';
        else
            lastPlayer = 'x';
        return lastPlayer;
    }

    private char getOtherPlayer(char player){
        if(player == 'x')
            return 'o';
        else
            return 'x';
    }

    public void displayScores(){
        System.out.println("player " + lastPlayer + " wins");

    }

}
