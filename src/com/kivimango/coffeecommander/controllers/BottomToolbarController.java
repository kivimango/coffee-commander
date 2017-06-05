package com.kivimango.coffeecommander.controllers;

import com.kivimango.coffeecommander.model.CoffeeFile;
import com.kivimango.coffeecommander.model.FileSystemStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Controller for handling user interactions coming from the bottom toolbar's buttons.
 *
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public class BottomToolbarController {

    private MainController main;
    private FileSystemStrategy model;
    @FXML Button createDirButton;
    @FXML Button copyButton;
    @FXML Button deleteButton;

    /**
     * Showing a TextInputDialog, waiting for user to enter the directory's name to create.
     * If input entered, calling the createDir method to perform operation based on the selected table.
     */

    @FXML
    private void handleCreateDirEvent(MouseEvent mouseEvent) {
        TextInputDialog dialog = initCreateDirDialog();
        Optional<String> dirName = dialog.showAndWait();
        if(dirName.isPresent()) {
            if(main.getLastFocusedTable() == 0) {
                createDir(main.getLeftTable(), main.getLeftPath(), dirName.get());
            } else {
                createDir(main.getRightTable(), main.getRightPath(), dirName.get());
            }
        }
    }

    /**
     * Performing the directory creation operation.
     * Displaying a feedback dialog with appropriate message (success, fail).
     * @param table the table to update after success creation
     * @param path new directory's parent directory.
     * @param name of the new directory
     */

    private void createDir(TableView<CoffeeFile> table, Path path, String name) {
        try {
            model.createNewDirectory(path, name);
            Path newPath = Paths.get( main.getLeftPath() + File.separator + name);
            table.requestFocus();
            if (Files.exists(newPath)) {
                showSuccessDialog("New directory ( " + name +" ) created!");
                main.refreshTable(table, path);
                System.out.print(path.toString());
            } else {
                showAlertDialog("Cannot create directory !");
            }
        } catch(IOException e){
            showAlertDialog(e.getLocalizedMessage());
        }
    }

    /**
     * Showing a dialog with two labels and two text inputs with the values of the source and destination
     * directories paths and a checkbox.
     */

    @FXML
    private void handleCopyEvent(MouseEvent event) {
        String leftValue;
        String rightValue;

        if(main.getLastFocusedTable() == 0) {
            leftValue = main.getLeftPath().toString();
            rightValue = main.getRightPath().toString();
        } else {
            leftValue = main.getRightPath().toString();
            rightValue = main.getLeftPath().toString();
        }

        ObservableList<CoffeeFile> selectedItems = FXCollections.observableArrayList();

        // Deciding the source and destination paths based on the selected table
        if(main.getLastFocusedTable() == 0) {
            selectedItems = main.getLeftTable().getSelectionModel().getSelectedItems();
        } else {
            selectedItems = main.getRightTable().getSelectionModel().getSelectedItems();
        }

        int selectedItemsCount = selectedItems.size();
        if(selectedItemsCount < 1) {
            showAlertDialog("No item(s) selected !");
            return;
        }

        // Constructing a dialog
        // TO-DO : export this part into a separate class
        Dialog<Pair<String, String >> dialog = new Dialog<>();
        dialog.setTitle("Copy files/directories");
        dialog.setHeaderText("Copying "+ selectedItemsCount +" item");

        ButtonType okButton = new ButtonType("Copy", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField sourceDirInput = new TextField();
        sourceDirInput.setText(leftValue);
        TextField destinationDirInput = new TextField();
        destinationDirInput.setText(rightValue);

        grid.add(new Label("Source directory:"), 0, 0);
        grid.add(sourceDirInput, 1, 0);
        grid.add(new Label("Destination directory:"),0, 1);
        grid.add(destinationDirInput, 1, 1);
        grid.add(new CheckBox("Overwrite existing"), 0, 2);

        sourceDirInput.textProperty().addListener((observable, oldValue, newValue) -> {
            sourceDirInput.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == okButton) {
                return new Pair<>(sourceDirInput.getText(), destinationDirInput.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        ObservableList<CoffeeFile> finalSelectedItems = selectedItems;
        ObservableList<CoffeeFile> finalSelectedItems1 = selectedItems;
        result.ifPresent(pathParams -> {
            System.out.println("source=" + pathParams.getKey() + ", destination=" + pathParams.getValue());

            // Constructing a new dialog to show the progress of the copying.
            // TO-DO:
            // - implement updating the progressbar with the current state of the copying.
            // - copy operation must be executed on a separate worker/service thread.
            Dialog progressDialog = new Dialog<>();

            progressDialog.setTitle("Copying...");
            progressDialog.setContentText("Copying...");

            progressDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

            GridPane grid2 = new GridPane();
            grid2.setHgap(10);
            grid2.setVgap(10);
            grid2.setPadding(new Insets(20, 150, 10, 10));

            Label label = new Label(pathParams.getKey());

            ProgressBar progressBar = new ProgressBar();

            grid2.add(label, 0, 0);
            grid2.add(progressBar, 0, 1);

            progressDialog.getDialogPane().setContent(grid2);
            progressDialog.show();

            // TO_DO : handle filealreadyexists exception, overwrite checkboc value, directory copy

            int i = 0;
            for(CoffeeFile f : finalSelectedItems) {
                label.setText(f.getName());
                Path sourcePath = Paths.get(pathParams.getKey() + File.separator + finalSelectedItems1.get(i).getName());
                Path destPath = Paths.get(pathParams.getValue() + File.separator + finalSelectedItems1.get(i).getName());
                System.out.print(sourcePath);
                System.out.println(destPath);
                try {
                    Files.copy(sourcePath, destPath);
                    double pr = ((selectedItemsCount * i+1) / 100);
                    progressBar.setProgress(pr);
                } catch (IOException e) {
                    e.printStackTrace();
                    //showAlertDialog(e.getMessage());
                    System.out.print(e.getMessage());
                }
                i++;
            }
            progressDialog.hide();
            // TO_DO : refresh destination table
        });
    }

    @FXML
    void handleDeleteEvent(MouseEvent mouseEvent) {
        ObservableList<CoffeeFile> selectedItems = FXCollections.observableArrayList();

        // Deciding which table is the source
        if(main.getLastFocusedTable() == 0) {
            selectedItems = main.getLeftTable().getSelectionModel().getSelectedItems();
        } else {
            selectedItems = main.getRightTable().getSelectionModel().getSelectedItems();
        }

        int selectedItemsCount = selectedItems.size();
        if(selectedItemsCount < 1) {
            showAlertDialog("No item(s) selected !");
            return;
        }

        // Show the confirmation dialog
        Alert dialog = initConfirmDirDialog();
        Optional<ButtonType> choice = dialog.showAndWait();

        // Deleting the selected items (files, directories containing files and subdirectories)
        if(choice.get() == ButtonType.OK) {
            for(CoffeeFile f : selectedItems) {
                try {
                    model.delete(Paths.get(f.getPath()));
                } catch (IOException e) {
                    showAlertDialog(e.getLocalizedMessage());
                }
            }

            // Refreshing the appropriate table after the selected items deleted
            if(main.getLastFocusedTable() == 0) {
                main.refreshTable(main.getLeftTable(), main.getLeftPath());
            } else {
                main.refreshTable(main.getRightTable(), main.getRightPath());
            }
        }
    }

    private Alert initConfirmDirDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete");
        alert.setHeaderText("Are you really want to delete this ?");
        return alert;
    }

    private TextInputDialog initCreateDirDialog() {
        TextInputDialog createDirDialog = new TextInputDialog("New directory");
        createDirDialog.setTitle("Create new directory");
        createDirDialog.setHeaderText(null);
        createDirDialog.setContentText("Name of the directory:");
        return createDirDialog;
    }

    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success !");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void injectMain(MainController main) {
        this.main = main;
    }

    void setStrategy(FileSystemStrategy strategy) {
        this.model = strategy;
    }
}
