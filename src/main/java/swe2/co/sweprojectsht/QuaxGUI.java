package swe2.co.sweprojectsht;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
        turn.setAlignment(Pos.CENTER);
        updateTurnLabel();

        setTop(turn);
        setCenter(renderer);

        renderer.render();

        renderer.setOnMouseClicked(e -> checkSwapRule());
    }

    public void updateTurnLabel() {

        //Set graphic can only set one element so I have to create a container
        HBox shapeContainer = new HBox(5); // 5px spacing between shapes
        if (engine.getHumanPlayer() == Player.BLACK) {

            Polygon octogon = createOctagonForLabel(20, 20, 12);
            octogon.setFill(Color.BLACK);

            Polygon diamond = createDiamondForLabel(20, 20, 12);
            diamond.setFill(Color.BLACK);

            shapeContainer.getChildren().addAll(octogon, diamond);
            turn.setGraphic(shapeContainer);
            turn.setText(" --------> BLACK to play:");
        } else {
            //Set graphic can only set one element so I have to create a container

                Polygon octogon = createOctagonForLabel(20, 20, 12);
                octogon.setFill(Color.WHITE);


                Polygon diamond = createDiamondForLabel(20, 20, 12);
                diamond.setFill(Color.WHITE);

                shapeContainer.getChildren().addAll(octogon, diamond);
                turn.setGraphic(shapeContainer);
                turn.setText(" --------> White to play:");

        }
    }

    //Add functions to create octagon and rhombus for label
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

    protected void makeBotMove() {
        // Small delay so bot doesn't move instantly
        Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            executeBotMove();
        }));
        delay.setCycleCount(1);
        delay.play();
    }

    protected void executeBotMove() {
        if (engine.checkWin(engine.getBotPlayer())) {
            return;
        }

        // Collect all empty octagon positions
        List<int[]> emptyOctagons = new ArrayList<>();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                if (board.getOctagon(r, c).getOwner() == Player.NONE) {
                    emptyOctagons.add(new int[]{r, c, 0}); // 0 = octagon
                }
            }
        }

        // Collect all empty diamond (bridge) positions
        List<int[]> emptyDiamonds = new ArrayList<>();
        for (int r = 0; r < Board.SIZE - 1; r++) {
            for (int c = 0; c < Board.SIZE - 1; c++) {
                if (board.getDiamond(r, c).getOwner() == Player.NONE) {
                    emptyDiamonds.add(new int[]{r, c, 1}); // 1 = diamond
                }
            }
        }

        // Combine all possible moves
        List<int[]> allMoves = new ArrayList<>();
        allMoves.addAll(emptyOctagons);
        allMoves.addAll(emptyDiamonds);

        if (allMoves.isEmpty()) {
            return; // No moves available
        }

        // Pick random move
        Random rand = new Random();
        int[] move = allMoves.get(rand.nextInt(allMoves.size()));
        int r = move[0];
        int c = move[1];
        int type = move[2];

        boolean success;
        if (type == 0) {
            success = engine.botPlacePiece(r, c);
        } else {
            success = engine.botPlaceBridge(r, c);
        }

        if (success) {
            renderer.render(); // Update the board
            updateTurnLabel();


        }
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
