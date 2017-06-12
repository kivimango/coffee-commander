package com.kivimango.coffeecommander.controllers;

import com.kivimango.coffeecommander.Main;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

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
    
    private final String GITHUB_ISSUES_LINK = "https://github.com/kivimango/coffee-commander/issues";

    private Stage stage;

    void injectMain(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void exitFromApplication() {
        Platform.exit();
    }

    /**
     * Showing a customized dialog with information about the application.
     * The dialog has a custom body with a clickable hyperlink to the github repo's Issues page.
     *
     * Note: however that this will introduce a dependency to the AWT stack. This is probably not an issue if you're
     * working with the full JRE, but it might become an issue if you want to work with a tailored JRE
     * (Java SE 9 & Jigsaw) or if you want to run your application on mobile device (javafxports).
     * There is an open issue to <a href="https://bugs.openjdk.java.net/browse/JDK-8091107">support Desktop</a>
     * in JavaFX in the future.
     *
     * @author kivimango
     * @since 0.1
     * @version 2.0
     */

    @FXML
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Replacing the default dialog icon with the application icon
        Image image1 = new Image(Main.class.getResourceAsStream("../../../main/resources/icons/icon.png"));
        ImageView imageView = new ImageView(image1);
        alert.setGraphic(imageView);

        alert.setTitle("About " + Main.APP_TITLE);
        alert.setHeaderText(Main.APP_TITLE + " is a free, platform-independent file manager application.\n" +
                "Author: kivimango\n" +
                "Version: " + Main.APP_VERSION);

        // Making a custom dialog body with a clickable hyperlink
        FlowPane fp = new FlowPane();
        Label lbl = new Label("Thank you for using this software.\n" +
                "Please report bugs and issues on the following site:");
        Hyperlink link = new Hyperlink(GITHUB_ISSUES_LINK);

        //Clicking on the hyperlink the host OS will open a new browser
        link.setOnAction( (ActionEvent evt) -> {
            alert.close();
            HostServices hostServices = (HostServices)this.getStage().getProperties().get("hostServices");
            hostServices.showDocument(GITHUB_ISSUES_LINK);
        } );

        fp.getChildren().addAll(lbl, link);
        alert.getDialogPane().contentProperty().set(fp);
        alert.showAndWait();
    }

    private Stage getStage() {
        if(this.stage==null)
            this.stage = (Stage) this.menuBar.getScene().getWindow();
        return stage;
    }
}
