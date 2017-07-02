package com.kivimango.coffeecommander.controllers;

import com.kivimango.coffeecommander.model.CoffeeFile;
import com.kivimango.coffeecommander.model.FileSystemStrategy;
import com.kivimango.coffeecommander.view.dialog.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
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
        TextInputDialog dialog = Alerts.initCreateDirDialog();
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
            name = name.trim();
            model.createNewDirectory(path, name);
            Path newPath = Paths.get( path + File.separator + name);
            table.requestFocus();
            if (Files.exists(newPath)) {
                Alert alert = Alerts.showSuccessDialog("New directory ( " + name +" ) created!");
                alert.showAndWait();
                main.refreshTable(table, path);
                System.out.print(newPath.toString());
            } else {
                Alert alert = Alerts.showAlertDialog("Cannot create directory !");
                alert.showAndWait();
            }
        } catch(IOException e){
            Alert alert = Alerts.showAlertDialog(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    /**
     * Showing a dialog with two labels and two text inputs with the values of the source and destination
     * directories paths and a checkbox.
     */

    @FXML
    private void handleCopyEvent(MouseEvent event) {
        Path source;
        Path target;
        ObservableList<CoffeeFile> items = FXCollections.observableArrayList();

        // Deciding the source and destination paths based on the selected table
        if(main.getLastFocusedTable() == 0) {
            source = main.getLeftPath();
            target = main.getRightPath();
            items = main.getLeftTable().getSelectionModel().getSelectedItems();
        } else {
            source = main.getRightPath();
            target = main.getLeftPath();
            items = main.getRightTable().getSelectionModel().getSelectedItems();
        }

        final ObservableList<CoffeeFile> selectedItems = items;

        int selectedItemsCount = selectedItems.size();
        if(selectedItemsCount < 1) {
            Alert alert = Alerts.showAlertDialog("No item(s) selected");
            alert.showAndWait();
            return;
        }

        // Constructing a dialog
        CopyDialog copyDialog = new CopyDialog(source.toString(), target.toString(), selectedItemsCount);
        Optional<CopyDialogResult> result = copyDialog.showAndWait();
        result.ifPresent((CopyDialogResult dialogResult) -> {
            // Constructing a new dialog to show the progress of the copying.
            CopyProgressDialog progressDialog = new CopyProgressDialog(source.toString(), target.toString(), selectedItemsCount);
            progressDialog.show();

            final int itemCount = selectedItems.size();
            boolean shouldOverWrite = dialogResult.shouldOverwrite();
            boolean shouldPreserve = dialogResult.shouldKeepOriginalFileDates();
            try {
                model.copy(progressDialog, target, selectedItems, shouldOverWrite, shouldPreserve);
                //}
            // user didn't checked overwrite checkbox in the CopyDialog,
            // but there is a file already in the target path.Asking the user with a new dialog.
            // NOT WORKING, FIX IT

            /*
            catch (FileAlreadyExistsException fae) {
                            Alert faeAlert = new Alert(Alert.AlertType.CONFIRMATION);
                            faeAlert.setTitle("File already exists");
                            faeAlert.setHeaderText("A file (...) already exists");
                            faeAlert.setContentText("What do you want to do with it?");

                            ButtonType overwriteButton = new ButtonType("Overwrite", ButtonBar.ButtonData.YES);
                            ButtonType skipButton = new ButtonType("Skip", ButtonBar.ButtonData.NO);
                            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                            faeAlert.getButtonTypes().setAll(overwriteButton, skipButton, cancelButton);
                            Optional<ButtonType> askingResult = faeAlert.showAndWait();
                            if(askingResult.isPresent()) {
                                // user chose overwrite
                                if (askingResult.get() == overwriteButton){
                                    try {
                                        model.copy(source, target, selectedItems, true, shouldPreserve,
                                                progressDialog.getTotalProgressBar(), progressDialog.getTargetItemLabel());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        showAlertDialog(e.getMessage());
                                    }
                                }
                                // user chose skip
                                else if (askingResult.get() == skipButton) {
                                    continue;
                                }
                                // user chose CANCEL or closed the dialog
                                else {
                                    break;
                                }
                            }
                            */
                        } catch (IOException e) {
                            e.printStackTrace();
                            //showAlertDialog("Unable to copy (" + f.getName() + ") !");
                Alert alert = Alerts.showAlertDialog("Unable to copy !");
                alert.showAndWait();
                        }
            refreshTable();
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
            Alert alert = Alerts.showAlertDialog("No item(s) selected");
            alert.showAndWait();
            return;
        }

        // Show the confirmation dialog
        Alert dialog = Alerts.initConfirmDeleteDialog();
        Optional<ButtonType> choice = dialog.showAndWait();

        // Deleting the selected items
        if(choice.isPresent() && choice.get() == ButtonType.OK) {
            doDelete(selectedItems);
        }
        refreshTable();
    }

    private void doDelete(ObservableList<CoffeeFile> selectedItems) {
        DeleteDialog deleteDialog = new DeleteDialog(selectedItems.size());
        deleteDialog.show();
        try {
            model.delete(deleteDialog, selectedItems);
        }
        catch (AccessDeniedException e) {
            // Unfortunately there is no way to make writable a file/directory if its read-only
            Alert alert = Alerts.showAlertDialog("Could not delete ! " + "File is read-only !", e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = Alerts.showAlertDialog(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     *  Refreshing the appropriate table after a task is completed (e.g.: copy, delete)
     */

    private void refreshTable() {
        if(main.getLastFocusedTable() == 0) {
            main.refreshTable(main.getLeftTable(), main.getLeftPath());
        } else {
            main.refreshTable(main.getRightTable(), main.getRightPath());
        }
    }

    void injectMain(MainController main) {
        this.main = main;
    }

    void setStrategy(FileSystemStrategy strategy) {
        this.model = strategy;
    }
}
