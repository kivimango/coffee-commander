package com.kivimango.coffeecommander.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;

/**
 * @author kivimango
 * @version 1.0
 * @since 0.1
 */

public class SettingsController {

    public Button applyButton;
    public Button cancelButton;
    public BorderPane window;
    public CheckBox rememberLastPathCheckBox;

    public void applySettings(ActionEvent actionEvent) {
    }

    public void cancel(ActionEvent actionEvent) {
        window.getScene().getWindow().hide();
    }
}
