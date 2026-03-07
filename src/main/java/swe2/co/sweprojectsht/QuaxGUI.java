package swe2.co.sweprojectsht;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class QuaxGUI extends BorderPane {
    private Board board;
    private GameEngine engine;
    private BoardRenderer renderer;

    public  QuaxGUI() {

        board = new Board();
        engine = new GameEngine(board, this);
        renderer = new BoardRenderer(board, engine);

        setCenter(renderer);

        renderer.render();

        renderer.setOnMouseClicked(e -> checkSwapRule());
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
        }

        engine.markSwapOffered();
    }

    void resetGame() {

        board = new Board();
        engine = new GameEngine(board, this);
        renderer = new BoardRenderer(board, engine);

        setCenter(renderer);
        renderer.render();
    }
}
