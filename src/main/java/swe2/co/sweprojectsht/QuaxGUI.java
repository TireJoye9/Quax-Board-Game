package swe2.co.sweprojectsht;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class QuaxGUI extends BorderPane {
    private Label turn;
    private Board board;
    private GameEngine engine;
    private BoardRenderer renderer;

    public  QuaxGUI() {

        board = new Board();
        engine = new GameEngine(board, this);
        renderer = new BoardRenderer(board, engine);

        turn = new Label();
        turn.setStyle("-fx-font-size: 16px");
        updateTurnLabel();

        setTop(turn);
        setCenter(renderer);

        renderer.render();

        renderer.setOnMouseClicked(e -> checkSwapRule());
    }

    public void updateTurnLabel() {

        if (engine.getCurrentPlayer() == Player.BLACK) {
            turn.setText("BLACK to play:");
        } else {
            turn.setText("WHITE to play:");
        }
    }

    void checkSwapRule() {

        if (!engine.swapOffer()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Swap Rule");
        alert.setHeaderText("Player 2");
        alert.setContentText("Do you want to swap colours?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");

        alert.getButtonTypes().setAll(yes, no);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yes) {
            engine.performSwap();
            updateTurnLabel();
            renderer.render();
        }

        engine.markSwapOffered();
    }

    void resetGame() {

        board = new Board();
        engine = new GameEngine(board, this);
        renderer = new BoardRenderer(board, engine);

        setCenter(renderer);

        updateTurnLabel();
        renderer.render();
    }
}
