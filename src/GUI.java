import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {

    public static int redScores = 0;
    public static int yellowScores = 0;

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
    }

    private void setButtons(GameController controller, Stage primaryStage){
        controller.getRadio1_button().setOnAction(e -> {
            controller.getRadio1_button().setSelected(true);
            controller.getRadio2_button().setSelected(false);
            controller.getRadio3_button().setSelected(false);
        });

        controller.getRadio2_button().setOnAction(e -> {
            controller.getRadio1_button().setSelected(false);
            controller.getRadio2_button().setSelected(true);
            controller.getRadio3_button().setSelected(false);
        });

        controller.getRadio3_button().setOnAction(e -> {
            controller.getRadio1_button().setSelected(false);
            controller.getRadio2_button().setSelected(false);
            controller.getRadio3_button().setSelected(true);
        });

        controller.getStart_button().setOnAction(e -> {
            if(controller.getRadio1_button().isSelected()) {
                controller.setMode(0);
                controller.getGame_mode_text().setText("Game Human vs Human");
                controller.getGame_mode_text().setVisible(true);
            }
            if(controller.getRadio2_button().isSelected()) {
                controller.setMode(1);
                controller.getGame_mode_text().setText("Game Human vs AI");
                controller.getGame_mode_text().setVisible(true);
            }
            if(controller.getRadio3_button().isSelected()){
                controller.setMode(2);
                controller.getGame_mode_text().setText("Game AI vs AI");
                controller.getGame_mode_text().setVisible(true);
            }
//            int val;
            if(controller.getDepth_combobox().getValue() != null) {
                int val = (int) controller.getDepth_combobox().getValue();
                controller.setDepth(val);
            }

            if(controller.getFunction_combobox().getValue() != null) {
                IEvaluationFunction fun = (IEvaluationFunction) controller.getFunction_combobox().getValue();
                controller.setFunction(fun);
            }

            controller.getBoard_pane().setDisable(false);
            controller.getRadio1_button().setDisable(true);
            controller.getRadio2_button().setDisable(true);
            controller.getRadio3_button().setDisable(true);
            controller.getDepth_combobox().setDisable(true);
            controller.getFunction_combobox().setDisable(true);
            controller.start();
        });

        controller.getRestart_button().setOnAction(e -> {
            try {
                restartStage(primaryStage, controller);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void restartStage(Stage primaryStage, GameController controller) throws Exception {
        start(primaryStage);
        controller.getBoard_pane().setDisable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
