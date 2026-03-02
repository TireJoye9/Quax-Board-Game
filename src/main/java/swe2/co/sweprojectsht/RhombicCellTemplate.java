package swe2.co.sweprojectsht;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Objects;

public class RhombicCellTemplate extends Cell{


    public Polygon createRhombus(double x, double y, double size) {
        Polygon polygon = new Polygon();
        GameEngine gameEngine = new GameEngine();
        QuaxGUI quaxGUI = MainMenu.getQuaxGUI();

        polygon.getPoints().addAll(
                x, y - size,
                x + size, y,
                x, y + size,
                x - size, y
        );
        //Moved into here as it would have just been overwritten after function called
        polygon.setFill(Color.LIGHTBLUE);
        polygon.setStroke(Color.BLACK);

        polygon.setOnMouseClicked( mouseEvent -> {
                    //Would Have another if here to say if pie rule was done then yh
                    //If turn == 1 make pie rule visible
                    //if turn == 2 and pi rule == to false, visible == false
                    //else we swap the colours of the 2 tiles and let black go again


                    if (Objects.equals(gameEngine.getCurrentTurn(), "BLACK"))
                    {
                        polygon.setFill(Color.BLACK);
                        gameEngine.switchTurn();
                        setOwner("BLACK");
                        quaxGUI.setStatusLabel("BLACK");


                    }
                    else if (Objects.equals(gameEngine.getCurrentTurn(), "WHITE"))
                    {
                        polygon.setFill(Color.WHITE);
                        gameEngine.switchTurn();
                        setOwner("WHITE");
                        quaxGUI.setStatusLabel("WHITE");
                    }
                }
        );

        return polygon;
    }
}
