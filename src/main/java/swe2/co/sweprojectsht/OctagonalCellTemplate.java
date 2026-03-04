package swe2.co.sweprojectsht;

import javafx.scene.shape.Polygon;

static public class OctagonalCell extends Cell{

    public Polygon createOctagon(double x, double y, double radius) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(45 * i + 22.5);
            polygon.getPoints().addAll(
                    x + radius * Math.cos(angle),
                    y + radius * Math.sin(angle)
            );
            //Interactivity
        }
        return polygon;
    }
}
