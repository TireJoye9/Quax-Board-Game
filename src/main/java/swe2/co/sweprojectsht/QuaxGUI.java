package swe2.co.sweprojectsht;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import javafx.application.Platform;


import java.util.Optional;

public class QuaxGUI extends BorderPane {
    private Label turn;
    private Board board;
    private GameEngine engine;
    private BoardRenderer renderer;
    private GreedyBot greedyBot;
    private boolean isProcessingSwap = false;  // ← ADD THIS LINE (Fix 1)


    // For strategy highlighting
    private boolean showBotStrategy = false;

    // For preventing multiple clicks
    private boolean isBotThinking = false;

    public QuaxGUI() {
        board = new Board();
        engine = new GameEngine(board, this);
        greedyBot = new GreedyBot(board, engine);
        renderer = new BoardRenderer(board, engine);

        turn = new Label();
        turn.setStyle("-fx-font-size: 16px");
        turn.setAlignment(Pos.CENTER);
        updateTurnLabel();

        // Create button bar
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setStyle("-fx-padding: 10px;");

        // Show Strategy Button
        Button showStrategyBtn = new Button("Show Strategy");
        showStrategyBtn.setOnAction(e -> {
            showBotStrategy = !showBotStrategy;
            if (showBotStrategy) {
                int[] bestMove = greedyBot.getBestMove();
                if (bestMove != null) {
                    renderer.setCurrentBestMove(bestMove);
                }
            } else {
                renderer.setCurrentBestMove(null);
            }
            renderer.setShowStrategy(showBotStrategy);
            renderer.render();
        });

        buttonBar.getChildren().add(showStrategyBtn);

        // Create top bar with turn label and buttons
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER);
        topBar.setStyle("-fx-padding: 10px;");
        topBar.getChildren().addAll(turn, buttonBar);

        setTop(topBar);
        setCenter(renderer);

        renderer.render();

        renderer.setOnMouseClicked(e -> checkSwapRule());
    }

    public void updateTurnLabel() {
        HBox shapeContainer = new HBox(5);
        if (engine.getHumanPlayer() == Player.BLACK) {
            Polygon octogon = createOctagonForLabel(20, 20, 12);
            octogon.setFill(Color.BLACK);
            Polygon diamond = createDiamondForLabel(20, 20, 12);
            diamond.setFill(Color.BLACK);
            shapeContainer.getChildren().addAll(octogon, diamond);
            turn.setGraphic(shapeContainer);
            turn.setText(" --------> BLACK to play:");
        } else {
            Polygon octogon = createOctagonForLabel(20, 20, 12);
            octogon.setFill(Color.WHITE);
            Polygon diamond = createDiamondForLabel(20, 20, 12);
            diamond.setFill(Color.WHITE);
            shapeContainer.getChildren().addAll(octogon, diamond);
            turn.setGraphic(shapeContainer);
            turn.setText(" --------> WHITE to play:");
        }
    }

    private Polygon createOctagonForLabel(double cx, double cy, double r) {
        Polygon p = new Polygon();
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(45 * i + 22.5);
            double x = cx + r * Math.cos(angle);
            double y = cy + r * Math.sin(angle);
            p.getPoints().addAll(x, y);
        }
        p.setStroke(Color.BLACK);
        return p;
    }

    private Polygon createDiamondForLabel(double cx, double cy, double size) {
        Polygon d = new Polygon(
                cx, cy - size,
                cx + size, cy,
                cx, cy + size,
                cx - size, cy
        );
        d.setStroke(Color.BLACK);
        return d;
    }

    void checkSwapRule() {
        // Prevent multiple popups
        if (!engine.swapOffer() || isProcessingSwap) {
            return;
        }

        isProcessingSwap = true;

        Platform.runLater(() -> {
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

                greedyBot.updateBoard(board);

                updateTurnLabel();
                renderer.render();
            }

            engine.markSwapOffered();
            isProcessingSwap = false;
        });
    }

    protected void makeBotMove() {
        // Don't trigger if bot is already thinking or game is over
        if (isBotThinking || engine.isGameOver()) {
            return;
        }

        isBotThinking = true;

        // Small delay so bot doesn't move instantly
        Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            executeBotMove();
            isBotThinking = false;
        }));
        delay.setCycleCount(1);
        delay.play();
    }

    protected void executeBotMove() {
        if (engine.isGameOver()) {
            isBotThinking = false;
            return;
        }

        // Use GreedyBot to find the best move
        int[] bestMove = greedyBot.getBestMove();

        if (bestMove == null) {
            isBotThinking = false;
            return;
        }

        int r = bestMove[0];
        int c = bestMove[1];
        int type = bestMove[2];

        boolean success;
        if (type == 0) {
            success = engine.botPlacePiece(r, c);
        } else {
            success = engine.botPlaceBridge(r, c);
        }

        if (success) {
            renderer.render();
            updateTurnLabel();

            if (showBotStrategy) {
                renderer.setCurrentBestMove(null);
            }
        }

        isBotThinking = false;
    }

    void resetGame() {
        board = new Board();
        engine = new GameEngine(board, this);
        greedyBot = new GreedyBot(board, engine);
        renderer = new BoardRenderer(board, engine);

        setCenter(renderer);

        updateTurnLabel();
        renderer.render();
    }

    public boolean isBotThinking() {
        return isBotThinking;
    }

    public boolean isGameOver() {
        return engine.isGameOver();
    }
}