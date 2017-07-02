package com.kivimango.coffeecommander.model;

import com.kivimango.coffeecommander.view.dialog.CopyProgressDialog;
import com.kivimango.coffeecommander.view.dialog.DeleteDialog;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileSystemStrategy {
    List<String> getDrives();
    List<CoffeeFile> getDirectoryContent(Path path) throws IOException;
    void createNewDirectory(Path path, String name) throws IOException;
    void copy(CopyProgressDialog dialog, Path target, ObservableList<CoffeeFile> selectedItems, boolean overwrite, boolean preserve) throws IOException;
    void delete(DeleteDialog dialog, ObservableList<CoffeeFile> selectedItems) throws IOException;
    void openFileWithAssociatedProgram(File fileToOpen) throws IOException;
}
