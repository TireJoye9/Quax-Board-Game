package swe2.co.sweprojectsht;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.IllegalFormatCodePointException;
import java.util.Objects;
import java.util.Queue;

public class OctagonalCellTemplate extends Cell {

    //Somehow need to pass gui into this function
    public Polygon createOctagon(double x, double y, double radius) {
        Polygon polygon = new Polygon();
        GameEngine gameEngine = new GameEngine();
        //Might have to add game engine class in here to dictate cell colours

        QuaxGUI quaxGUI = MainMenu.getQuaxGUI();
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(45 * i + 22.5);
            polygon.getPoints().addAll(
                    x + radius * Math.cos(angle),
                    y + radius * Math.sin(angle)
            );
            //Moved into here as it would have just been overwritten after function called
            polygon.setFill(Color.DARKORANGE);
            polygon.setStroke(Color.BLACK);
        }

        polygon.setOnMouseClicked(mouseEvent -> {
                    //Would Have another if here to say if pie rule was done then yh
                    //If turn == 1 make pie rule visible
                    //if turn == 2 and pi rule == to false, visible == false
                    //else we swap the colours of the 2 tiles and let black go again

                    //handle pie rule here
                    if (Objects.equals(gameEngine.getCurrentTurn(), "BLACK")) {
                        polygon.setFill(Color.BLACK);
                        GameEngine.switchTurn();
                        setOwner("BLACK");
                        quaxGUI.setStatusLabel("BLACK");
                        quaxGUI.checkPieRuleConditions();

                    } else if (Objects.equals(gameEngine.getCurrentTurn(), "WHITE")) {
                        polygon.setFill(Color.WHITE);
                        GameEngine.switchTurn();
                        setOwner("WHITE");
                        quaxGUI.setStatusLabel("WHITE");
                        quaxGUI.checkPieRuleConditions();
                        }
                    //If button was clicked we swap everything back to Black
                    GameEngine.incrementTurn();
                }
        );


        return polygon;
    }
}

