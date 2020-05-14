import java.util.Random;
import java.util.Scanner;

public class Game {
    private IEvaluationFunction function;
    private Board board;
    private char lastPlayer;
    private boolean firstMove = true;
    private int nextMoveLocation = 3;
    private boolean alfabeta = false;
    private int MODE;
    private int depth;
    private Scanner scanner;
    int turn = 0;   //turn 1 -> o; turn 2 -> x
    int minmaxCount = 0;

    public Game(int mode){
        this.board = new Board();
        this.lastPlayer = 'x';
        this.MODE = mode;
        scanner = new Scanner(System.in);
        start();
    }

    public Game(int mode, int depth, IEvaluationFunction function, boolean alfabeta){
        this.function = function;
        this.board = new Board();
        this.lastPlayer = 'x';
        this.MODE = mode;
        this.depth = depth;
        this.alfabeta = alfabeta;
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

            case 2:
                board.displayBoard();
                while(board.isEmptyField()){
                    if(turn == 1)
                        turn = 2;
                    else
                        turn = 1;
                    lastPlayer = getOtherPlayer(lastPlayer);
                    AIMove();
                    board.displayBoard();
                    if(board.checkIfWin(lastPlayer, board)) {
                        if(turn == 1)
                            System.out.println("AI 1 Wins!");
                        else
                            System.out.println("AI 2 Wins!");
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

        if(firstMove) {
            firstMove = false;
            Random random = new Random();
            nextMoveLocation = random.nextInt(7);
        }
        else {
            long start = System.currentTimeMillis();
            minimax(0, true, player, board, Integer.MIN_VALUE, Integer.MAX_VALUE);
            long stop = System.currentTimeMillis();
            long time = stop - start;
            System.out.println("Time: " + time);
            System.out.println("Count: " + minmaxCount);
            minmaxCount = 0;

        }
        System.out.println("Next AI move: " + nextMoveLocation);
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

    public int minimax(int level, boolean isMax, char player, Board board, int alfa, int beta) {
//        board.displayBoard();
        minmaxCount++;

        if(level == depth || !board.isEmptyField() || board.checkIfWin(player, board)
                || board.checkIfWin(getOtherPlayer(player), board)){
            return function.evalueStatus(board, player, turn);
        }

        if (isMax) {
//                System.out.println("TERAZ MAX");
            int maxScore = Integer.MIN_VALUE;
            int value = 0;

            for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
                Board temp = new Board(board);
                if (makeMove(i, player, temp)) {
                    value = minimax(level + 1, false, getOtherPlayer(player), temp, alfa, beta);
                }
                if(level == 0 && board.firstEmptyInCol(i) != -1){
                    if(value > maxScore) {
                        nextMoveLocation = i;
                    }
                    else if(value == maxScore) {
                        Random random = new Random();
                        boolean rand = random.nextBoolean();
                        if(rand)
                            nextMoveLocation = i;
                    }
//                    System.out.println("Score for location "+i+" = "+value);
                }
                maxScore = Math.max(value, maxScore);
                if(alfabeta) {
                    alfa = Math.max(value, alfa);
                    if (beta <= alfa) {
                        break;
                    }
                }
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
                    value = minimax(level + 1, true, getOtherPlayer(player), temp, alfa, beta);
                }
                minScore = Math.min(value, minScore);
                if(alfabeta) {
                    beta = Math.min(value, beta);
                    if (beta <= alfa) {
                        break;
                    }
                }
            }
//                System.out.println("Min value: " + minScore);
            return minScore;
        }
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
