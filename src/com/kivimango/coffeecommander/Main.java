package com.kivimango.coffeecommander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main extends Application {

    private static final String APP_TITLE = "Coffee Commander";
    private static final String APP_VERSION = "0.1";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle(APP_TITLE + " " + APP_VERSION);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.75);
        int height = (int) ((screenSize.height * 0.75));
        primaryStage.setScene(new Scene(root, width, height));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
