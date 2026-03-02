package swe2.co.sweprojectsht;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.Objects;

public class QuaxGUI extends Application{
    private Label statusLabel = new Label("WHITE TO  PLAY");
    private Button pieRuleButton = new Button("Activate Pie Rule");
    public static boolean pieRuleButtonClicked = false;

    public void start(Stage stage) {
        stage.setTitle("Quax 1v1");
        Board board = new Board();

        BorderPane root = new BorderPane();
        BoardRenderer renderer = new BoardRenderer();

        root.setCenter(renderer.createBoardUI(board));

        pieRuleButton.setVisible(true);
        pieRuleButton.setOnAction(e -> handlePieRule());

        VBox controls = new VBox(30);
        controls.setPrefWidth(300);
        controls.getChildren().addAll(statusLabel,pieRuleButton);
        root.setRight(controls);

        stage.setResizable(true);
        Scene scene = new Scene(root, 1000, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void setStatusLabel(String colour)
    {
        if (Objects.equals(colour, "WHITE")) {
            statusLabel.setText("BLACK TO PLAY");
        } else if (Objects.equals(colour ,"BLACK")){
            statusLabel.setText("WHITE TO PLAY");
        }
    }


    public void handlePieRule() {
        pieRuleButton.setText("Pie Rule Activated! WHITE is now BLACK.");
        statusLabel.setText("WHITE TO PLAY");
        pieRuleButtonClicked = true;

    }
    public void checkPieRuleConditions()
    {
        if (pieRuleButtonClicked && GameEngine.getCurrentTurnNumber() > 1)
        {
            pieRuleButton.setVisible(false);
        }
    }

}
