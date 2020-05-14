import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {

    public static int redScores = 0;
    public static int yellowScores = 0;

    private boolean tests = true;
    private boolean setDepth = false;
    private int repeat = 1;

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("GameStage.fxml"));
        StackPane stackPane = loader.load();

        GameController controller = loader.getController();

        primaryStage.setScene(new Scene(stackPane));
        primaryStage.setTitle("Connect4 Game");
        primaryStage.show();

        setButtons(controller, primaryStage);

        controller.setTests(tests, repeat);
    }

    private void setButtons(GameController controller, Stage primaryStage){
        controller.getRadio1_button().setOnAction(e -> {
            controller.setMode(0);
            controller.getRadio1_button().setSelected(true);
            controller.getRadio2_button().setSelected(false);
            controller.getRadio3_button().setSelected(false);
            setElementsDisable(controller, false, false, false, true, true,
                    true, true, true, true);
            controller.getStart_button().setDisable(false);
        });

        controller.getRadio2_button().setOnAction(e -> {
            controller.setMode(1);
            controller.getRadio1_button().setSelected(false);
            controller.getRadio2_button().setSelected(true);
            controller.getRadio3_button().setSelected(false);
            setElementsDisable(controller, false, false, false, true, false,
                    true, true, false, false);
            controller.getStart_button().setDisable(false);
        });

        controller.getRadio3_button().setOnAction(e -> {
            controller.setMode(2);
            controller.getRadio1_button().setSelected(false);
            controller.getRadio2_button().setSelected(false);
            controller.getRadio3_button().setSelected(true);
            setElementsDisable(controller, false, false, false, false, false,
                    false, false, false, false);
            controller.getStart_button().setDisable(false);
        });

        controller.getStart_button().setOnAction(e -> {
            controller.setIsRestart(false);

            if(controller.getRadio1_button().isSelected()) {
                controller.setMode(0);
            }
            if(controller.getRadio2_button().isSelected()) {
                controller.setMode(1);
            }
            if(controller.getRadio3_button().isSelected()){
                controller.setMode(2);
            }

            if(controller.getAlgorithm1_combobox().getValue() != null) {
                String option = (String) controller.getAlgorithm1_combobox().getValue();
                if(option.equals("minimax"))
                    controller.setAlgorithm1(false);
                else if(option.equals("alfa-beta"))
                    controller.setAlgorithm1(true);
            }

            if(controller.getAlgorithm2_combobox().getValue() != null) {
                String option = (String) controller.getAlgorithm2_combobox().getValue();
                if(option.equals("minimax"))
                    controller.setAlgorithm2(false);
                else if(option.equals("alfa-beta"))
                    controller.setAlgorithm2(true);
            }

            if(controller.getDepth1_combobox().getValue() != null) {
                int val = (int) controller.getDepth1_combobox().getValue();
                controller.setDepth1(val);
            }

            if(controller.getDepth2_combobox().getValue() != null) {
                int val = (int) controller.getDepth2_combobox().getValue();
                controller.setDepth2(val);
            }

            if(controller.getFunction1_combobox().getValue() != null) {
                String option = (String) controller.getFunction1_combobox().getValue();
                if(option.equals("Function win-lose"))
                    controller.setFunction1(new EvaluationFunctionWinLose());
                else if(option.equals("Function 3 next to each other"))
                    controller.setFunction1(new EvaluationFunctionThree());
                else if(option.equals("Function empty spaces in column"))
                    controller.setFunction1(new EvaulationFunctionEmptySpaces());
            }

            if(controller.getFunction2_combobox().getValue() != null) {
                String option = (String) controller.getFunction2_combobox().getValue();
                if(option.equals("Function win-lose"))
                    controller.setFunction2(new EvaluationFunctionWinLose());
                else if(option.equals("Function 3 next to each other"))
                    controller.setFunction2(new EvaluationFunctionThree());
                else if(option.equals("Function empty spaces in column"))
                    controller.setFunction2(new EvaulationFunctionEmptySpaces());
            }

            new Results().writeAllParameters(controller.getDepth1(), controller.getDepth2(),
                    controller.getAlgorithm1(), controller.getAlgorithm2());

            if(tests) {
                if(setDepth) {
                    controller.setDepth1(controller.getDepth1() - controller.n + 1);
                    if (controller.getDepth1() <= 0)
                        controller.setDepth1(1);
                }
                controller.n --;
            }

            controller.getBoard_pane().setDisable(false);
            setElementsDisable(controller, true, true, true, true, true,
                    true, true, true, true);
            controller.getStart_button().setDisable(true);
            controller.start();
        });

        controller.getRestart_button().setOnAction(e -> {
            try {
//                restartStage(primaryStage, controller);
                controller.setIsRestart(true);
                controller.setBoard(new Board());
                controller.getDisc_pane().getChildren().clear();
                controller.resetMovesAndTimes();
                controller.setFirstMove(true);
                controller.setTurn(0);

                if(controller.getRadio1_button().isSelected())
                    setElementsDisable(controller, false, false, false, true, true,
                            true, true, true, true);
                if(controller.getRadio2_button().isSelected())
                    setElementsDisable(controller, false, false, false, true, false,
                            true, true, false, false);
                if(controller.getRadio3_button().isSelected())
                    setElementsDisable(controller, false, false, false, false, false,
                            false, false, false, false);

                controller.getRadio1_button().setDisable(false);
                controller.getRadio2_button().setDisable(false);
                controller.getRadio3_button().setDisable(false);
                controller.getStart_button().setDisable(false);
                controller.getWin_text().setVisible(false);
                controller.getError_text().setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

//    private void restartStage(Stage primaryStage, GameController controller) throws Exception {
//        start(primaryStage);
//        controller.getBoard_pane().setDisable(false);
//    }

    private void setElementsDisable(GameController controller, boolean rabioButton1, boolean rabioButton2, boolean rabioButton3,
                                   boolean algorithm1, boolean algorithm2, boolean depth1, boolean function1,
                                   boolean depth2, boolean function2){
        controller.getRadio1_button().setDisable(rabioButton1);
        controller.getRadio2_button().setDisable(rabioButton2);
        controller.getRadio3_button().setDisable(rabioButton3);
        controller.getAlgorithm1_combobox().setDisable(algorithm1);
        controller.getAlgorithm2_combobox().setDisable(algorithm2);
        controller.getDepth1_combobox().setDisable(depth1);
        controller.getFunction1_combobox().setDisable(function1);
        controller.getDepth2_combobox().setDisable(depth2);
        controller.getFunction2_combobox().setDisable(function2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
