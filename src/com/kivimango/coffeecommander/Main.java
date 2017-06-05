package com.kivimango.coffeecommander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Coffee Commander
 * A free, platform-independent file manager application written in JAVA using JavaFX library.
 *
 * @author kivimango
 * @version 0.1
 * @link https://github.com/kivimango/coffee-commander
 */

public class Main extends Application {

    public static final String APP_TITLE = "Coffee Commander";
    public static final String APP_VERSION = "0.1";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
        Pane root = loader.load();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.85);
        int height = (int) ((screenSize.height * 0.75));

        primaryStage.setTitle(APP_TITLE + " " + APP_VERSION);
        primaryStage.setScene(new Scene(root, width, height));
        //primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("../../../main/resources/icons/icon.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
