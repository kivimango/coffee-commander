package com.kivimango.coffeecommander.view.dialog;

import javafx.scene.control.ProgressBar;

public class DeleteProgressDialog {
    private ProgressBar progressBar;

    public DeleteProgressDialog(int itemCount) {
        progressBar = new ProgressBar(itemCount);
        progressBar.setMinWidth(300);
        progressBar.setMinHeight(25);
    }
}
