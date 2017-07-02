package com.kivimango.coffeecommander.view.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

public class CopyProgressDialog extends Dialog {

    private Label sourceItemLabel = new Label();
    private Label targetItemLabel = new Label();
    private ProgressBar totalProgressBar;
    private ProgressBar partProgressBar = new ProgressBar();

    public CopyProgressDialog(String sourcePath, String destPath, int itemCount) {
        setTitle("Copying...");
        setContentText("Copying...");
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        GridPane grid2 = new GridPane();
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(20, 150, 10, 10));

        sourceItemLabel.setText(sourcePath);
        targetItemLabel.setText(destPath);

        totalProgressBar = new ProgressBar(itemCount);
        totalProgressBar.setMinWidth(300);
        totalProgressBar.setMinHeight(25);
        // partProgressBar.setMinWidth(300);
        // partProgressBar.setMinHeight(25);

        // See GridPane colspan / rowspan tutorial
        grid2.add(new Label("From:"), 0, 0);
        grid2.add(sourceItemLabel, 1, 0);
        grid2.add( new Label("To:"), 0,1);
        grid2.add(targetItemLabel, 1, 1);
        grid2.add(totalProgressBar, 0, 2, 2, 1);
        //grid2.add(partProgressBar, 0, 3);

        getDialogPane().setContent(grid2);
    }

    public ProgressBar getTotalProgressBar() {
        return totalProgressBar;
    }

    public ProgressBar getPartProgressBar() {
        return partProgressBar;
    }

    public Label getSourceLabelText() {
        return  sourceItemLabel;
    }

    public Label getTargetItemLabel() {
        return  targetItemLabel;
    }
}
