package swe2.co.sweprojectsht;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {
    private Stage stage;
    public static QuaxGUI game;

    public MainMenu(Stage stage) {
        this.stage = stage;
    }
    public MainMenu() {
    }

    public void show() {
        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);

        Label title = new Label("WELCOME TO QUAX");

        Button hvhBtn = new Button("1 vs 1 Match");
        hvhBtn.setPrefSize(200, 50);
        hvhBtn.setOnAction(e -> launchGame());

        menuLayout.getChildren().addAll(title, hvhBtn);

        Scene menuScene = new Scene(menuLayout, 400, 300);
        stage.setScene(menuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }

    private void launchGame() {
        //Only exists because setLabel was not working due to it editing different instances of QuaxGui
        game = new QuaxGUI();
        try {
            game.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static QuaxGUI getQuaxGUI() {
        return game;
    }
}
