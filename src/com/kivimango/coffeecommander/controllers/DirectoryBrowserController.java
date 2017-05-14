package com.kivimango.coffeecommander.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DirectoryBrowserController {
    @FXML
    public Label helloWorld;

    public void sayHello(ActionEvent actionEvent) {
        helloWorld.setText("Hello World !");
    }
}
