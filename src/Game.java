import java.awt.*;
import java.util.Random;
import java.util.Scanner;

public class Game {
    IEvaluationFunction function;
    private Board board;
    private Point lastField;
    private char lastPlayer;
    private int MODE;
    private int depth;
    Scanner scanner;

    public Game(int mode, int depth, IEvaluationFunction function){
        this.function = function;
        this.board = new Board();
        this.lastField = new Point(0, 0);
        this.lastPlayer = 'x';
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
                    lastPlayer = setPlayer(lastPlayer);
                    humanMove();
                    board.displayBoard();
                    if (checkIfWin(lastPlayer, board, lastField)) {
                        displayScores();
                        break;
                    }
                }
                break;

            case 1:
                board.displayBoard();
                while(board.isEmptyField()){
                    lastPlayer = setPlayer(lastPlayer);
                    humanMove();
                    board.displayBoard();
                    if(checkIfWin(lastPlayer, board, lastField)) {
                        System.out.println("You Win!");
                        break;
                    }

                    lastPlayer = setPlayer(lastPlayer);
                    AIMove();
                    board.displayBoard();
                    if(checkIfWin(lastPlayer, board, lastField)) {
                        System.out.println("AI Wins!");
                        break;
                    }

//                    {System.out.println("Draw!");break;}

                }
        }
    }

    public void humanMove(){
        System.out.println("Your move, select the column ");
        int move = scanner.nextInt();
        while(move<0 || move > 6){
            System.out.println("Invalid move.\n\nYour move - columns 0 to 6: ");
            move = scanner.nextInt();
        }
        while(!makeMove(move, lastPlayer, board, lastField)) {
            System.out.println("Select other column");
            move = scanner.nextInt();
        }
    }

    public void AIMove(){
        char player = lastPlayer;
        Point field = lastField;
        Board board = new Board(this.board);

        int bestEvaluation = Integer.MIN_VALUE;
        int bestCol = 0;
        for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
            int res = minimax(0, i, true, player, board, field);
            if(bestEvaluation < res) {
                bestEvaluation = res;
                bestCol = i;
            }
            System.out.println("Score for column " + i + " = " + res);
        }
//        Random random = new Random();
//        int rand = random.nextInt(7);
        makeMove(bestCol, lastPlayer, this.board, lastField);
    }

    private boolean makeMove(int col, char lastPlayer, Board board, Point lastField){
        int field = board.firstEmptyInCol(col);
        if(field != -1) {
            board.fields[field][col] = lastPlayer;
            lastField.x = col;
            lastField.y = field;
        } else {
            return false;
        }
        return true;
    }

        public int minimax(int level, int col, boolean isMax, char player, Board board, Point lastField) {
//            board.displayBoard();
//            if (level == depth) {
//                if(checkIfWin(lastPlayer, board, lastField))
//                    return 1;
//                char tmp = lastPlayer;
//                if(checkIfWin(setPlayer(tmp), board, lastField))
//                    return -1;
            if (level == depth || !board.isEmptyField()) {
                return function.evalueStatus(board);
            }

            // If current move is maximizer, find the maximum attainable
            // value
            if (isMax) {
                int max = Integer.MIN_VALUE;
                for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
//                    Board temp = new Board(board);
                    makeMove(i, player, board, lastField);
                    max = Math.max(max, minimax(level + 1, i, false, setPlayer(player), board, lastField));
//                    if (res > max)
//                        max = res;
                }
                return max;
            }
                // Else (If current move is Minimizer), find the minimum
                // attainable value
            else {
                int min = Integer.MAX_VALUE;
                for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
                    Board temp = new Board(board);
                    makeMove(i, player, temp, lastField);
                    min = Math.min(min,  minimax(level + 1, i, true, setPlayer(player), temp, lastField));
//                    if (res < min)
//                        min = res;
                }
                return min;
            }
        }

    private boolean checkIfWin(char lastPlayer, Board board, Point lastField){
//        System.out.println("Lastfield: "+ lastField.x + " " + lastField.y);
//        // vertical
//        int minRow = Math.max(0, lastField.y - 3);
//        int maxRow = Math.min(5, lastField.y + 3);
//
//        for(int i=minRow; i<maxRow-3; i++){
//            if(board.fields[i][lastField.x] == lastPlayer && board.fields[i+1][lastField.x] == lastPlayer
//                    && board.fields[i+2][lastField.x] == lastPlayer && board.fields[i+3][lastField.x] == lastPlayer)
//                return true;
//        }
//
//        // horizontal
//        int minCol = Math.max(0, lastField.x - 3);
//        int maxCol = Math.min(6, lastField.x + 3);
//
//        for(int i=minCol; i<=maxCol-3; i++){
//            if(board.fields[lastField.y][i] == lastPlayer && board.fields[lastField.y][i+1] == lastPlayer
//                    && board.fields[lastField.y][i+2] == lastPlayer && board.fields[lastField.y][i+3] == lastPlayer)
//                return true;
//        }

//        int j=minCol;
//        int k = Math.min(6, lastField.x + lastField.y);
//        for(int i=minRow; i<maxRow-3; i++){
//            if(board.fields[i][j] == lastPlayer && board.fields[i+1][j+1] == lastPlayer
//                    && board.fields[i+2][j+2] == lastPlayer && board.fields[i+3][j+3] == lastPlayer)
//                return true;
//            j++;
//        }
//
//        for(int i=minRow; i<=maxRow-3; i++){
//            if( k >= 3) {
//                if (board.fields[i][k] == lastPlayer && board.fields[i + 1][k - 1] == lastPlayer
//                        && board.fields[i + 2][k - 2] == lastPlayer && board.fields[i + 3][k - 3] == lastPlayer) {
//                    return true;
//                }
//            }
//            k--;
//        }

        // horizontalCheck
        for (int i = 0; i<Board.BOARD_ROWS; i++){
            for (int j = 0; j<Board.BOARD_COLUMNS-3 ; j++ ){
                if (board.fields[i][j] == lastPlayer && board.fields[i][j+1] == lastPlayer
                        && board.fields[i][j+2] == lastPlayer && board.fields[i][j+3] == lastPlayer){
                    return true;
                }
            }
        }
        // verticalCheck
        for (int i = 0; i<Board.BOARD_ROWS-3 ; i++ ){
            for (int j = 0; j<Board.BOARD_COLUMNS; j++){
                if (board.fields[i][j] == lastPlayer && board.fields[i+1][j] == lastPlayer
                        && board.fields[i+2][j] == lastPlayer && board.fields[i+3][j] == lastPlayer){
                    return true;
                }
            }
        }

        // diagonally
        for (int i=3; i<Board.BOARD_ROWS; i++){
            for (int j=0; j<Board.BOARD_COLUMNS-3; j++){
                if (board.fields[i][j] == lastPlayer && board.fields[i-1][j+1] == lastPlayer
                        && board.fields[i-2][j+2] == lastPlayer && board.fields[i-3][j+3] == lastPlayer)
                    return true;
            }
        }
        for (int i=3; i< Board.BOARD_ROWS; i++){
            for (int j=3; j<Board.BOARD_COLUMNS; j++){
                if (board.fields[i][j] == lastPlayer && board.fields[i-1][j-1] == lastPlayer
                        && board.fields[i-2][j-2] == lastPlayer && board.fields[i-3][j-3] == lastPlayer)
                    return true;
            }
        }
        return false;
    }

    private char setPlayer(char lastPlayer){
        if(lastPlayer == 'x')
            lastPlayer = 'o';
        else
            lastPlayer = 'x';
        return lastPlayer;
    }

    public void displayScores(){
        System.out.println("player " + lastPlayer + " wins");

    }

}
