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

    @Override
    public void start(Stage stage) {
        stage.setTitle("Quax 1v1");

        BorderPane root = new BorderPane();
        BoardRenderer renderer = new BoardRenderer();
        root.setCenter(renderer.createBoardUI(board));

        VBox controls = new VBox(10, statusLabel, pieRuleButton);
        pieRuleButton.setVisible(false);
        root.setRight(controls);

        Scene scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.show();
    }
}
