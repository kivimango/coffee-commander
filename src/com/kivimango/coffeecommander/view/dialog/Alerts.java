package com.kivimango.coffeecommander.view.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 * Creating different alerts.
 *
 * @author kivimango
 * @version 1.0
 * @since 0.1
 */

public class Alerts {

    public static Alert showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success !");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
    }

    public static Alert initOverwriteDialog(String existingFile) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("File already exists");
        alert.setHeaderText("File already exists.\n What dou you want to do ?");
        alert.setContentText(existingFile);
        ButtonType overWriteButton = new ButtonType("Overwrite", ButtonBar.ButtonData.YES);
        ButtonType skipButton = new ButtonType("Skip", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(overWriteButton, skipButton, cancelButton);
        return alert;
    }

    public static Alert initConfirmDeleteDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete");
        alert.setHeaderText("Are you really want to delete the selected item(s) ?");
        return alert;
    }

    public static TextInputDialog initCreateDirDialog() {
        TextInputDialog createDirDialog = new TextInputDialog("New directory");
        createDirDialog.setTitle("Create new directory");
        createDirDialog.setHeaderText(null);
        createDirDialog.setContentText("Name of the directory:");
        return createDirDialog;
    }

    public static Alert showAlertDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert;
    }

    public static Alert showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
    }
}
