package swe2.co.sweprojectsht;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class BoardRenderer {
    public GridPane createBoardUI(Board board) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        String[] alphabets = {"A","B","C","D","E","F","G","H","I","J","K"};
        for (int i = 0; i < 11; i++) {
            grid.add(new Label(alphabets[i]), i + 1, 0);
            grid.add(new Label(alphabets[i]), i + 1, 12);
        }

        for (int i = 0; i < 11; i++) {
            grid.add(new Label(String.valueOf(i + 1)), 0, i + 1);
            grid.add(new Label(String.valueOf(i + 1)), 12, i + 1);
        }

        for (int r = 0; r < 11; r++) {
            for (int c = 0; c < 11; c++) {
                Polygon octagon = createOctagon(20);
                octagon.setFill(Color.LIGHTGRAY);
                octagon.setStroke(Color.BLACK);
                grid.add(octagon, c + 1, r + 1);
            }
        }
        return grid;
    }

    private Polygon createOctagon(double radius) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(45 * i + 22.5);
            polygon.getPoints().addAll(
                    radius * Math.cos(angle),
                    radius * Math.sin(angle)
            );
        }
        return polygon;
    }
}
