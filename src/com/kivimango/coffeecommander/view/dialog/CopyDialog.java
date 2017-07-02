package com.kivimango.coffeecommander.view.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CopyDialog extends Dialog<CopyDialogResult> {

    private CheckBox overwriteOption = new CheckBox("Overwrite existing");
    private CheckBox preserveOption = new CheckBox("Keep original file dates");

    public CopyDialog(String sourceDir, String targetDir, int itemCount) {
        setTitle("Copy files/directories");
        setHeaderText( itemCount +" item(s) selected");
        ButtonType okButton = new ButtonType("Copy", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Label sourceDirInput = new Label(sourceDir);
        Label destinationDirInput = new Label(targetDir);

        grid.add(new Label("Source directory:"), 0, 0);
        grid.add(sourceDirInput, 1, 0);
        grid.add(new Label("Destination directory:"),0, 1);
        grid.add(destinationDirInput, 1, 1);
        overwriteOption.setSelected(true);
        grid.add(overwriteOption, 0, 2);
        preserveOption.setSelected(true);
        grid.add(preserveOption, 0, 3);
        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if(dialogButton == okButton) {
                return new CopyDialogResult(overwriteOption.isSelected(), preserveOption.isSelected());
            }
            return null;
        });
    }
}