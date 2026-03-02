package swe2.co.sweprojectsht;

import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardRenderer {
    private final double SPACING = 35.0;
    private final double OFFSET = 80.0;
    private final int NUMBEROFTILES = 11;

    public Pane createBoardUI(Board board) {
        //Creates the cells
        OctagonalCellTemplate octagonalCellTemplate = new OctagonalCellTemplate();
        RhombicCellTemplate rhombicCellTemplate = new RhombicCellTemplate();
        Pane pane = new Pane();

        // 1. OUTER FRAME
        double outerFrameSize = (10 * SPACING) + 120;
        Rectangle outerFrame = new Rectangle(OFFSET - 60, OFFSET - 60, outerFrameSize, outerFrameSize);
        outerFrame.setFill(Color.ORANGE);
        outerFrame.setStroke(Color.BLACK);
        outerFrame.setStrokeWidth(2);
        //Adds Frame to visual pane
        pane.getChildren().add(outerFrame);

        // 2. INNER FRAME
        double innerFrameSize = (10 * SPACING) + 40;
        Rectangle innerFrame = new Rectangle(OFFSET - 20, OFFSET - 20, innerFrameSize, innerFrameSize);
        innerFrame.setFill(Color.WHITE);
        innerFrame.setStroke(Color.BLACK);
        innerFrame.setStrokeWidth(2);
        //Adds Frame to visual pane
        pane.getChildren().add(innerFrame);

        String[] alphabets = {"A","B","C","D","E","F","G","H","I","J","K"};

        // 3. PLACE LABELS
        for (int i = 0; i < 11; i++) {
            // Top Labels
            Label topLabel = new Label(alphabets[i]);
            topLabel.setLayoutX(OFFSET + i * SPACING - 5);
            topLabel.setLayoutY(OFFSET - 45);

            // Bottom Labels
            Label botLable = new Label(alphabets[i]);
            botLable.setLayoutX(OFFSET + i * SPACING - 5);
            botLable.setLayoutY(OFFSET + 10 * SPACING + 25);

            // Left Labels
            Label leftLable = new Label(String.valueOf(i + 1));
            leftLable.setLayoutX(OFFSET - 50);
            leftLable.setLayoutY(OFFSET + i * SPACING - 10);

            // Right Labels
            Label rightLable = new Label(String.valueOf(i + 1));
            rightLable.setLayoutX(OFFSET + 10 * SPACING + 35);
            rightLable.setLayoutY(OFFSET + i * SPACING - 10);

            pane.getChildren().addAll(topLabel, botLable, leftLable, rightLable);
        }

        // 4. DRAW TILES
        for (int r = 0; r < NUMBEROFTILES -1; r++) {
            for (int c = 0; c < NUMBEROFTILES -1; c++) {
                double rx = OFFSET + (c * SPACING) + (SPACING / 2);
                double ry = OFFSET + (r * SPACING) + (SPACING / 2);

                Polygon rhombus = rhombicCellTemplate.createRhombus(rx, ry, SPACING / 2);
                rhombus.setFill(Color.SKYBLUE);
                rhombus.setStroke(Color.DARKGRAY);
                pane.getChildren().add(rhombus);
            }
        }

        // 5. DRAW STONES
        for (int r = 0; r < NUMBEROFTILES; r++) {
            for (int c = 0; c < NUMBEROFTILES; c++) {
                double ox = OFFSET + c * SPACING;
                double oy = OFFSET + r * SPACING;

                //a line from the centre of a regular polygon at right angles to any of its sides.
                double apothem = SPACING / 2.0;
                //ensures octagons fit together perfectly without gaps
                double radiusToTouch = apothem / Math.cos(Math.toRadians(22.5));

                Polygon octagon = octagonalCellTemplate.createOctagon(ox, oy, radiusToTouch);
                octagon.setFill(Color.DARKORANGE);
                octagon.setStroke(Color.BLACK);
                pane.getChildren().add(octagon);
            }
        }

        return pane;
    }
}
