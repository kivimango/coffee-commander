package com.kivimango.coffeecommander.controllers;

import com.kivimango.coffeecommander.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;

/**
 * Controller for handling user interactions coming from the MenuBar menu items.
 *
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public class MenubarController {

    private MainController mainController;

    @FXML private MenuBar menuBar;

    public void injectMain(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void exitFromApplication() {
        Platform.exit();
    }

    @FXML
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About " + Main.APP_TITLE);
        alert.setHeaderText(Main.APP_TITLE + " is a free, platform-independent file manager application.\n" +
                "Author: kivimango\n" +
                "Version: " + Main.APP_VERSION);
        alert.setContentText("Thank you for using this software.\n" +
                "Please report bugs and issues on the following site: \n" +
                "https://github.com/kivimango/coffee-commander/issues");
        alert.showAndWait();
    }
}
