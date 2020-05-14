import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GameController {

    private static final int TILE_SIZE = 100;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    private IEvaluationFunction function1 = new EvaluationFunctionWinLose();
    private IEvaluationFunction function2 = new EvaluationFunctionWinLose();
    private char lastPlayer = 'x';
    private boolean firstMove = true;
    private int nextMoveLocation = 3;
    private int moves1 = 0, moves2 = 0;
    private long moveTime1 = 0, moveTime2 = 0;
    private long time1 = 0, time2 = 0;
    private int depth1 = 5, depth2 = 4;
    private boolean alfabeta1 = false, alfabeta2 = false;
    private int turn = 0;
    private int mode = 0;
    private boolean isRestart = false;
    private Board board;

    public int n = 0;
    private boolean testDepth = false;


    public GameController(){
    }

    @FXML
    void initialize(){
        board = new Board();
        board_pane.getChildren().add(makeGrid());
        if(mode == 0 || mode == 1)
            board_pane.getChildren().addAll(makeColumns());
        board_pane.setDisable(true);
//        start_button.setDisable(true);
        radio3_button.setSelected(true);
        scores_red_text.setText(String.valueOf(GUI.redScores));
        scores_yellow_text.setText(String.valueOf(GUI.yellowScores));

        algorithm1_combobox.getItems().add("minimax");
        algorithm1_combobox.getItems().add("alfa-beta");

        algorithm2_combobox.getItems().add("minimax");
        algorithm2_combobox.getItems().add("alfa-beta");

        for(int i=1; i <= 13; i++) {
            depth1_combobox.getItems().add(i);
            depth2_combobox.getItems().add(i);
        }
        function1_combobox.getItems().add("Function win-lose");
        function1_combobox.getItems().add("Function 3 next to each other");
        function1_combobox.getItems().add("Function empty spaces in column");

        function2_combobox.getItems().add("Function win-lose");
        function2_combobox.getItems().add("Function 3 next to each other");
        function2_combobox.getItems().add("Function empty spaces in column");

    }

    public void start(){
        switch (mode) {
            case 0:
                break;
            case 2:
                turn = (turn == 1) ? 2 : 1;
                lastPlayer = getOtherPlayer(lastPlayer);
                makeAImove();
                board.displayBoard();
                break;
        }
    }

    private void nextTurn(int column) {
        turn = (turn == 1) ? 2 : 1;

        error_text.setVisible(false);

        if(placeCircle(column)) {
            lastPlayer = getOtherPlayer(lastPlayer);
            makeMove(column, lastPlayer, board);
            board.displayBoard();
        }
        else
            turn = (turn == 1) ? 2 : 1;

        if(!board.isEmptyField()) {
            win_text.setText("Draw!");
            win_text.setVisible(true);
            board_pane.setDisable(true);
            start_button.setDisable(true);
        }

    }

    private boolean placeCircle(int column) {
        int row = board.firstEmptyInCol(column);
        if(row != -1) {

            Circle circle;
            if (turn == 1)
                circle = new Circle(TILE_SIZE / 2, Color.RED);
            else
                circle = new Circle(TILE_SIZE / 2, Color.YELLOW);
            circle.setCenterX(TILE_SIZE / 2);
            circle.setCenterY(TILE_SIZE / 2);

            disc_pane.getChildren().add(circle);
            circle.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

            board_pane.setDisable(true);

            TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), circle);
            animation.setToY((ROWS - row - 1) * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation.setOnFinished(e -> {
                if (board.checkIfWin(lastPlayer, board)) {
                    gameOver();
                }
                else if(!board.isEmptyField()){
                    win_text.setText("Draw!");
                    win_text.setVisible(true);
                    board_pane.setDisable(false);
                    tests();
                    new Results().writeAvgTime(depth1, time1, moves1);
                }
                else {
                    if (mode == 1 && turn == 1) {
                        turn = 2;
                        lastPlayer = getOtherPlayer(lastPlayer);
                        makeAImove();
                        board.displayBoard();
                    }

                    if (mode == 2 && !isRestart) {
                        turn = (turn == 1) ? 2 : 1;
                        lastPlayer = getOtherPlayer(lastPlayer);
                        makeAImove();
                        board.displayBoard();
                    }
                    board_pane.setDisable(false);
                }
            });

            animation.play();
            return true;
        } else {
            error_text.setText("Select other column");
            error_text.setVisible(true);
            return false;
        }
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

    private void makeAImove() {
        if (turn == 1) {
            moves1++;
        } else {
            moves2++;
        }
        long startTime = System.currentTimeMillis();

        char player = lastPlayer;
        Board board = new Board(this.board);

        Random random = new Random();
        if(firstMove) {
            firstMove = false;
//            random.nextInt(7);
//            random.nextInt(7);
            nextMoveLocation = random.nextInt(7);
        }
        else {
            if (turn == 1) {
                System.out.println("Depth1:" + depth1);
                System.out.println("Algorithm1:" + alfabeta1);
                System.out.println("Function1:" + function1);
                minimax(0, true, player, board, depth1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            else {
                System.out.println("Depth2:" + depth2);
                System.out.println("Algorithm2:" + alfabeta2);
                System.out.println("Function2:" + function2);
                minimax(0, true, player, board, depth2, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
        }

        System.out.println("Next AI move: " + nextMoveLocation);
        while(!placeCircle(nextMoveLocation))
            nextMoveLocation = random.nextInt(7);
        makeMove(nextMoveLocation, lastPlayer, this.board);

        long stopTime = System.currentTimeMillis();
        if(turn == 1) {
            moveTime1 = (stopTime - startTime);
            time1 += (stopTime - startTime);
        }
        else {
            moveTime2 = (stopTime - startTime);
            new Results().writeMoveAndTime(moveTime1, moveTime2, moves1, moves2);
            moveTime1 = 0;
            moveTime2 = 0;
            time2 += (stopTime - startTime);
        }

    }

    public int minimax(int level, boolean isMax, char player, Board board, int depth, int alpha, int beta) {
        if(level == depth || !board.isEmptyField() || board.checkIfWin(player, board)
                || board.checkIfWin(getOtherPlayer(player), board)){
            if(turn == 1)
                return function1.evalueStatus(board, player, turn);
            else
                return function2.evalueStatus(board, player, turn);
        }

        if (isMax) {
            int maxScore = Integer.MIN_VALUE;
            int value = 0;

            for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
                Board temp = new Board(board);
                if (makeMove(i, player, temp)) {
                    value = minimax(level + 1, false, getOtherPlayer(player), temp, depth, alpha, beta);
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

                if((turn == 1 && alfabeta1) || (turn == 2 && alfabeta2)) {
                    alpha = Math.max(maxScore, alpha);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            int value = 0;

            for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
                Board temp = new Board(board);
                if (makeMove(i, player, temp)) {
                    value = minimax(level + 1, true, getOtherPlayer(player), temp, depth, alpha, beta);
                    minScore = Math.min(value, minScore);
                    if((turn == 1 && alfabeta1) || (turn == 2 && alfabeta2)) {
                        beta = Math.min(minScore, beta);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }

            }
            return minScore;
        }
    }

    private void gameOver() {
        if(mode == 2)
            new Results().writeAvgTime(depth1, time1, moves1);

        if(turn == 1) {
            GUI.redScores++;
            scores_red_text.setText(String.valueOf(GUI.redScores));
            win_text.setText("Red Wins!");
            new Results().writeDepthAndMovesToWin(alfabeta1, alfabeta2, function1.toString(), function2.toString(),
                    depth1, depth2, time1, moves1, 1);
        } else {
            GUI.yellowScores++;
            scores_yellow_text.setText(String.valueOf(GUI.yellowScores));
            win_text.setText("Yellow Wins!");
            new Results().writeDepthAndMovesToWin(alfabeta1, alfabeta2, function1.toString(), function2.toString(),
                    depth1, depth2, time2, moves2, 2);
        }

        win_text.setVisible(true);
        board_pane.setDisable(true);
        start_button.setDisable(true);

        System.out.println("Winner: " + (turn == 1 ? "RED" : "YELLOW"));

        tests();
    }

    public void tests(){
        if(n > 0) {
//            if(testDepth && depth1 > 0) {
//            setDepth1(depth1--);
////            testDepth--;
//            }
            restart_button.fire();
            start_button.fire();
        }
    }

    public void setTests(boolean testDepth, int repeat){
        this.testDepth = testDepth;
        this.n = repeat;
    }

    private List<Rectangle> makeColumns(){
        List<Rectangle> list = new ArrayList<>();

        for(int x=0; x < COLUMNS; x++){
            Rectangle rect = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);
            rect.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rect.setFill(Color.TRANSPARENT);

            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));

            final int column = x;
            rect.setOnMouseClicked(e -> {
                nextTurn(column);
            });
            list.add(rect);
        }
        return list;
    }

    private Shape makeGrid() {
        Shape shape = new Rectangle((COLUMNS + 1)* TILE_SIZE, (ROWS + 1)* TILE_SIZE);

        for(int y=0; y<ROWS; y++){
            for(int x=0; x<COLUMNS; x++){
                Circle circle = new Circle(TILE_SIZE/2);
                circle.setCenterX(TILE_SIZE/2);
                circle.setCenterY(TILE_SIZE/2);
                circle.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
                circle.setTranslateY(y * (TILE_SIZE + 5) + TILE_SIZE / 4);

                shape = Shape.subtract(shape, circle);
            }
        }
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        shape.setFill(Color.BLUE);
        shape.setEffect(lighting);
        return shape;
    }

    private char getOtherPlayer(char player){
        if(player == 'x')
            return 'o';
        else
            return 'x';
    }

    @FXML   //nie potrzeba inicjalizacji elementów
    private Button start_button;

    @FXML
    private Button restart_button;

    @FXML
    private Text scores_red_text;

    @FXML
    private Text scores_yellow_text;

    @FXML
    private Text error_text;

    @FXML
    private Text win_text;

    @FXML
    private Pane board_pane;

    @FXML
    private Pane disc_pane;

    @FXML
    private RadioButton radio1_button;

    @FXML
    private RadioButton radio2_button;

    @FXML
    private RadioButton radio3_button;

    @FXML
    private ComboBox algorithm1_combobox;

    @FXML
    private ComboBox algorithm2_combobox;

    @FXML
    private ComboBox depth1_combobox;

    @FXML
    private ComboBox function1_combobox;

    @FXML
    private ComboBox depth2_combobox;

    @FXML
    private ComboBox function2_combobox;

    //gettery
    public Text getWin_text() { return win_text; }

    public Text getError_text() { return error_text; }

    public Button getStart_button(){ return start_button; }

    public Button getRestart_button(){ return restart_button; }

    public RadioButton getRadio1_button(){ return radio1_button; }

    public RadioButton getRadio2_button(){ return radio2_button; }

    public RadioButton getRadio3_button(){ return radio3_button; }

    public ComboBox getAlgorithm1_combobox(){ return algorithm1_combobox; }

    public ComboBox getAlgorithm2_combobox(){ return algorithm2_combobox; }

    public ComboBox getDepth1_combobox(){ return depth1_combobox; }

    public ComboBox getFunction1_combobox(){ return function1_combobox; }

    public ComboBox getDepth2_combobox(){ return depth2_combobox; }

    public ComboBox getFunction2_combobox(){ return function2_combobox; }

    public Pane getBoard_pane(){ return board_pane; }

    public Pane getDisc_pane(){ return disc_pane; }

    public int getMode() { return mode; }

    public boolean getAlgorithm1() { return alfabeta1; }

    public boolean getAlgorithm2() { return alfabeta2; }

    public int getDepth1() { return depth1; }

    public int getDepth2() { return depth2; }

    public Board getBoard() { return board; }

    //settery
    public void setBoard(Board board) { this.board = board; }

    public void setIsRestart(boolean b) { this.isRestart = b; }

    public void setTurn(int turn) { this.turn = turn; }

    public void setFirstMove(boolean firstMove) { this.firstMove = firstMove; }

    public void setMode(int mode) { this.mode = mode; }

    public void setAlgorithm1(boolean b) { this.alfabeta1 = b; }

    public void setAlgorithm2(boolean b) { this.alfabeta2 = b; }

    public void setDepth1(int depth) { this.depth1 = depth; }

    public void setDepth2(int depth) { this.depth2 = depth; }

    public void setFunction1(IEvaluationFunction f) { this.function1 = f; }

    public void setFunction2(IEvaluationFunction f) { this.function2 = f; }

    public void resetMovesAndTimes(){
        moves1 = 0;
        moves2 = 0;
        moveTime1 = 0;
        moveTime2 = 0;
        time1 = 0;
        time2 = 0;
    }
}
