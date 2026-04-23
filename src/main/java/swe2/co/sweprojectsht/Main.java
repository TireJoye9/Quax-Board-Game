package swe2.co.sweprojectsht;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {

    public void start(Stage stage) {

        QuaxGUI gui = new QuaxGUI();

        Scene scene = new Scene(gui, 800, 800);

        stage.setTitle("Quax");

        stage.setScene(scene);

        stage.show();
    }
}
