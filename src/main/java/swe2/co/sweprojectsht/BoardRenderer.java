package swe2.co.sweprojectsht;

import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardRenderer {
    private final double SPACING = 35.0;
    private final double OFFSET = 80.0;

    public Pane createBoardUI(Board board) {
        Pane pane = new Pane();

        // 1. OUTER FRAME
        double outerFrameSize = (10 * SPACING) + 120;
        Rectangle outerFrame = new Rectangle(OFFSET - 60, OFFSET - 60, outerFrameSize, outerFrameSize);
        outerFrame.setFill(Color.LIGHTGRAY);
        outerFrame.setStroke(Color.BLACK);
        outerFrame.setStrokeWidth(2);
        pane.getChildren().add(outerFrame);

        // 2. INNER FRAME
        double innerFrameSize = (10 * SPACING) + 40;
        Rectangle innerFrame = new Rectangle(OFFSET - 20, OFFSET - 20, innerFrameSize, innerFrameSize);
        innerFrame.setFill(Color.WHITE);
        innerFrame.setStroke(Color.BLACK);
        innerFrame.setStrokeWidth(2);
        pane.getChildren().add(innerFrame);

        String[] alphabets = {"A","B","C","D","E","F","G","H","I","J","K"};

        // 3. PLACE LABELS
        for (int i = 0; i < 11; i++) {
            // Top Labels
            Label topL = new Label(alphabets[i]);
            topL.setLayoutX(OFFSET + i * SPACING - 5);
            topL.setLayoutY(OFFSET - 45);

            // Bottom Labels
            Label botL = new Label(alphabets[i]);
            botL.setLayoutX(OFFSET + i * SPACING - 5);
            botL.setLayoutY(OFFSET + 10 * SPACING + 25);

            // Left Labels
            Label leftL = new Label(String.valueOf(i + 1));
            leftL.setLayoutX(OFFSET - 50);
            leftL.setLayoutY(OFFSET + i * SPACING - 10);

            // Right Labels
            Label rightL = new Label(String.valueOf(i + 1));
            rightL.setLayoutX(OFFSET + 10 * SPACING + 35);
            rightL.setLayoutY(OFFSET + i * SPACING - 10);

            pane.getChildren().addAll(topL, botL, leftL, rightL);
        }

        // 4. DRAW TILES
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                double rx = OFFSET + (c * SPACING) + (SPACING / 2);
                double ry = OFFSET + (r * SPACING) + (SPACING / 2);

                Polygon rhombus = createRhombus(rx, ry, SPACING / 2);
                rhombus.setFill(Color.SKYBLUE);
                rhombus.setStroke(Color.DARKGRAY);
                pane.getChildren().add(rhombus);
            }
        }

        // 5. DRAW STONES
        for (int r = 0; r < 11; r++) {
            for (int c = 0; c < 11; c++) {
                double ox = OFFSET + c * SPACING;
                double oy = OFFSET + r * SPACING;

                double apothem = SPACING / 2.0;
                double radiusToTouch = apothem / Math.cos(Math.toRadians(22.5));

                Polygon octagon = createOctagon(ox, oy, radiusToTouch);
                octagon.setFill(Color.LIGHTBLUE);
                octagon.setStroke(Color.BLACK);
                pane.getChildren().add(octagon);
            }
        }

        return pane;
    }

    private Polygon createOctagon(double x, double y, double radius) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(45 * i + 22.5);
            polygon.getPoints().addAll(
                    x + radius * Math.cos(angle),
                    y + radius * Math.sin(angle)
            );
        }
        return polygon;
    }

    private Polygon createRhombus(double x, double y, double size) {
        Polygon polygon = new Polygon();

        polygon.getPoints().addAll(
                x, y - size,
                x + size, y,
                x, y + size,
                x - size, y
        );
        return polygon;
    }
}
