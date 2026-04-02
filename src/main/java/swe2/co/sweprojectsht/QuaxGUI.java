package swe2.co.sweprojectsht;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class QuaxGUI extends BorderPane {
    private Label turn;             // Turn Label to know who's next
    private Board board;
    private GameEngine engine;
    private BoardRenderer renderer;

    public  QuaxGUI() {

        board = new Board();                            // Creates new game board
        engine = new GameEngine(board, this);   // Creates game engine so we can call resetGame()
        renderer = new BoardRenderer(board, engine);    // Responsible for drawing and handling clicks

        turn = new Label();                     // Creates the label
        turn.setStyle("-fx-font-size: 16px");   // Good size to be able to see it
        updateTurnLabel();                      // Set the initial text

        setTop(turn);           // Puts label at the top
        setCenter(renderer);    // Puts board in the middle

        renderer.render();  // Draws the board

        renderer.setOnMouseClicked(e -> checkSwapRule()); // Waits for a click, and enables the swap rule
    }

    // Changes the turn label from BLACK to WHITE and repeat
    public void updateTurnLabel() {

        if (engine.getCurrentPlayer() == Player.BLACK) {
            turn.setText("BLACK to play:");
        } else {
            turn.setText("WHITE to play:");
        }
    }

    // Handles swap rule, only after player number 1 makes a move
    void checkSwapRule() {

        // Necessary to prevent infinite loop of asking to swap
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

        Optional<ButtonType> result = alert.showAndWait(); // Wait for user to click

        if (result.isPresent() && result.get() == yes) {
            engine.performSwap();
            updateTurnLabel(); // Change label
            renderer.render(); // Redraw board with new colour swap
        }

        engine.markSwapOffered(); // Prevents from alert appearing again
    }

    // Resets the game to play again
    void resetGame() {

        board = new Board();
        engine = new GameEngine(board, this);
        renderer = new BoardRenderer(board, engine);

        setCenter(renderer); // Replace the old board UI

        updateTurnLabel(); // Reset the turn label
        renderer.render(); // Redraw board
    }
}
