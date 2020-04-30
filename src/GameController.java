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

public class GameController {

    private static final int TILE_SIZE = 100;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    private IEvaluationFunction function = new EvaluationFunctionWinLose();
    private char lastPlayer = 'x';
    private boolean firstMove = true;
    private int nextMoveLocation = 3;
    private int depth = 5;
    private int turn = 0;
    private int mode = 0;
    private Board board;


    public GameController(){
    }

    @FXML
    void initialize(){
        board = new Board();
        board_pane.getChildren().add(makeGrid());
        if(mode == 0 || mode == 1)
            board_pane.getChildren().addAll(makeColumns());
        board_pane.setDisable(true);
        start_button.setDisable(true);
        scores_red_text.setText(String.valueOf(GUI.redScores));
        scores_yellow_text.setText(String.valueOf(GUI.yellowScores));

        for(int i=1; i < 9; i++)
            depth_combobox.getItems().add(i);
        function_combobox.getItems().add(new EvaluationFunctionWinLose());
        depth_combobox.setOnAction(e  -> {
            if(getDepth_combobox().getValue() != null && getFunction_combobox().getValue() != null)
                start_button.setDisable(false);
        } );
        function_combobox.setOnAction(e  -> {
            if(getDepth_combobox().getValue() != null && getFunction_combobox().getValue() != null)
                start_button.setDisable(false);
        } );
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

        placeCircle(column);
        lastPlayer = getOtherPlayer(lastPlayer);
        makeMove(column, lastPlayer, board);
        board.displayBoard();

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

//        board_pane.setDisable(true);
            TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), circle);
            animation.setToY((ROWS - row - 1) * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation.setOnFinished(e -> {
                if (board.checkIfWin(lastPlayer, board)) {
                    gameOver();
                }
                else {
                    if (mode == 1 && turn == 1) {
                        turn = 2;
                        lastPlayer = getOtherPlayer(lastPlayer);
                        makeAImove();
                        board.displayBoard();
                    }

                    if (mode == 2) {
                        turn = (turn == 1) ? 2 : 1;
                        lastPlayer = getOtherPlayer(lastPlayer);
                        makeAImove();
                        board.displayBoard();
                    }
                }
//            board_pane.setDisable(false);
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
        char player = lastPlayer;
        Board board = new Board(this.board);

        if(firstMove)
            firstMove = false;
        else
            minimax(0, true, player, board);

        System.out.println("Next AI move: " + nextMoveLocation);
        placeCircle(nextMoveLocation);
        makeMove(nextMoveLocation, lastPlayer, this.board);
    }


    public int minimax(int level, boolean isMax, char player, Board board) {
        if(level == depth || !board.isEmptyField() || board.checkIfWin(player, board)
                || board.checkIfWin(getOtherPlayer(player), board)){
            return function.evalueStatus(board, player, turn);
        }

        if (isMax) {
            int maxScore = Integer.MIN_VALUE;
            int value = 0;

            for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
                Board temp = new Board(board);
                if (makeMove(i, player, temp)) {
                    value = minimax(level + 1, false, getOtherPlayer(player), temp);
                }
                if(level == 0 && board.firstEmptyInCol(i) != -1){
                    if(value >= maxScore) {
                        nextMoveLocation = i;
                    }
//                    System.out.println("Score for location "+i+" = "+value);
                }
                maxScore = Math.max(value, maxScore);
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            int value = 0;

            for (int i = 0; i < Board.BOARD_COLUMNS; i++) {
                Board temp = new Board(board);
                if (makeMove(i, player, temp)) {
                    value = minimax(level + 1, true, getOtherPlayer(player), temp);
                }
                minScore = Math.min(value, minScore);
            }
            return minScore;
        }
    }

    private void gameOver() {
        if(turn == 1) {
            GUI.redScores++;
            scores_red_text.setText(String.valueOf(GUI.redScores));
            win_text.setText("Red Wins!");
        } else {
            GUI.yellowScores++;
            scores_yellow_text.setText(String.valueOf(GUI.yellowScores));
            win_text.setText("Yellow Wins!");
        }

        win_text.setVisible(true);
        board_pane.setDisable(true);
        start_button.setDisable(true);

        System.out.println("Winner: " + (turn == 1 ? "RED" : "YELLOW"));
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
    private Text game_mode_text;

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
    private ComboBox depth_combobox;

    @FXML
    private ComboBox function_combobox;


    public Button getStart_button(){
        return start_button;
    }

    public Button getRestart_button(){
        return restart_button;
    }

    public RadioButton getRadio1_button(){
        return radio1_button;
    }

    public RadioButton getRadio2_button(){
        return radio2_button;
    }

    public RadioButton getRadio3_button(){
        return radio3_button;
    }

    public ComboBox getDepth_combobox(){
        return depth_combobox;
    }

    public ComboBox getFunction_combobox(){
        return function_combobox;
    }

    public Text getGame_mode_text(){
        return game_mode_text;
    }

    public Pane getBoard_pane(){
        return board_pane;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setFunction(IEvaluationFunction f) {
        this.function = f;
    }
}
