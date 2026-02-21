package swe2.co.sweprojectsht;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuaxGUI extends Application{
    private Board board = new Board();
    private Label statusLabel = new Label("BLACK to play");
    private Button pieRuleButton = new Button("Activate Pie Rule");

    public void start(Stage stage) {
        stage.setTitle("Quax 1v1");

        BorderPane root = new BorderPane();
        BoardRenderer renderer = new BoardRenderer();

        root.setCenter(renderer.createBoardUI(board));

        pieRuleButton.setVisible(false);
        pieRuleButton.setOnAction(e -> handlePieRule());

        VBox controls = new VBox(20, statusLabel, pieRuleButton);
        controls.setPrefWidth(200);
        root.setRight(controls);

        stage.setResizable(true);
        Scene scene = new Scene(root, 1200, 900);
        stage.setScene(scene);
        stage.show();
    }

    private void handlePieRule() {
        pieRuleButton.setVisible(false);
        statusLabel.setText("Pie Rule Activated! WHITE is now BLACK.");
    }
}
