package swe2.co.sweprojectsht;

import javafx.application.Application;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainMenu menu = new MainMenu(primaryStage);
        menu.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
