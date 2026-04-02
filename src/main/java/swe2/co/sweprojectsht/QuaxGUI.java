package swe2.co.sweprojectsht;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

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
        if (engine.getCurrentPlayer() == Player.BLACK) {

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

    void resetGame() {

        board = new Board();
        engine = new GameEngine(board, this);
        renderer = new BoardRenderer(board, engine);

        setCenter(renderer);

        updateTurnLabel();
        renderer.render();
    }
}
