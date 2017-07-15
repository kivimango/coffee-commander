package com.kivimango.coffeecommander.model;

import com.kivimango.coffeecommander.model.tasks.CopyTask;
import com.kivimango.coffeecommander.model.tasks.DeleteTask;
import com.kivimango.coffeecommander.util.FileIconConverter;
import com.kivimango.coffeecommander.view.dialog.Alerts;
import com.kivimango.coffeecommander.view.dialog.CopyProgressDialog;
import com.kivimango.coffeecommander.view.dialog.DeleteDialog;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of basic file system operations which are cross-platform.
 *
 * @author kivimango
 * @version 1.0
 * @since 0.1
 */

public class BaseModel {

    // Protected fields used by children classes.Do not remove them.
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    FileIconConverter iconConverter = new FileIconConverter();
    Desktop desktop = Desktop.getDesktop();

    /**
     * Comparator class to sort file list by isDir property
     */

    FileComparator fileComparator = new FileComparator();

    protected void copy(CopyProgressDialog dialog, Path target, ObservableList<CoffeeFile> selectedItems, boolean overwrite,
                         boolean preserve) throws IOException {
        Task copyTask = new CopyTask(target, selectedItems, overwrite, preserve);
        copyTask.setOnSucceeded(event -> {
                    Alert alert = Alerts.showAlertDialog("Done!");
                    alert.showAndWait();
                    //dialog.close();
                }
        );

        // Binding task progress update to GUI components
        dialog.getTargetItemLabel().textProperty().unbind();
        dialog.getTotalProgressBar().progressProperty().unbind();
        dialog.getTargetItemLabel().textProperty().bind(copyTask.messageProperty());
        dialog.getTotalProgressBar().progressProperty().bind(copyTask.progressProperty());

        new Thread(copyTask).start();
    }

    protected void delete(DeleteDialog dialog, ObservableList<CoffeeFile> selectedItems) throws IOException {
        Task deleteTask = new DeleteTask(selectedItems);
        deleteTask.setOnSucceeded(event -> {
            Alert alert = Alerts.showAlertDialog("Done!");
            alert.showAndWait();
            //dialog.close();
                }
        );

        // Binding task progress update to GUI components
        dialog.getProgressBar().progressProperty().unbind();
        dialog.getFileNameLabel().textProperty().unbind();
        dialog.getProgressBar().progressProperty().bind(deleteTask.progressProperty());
        dialog.getFileNameLabel().textProperty().bind(deleteTask.messageProperty());
        new Thread(deleteTask).start();
    }
}
