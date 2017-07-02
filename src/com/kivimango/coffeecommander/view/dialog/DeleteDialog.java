package com.kivimango.coffeecommander.view.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

public class DeleteDialog extends Dialog {

    private final int count;
    private Label fileName = new Label();
    private ProgressBar deleteProgressBar;

    public DeleteDialog(int count) {
        this.count = count;

        setTitle("Deleting items");
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        deleteProgressBar = new ProgressBar(count);
        deleteProgressBar.setMinWidth(300);
        deleteProgressBar.setMinHeight(25);

        grid.add(new Label("Deleting"), 0, 0);
        grid.add(fileName, 1, 0);
        grid.add(deleteProgressBar, 0, 1, 2, 1);

        getDialogPane().setContent(grid);
    }

    public ProgressBar getProgressBar() {
        return deleteProgressBar;
    }

    public Label getFileNameLabel() {
        return fileName;
    }
}
