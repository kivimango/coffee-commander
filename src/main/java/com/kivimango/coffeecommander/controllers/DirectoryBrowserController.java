package com.kivimango.coffeecommander.controllers;

import com.kivimango.coffeecommander.model.*;
import com.kivimango.coffeecommander.util.WindowsShortcutResolver;
import com.sun.javafx.PlatformUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller for handling user interactions with the two tables.
 *
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public class DirectoryBrowserController {

    private MainController mainController;
    private FileSystemStrategy model;
    private Path leftCurrentWorkingDirectory;
    private Path rightCurrentWorkingDirectory;

    @FXML
    private TableView<CoffeeFile> leftTable;

    @FXML
    private TableView<CoffeeFile> rightTable;

    @FXML
    private TableColumn<CoffeeFile, Image> leftIconCol;

    @FXML
    private TableColumn<CoffeeFile, String> leftFileNameCol;

    @FXML
    private TableColumn<CoffeeFile, Long> leftSizeCol;

    @FXML
    private TableColumn<CoffeeFile, Date> leftDateCol;

    @FXML
    private TableColumn leftPermsCol;

    @FXML
    private TableColumn<CoffeeFile, Image> rightIconCol;

    @FXML
    private TableColumn<CoffeeFile, String> rightFileNameCol;

    @FXML
    private TableColumn<CoffeeFile, Long> rightSizeCol;

    @FXML
    private TableColumn<CoffeeFile, Date> rightDateCol;

    @FXML
    private TableColumn rightPermsCol;

    @FXML
    private ComboBox<String> leftDriveList;

    @FXML
    private ComboBox<String> rightDriveList;

    @FXML
    private Label leftPathLabel;

    @FXML
    private Label rightPathLabel;

    @FXML
    private Button leftRefreshButton;

    @FXML
    private Button rightRefreshButton;

    @FXML
    private Button leftUpButton;

    @FXML
    private Button rightUpButton;

    /**
     *  variable indicating the last focused table 0 : leftTable, 1 : rightTable
     */

    private short lastFocused = 0;

    void setStrategy(FileSystemStrategy strategy) {
        this.model = strategy;
    }

    void init() {
        Path firstRoot = initDriveLists();
        initTables(firstRoot);
    }

    void injectMain(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleMouseClickOnLeftTable(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            CoffeeFile selectedRow = leftTable.getSelectionModel().getSelectedItem();
            if(selectedRow != null) {
                handleMouseClickOnTables(leftTable, selectedRow);
                leftPathLabel.setText(selectedRow.getPath());
                leftCurrentWorkingDirectory = Paths.get(selectedRow.getPath());
            }
        }
        else {
            rememberLastFocusedTable();
        }
    }

    private void rememberLastFocusedTable() {
        if(!rightTable.isFocused()) {
            lastFocused = 0;
        } else if(!leftTable.isFocused()){
            lastFocused = 1;
        }
    }

    @FXML
    private void handleMouseClickOnRightTable(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            CoffeeFile selectedRow = rightTable.getSelectionModel().getSelectedItem();
            if(selectedRow != null) {
                handleMouseClickOnTables(rightTable, selectedRow);
                rightPathLabel.setText(selectedRow.getPath());
                rightCurrentWorkingDirectory = Paths.get(selectedRow.getPath());
            }
        }
        else {
            rememberLastFocusedTable();
        }
    }

    private void handleMouseClickOnTables(TableView<CoffeeFile> tableToUpdate, CoffeeFile selectedRow) {
        Path selectedFile = Paths.get(selectedRow.getPath());
        if(Files.isDirectory(selectedFile)) {
            refreshTable(tableToUpdate, selectedFile);
        } else if((PlatformUtil.isWindows() || PlatformUtil.isWin7OrLater()) && selectedRow.getName().endsWith(".lnk")) {
            parseWindowsLnk(selectedFile.toFile());
        } else try {
            if(PlatformUtil.isWindows()) {
                model.openFileWithAssociatedProgram(selectedFile.toFile());
            }
        } catch (IOException e) {
            showAlertDialog(e.getLocalizedMessage());
        }
    }

    private void parseWindowsLnk(File selectedFile) {
            try {
                if(WindowsShortcutResolver.isPotentialValidLink(selectedFile)) {
                    try {
                        WindowsShortcutResolver lr = new WindowsShortcutResolver(selectedFile);
                        File f = new File(lr.getRealFilename());
                        refreshTable(leftTable, f.toPath());
                    } catch (IOException e) {
                       showAlertDialog(e.getLocalizedMessage());
                    }
                }
            } catch (ParseException | IOException e1) {
                showAlertDialog(e1.getLocalizedMessage());
            }
    }

    /**
     * Loading the files and folders of the selected drive's root folder,
     * than refreshing the table with the result.
     * @param event
     * @version 1.0
     * @since 0.1
     */

    @FXML
    private void handleLeftComboBoxChangeEvent(ActionEvent event) {
    String selectedDrive = leftDriveList.getSelectionModel().getSelectedItem();
        leftCurrentWorkingDirectory = Paths.get(selectedDrive);
        refreshTable(leftTable, leftCurrentWorkingDirectory);
        leftPathLabel.setText(selectedDrive);
    }

    @FXML
    public void handleRightComboBoxChangeEvent(ActionEvent event) {
        String selectedDrive = rightDriveList.getValue();
        rightCurrentWorkingDirectory = Paths.get(selectedDrive);
        refreshTable(rightTable, rightCurrentWorkingDirectory);
        rightPathLabel.setText(selectedDrive);
    }

    @FXML
    public void handleLeftRefreshButtonEvent(MouseEvent mouseEvent) {
        refreshTable(leftTable, leftCurrentWorkingDirectory);
    }

    @FXML
    public void handleRightRefreshButtonEvent(MouseEvent mouseEvent) {
        refreshTable(rightTable, rightCurrentWorkingDirectory);
    }

    /**
     * Method to avoid code duplication: refreshing the table that passed as a parameter
     * @param table Table to refresh e.g.: lefTable or rightTable
     * @param path The path of the selected drive's root folder
     */

    void refreshTable(TableView<CoffeeFile> table, Path path) {
        List<CoffeeFile> content = new ArrayList<>();
        try {
            content = model.getDirectoryContent(path);
        } catch (IOException e) {
            showAlertDialog(e.getMessage());
        }
        table.getItems().setAll(content);
        table.refresh();
    }

    @FXML
    public void handleLeftUpButtonEvent(MouseEvent mouseEvent) {
        Path parent = leftCurrentWorkingDirectory.getParent();
        if(parent != null) {
            leftCurrentWorkingDirectory = parent;
            refreshTable(leftTable, parent);
        }
        else {
            leftCurrentWorkingDirectory = Paths.get("/");
            refreshTable(leftTable, leftCurrentWorkingDirectory);
        }
        leftPathLabel.setText(leftCurrentWorkingDirectory.toString());
    }

    @FXML
    public void handleRightUpButtonEvent(MouseEvent mouseEvent) {
        Path parent = rightCurrentWorkingDirectory.getParent();
        if(parent != null) {
            rightCurrentWorkingDirectory = parent;
            refreshTable(rightTable, parent);
        }
        else {
            rightCurrentWorkingDirectory = Paths.get("/");
            refreshTable(rightTable, rightCurrentWorkingDirectory);
        }
        rightPathLabel.setText(rightCurrentWorkingDirectory.toString());
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

    private Path initDriveLists() {
        List<String> drives = model.getDrives();
        leftDriveList.getItems().setAll((drives));
        leftDriveList.getSelectionModel().selectFirst();
        rightDriveList.getItems().setAll(drives);
        rightDriveList.getSelectionModel().selectFirst();

        Path firstRoot = Paths.get(drives.get(0));
        leftCurrentWorkingDirectory = Paths.get(drives.get(0));
        rightCurrentWorkingDirectory = Paths.get(drives.get(0));
        return firstRoot;
    }

    private void initTables(Path firstRoot) {
        // Setting cell renderer form displaying the icon
        leftIconCol.setCellFactory(column -> new TableCell<CoffeeFile, Image>() {
            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);
                if(image!=null) {
                    ImageView imageview = new ImageView();
                    imageview.setImage(image);
                    setGraphic(imageview);
                }
            }
        });
        rightIconCol.setCellFactory(column -> new TableCell<CoffeeFile, Image>() {
            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);
                if(image!=null) {
                    ImageView imageview = new ImageView();
                    imageview.setImage(image);
                    setGraphic(imageview);
                }
            }
        });
        // Getting the content of the first root
        List<CoffeeFile> content = new ArrayList<>();
        try {
            content = model.getDirectoryContent(firstRoot);
        } catch (IOException e) {
            showAlertDialog(e.getLocalizedMessage());
        }

        // Filling the tables with the content
        leftTable.getItems().setAll(content);
        rightTable.getItems().setAll(content);

        leftPathLabel.setText(firstRoot.toString());
        rightPathLabel.setText(firstRoot.toString());

        leftTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        rightTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        leftTable.requestFocus();
    }

    /** Getters */

    TableView<CoffeeFile> getLeftTable() {
        return leftTable;
    }

    TableView<CoffeeFile> getRightTable() {
        return rightTable;
    }

    short getLastFocused() {
        return lastFocused;
    }

    Path getLeftPath() {
        return leftCurrentWorkingDirectory;
    }

    Path getRightPath() {
        return rightCurrentWorkingDirectory;
    }
}
