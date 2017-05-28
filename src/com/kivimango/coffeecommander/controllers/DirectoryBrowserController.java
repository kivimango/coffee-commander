package com.kivimango.coffeecommander.controllers;

import com.kivimango.coffeecommander.Main;
import com.kivimango.coffeecommander.model.*;
import com.kivimango.coffeecommander.util.WindowsShortcutResolver;
import com.sun.javafx.PlatformUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DirectoryBrowserController implements Initializable {

    @FXML
    public MenuBar menuBar;

    @FXML
    public TableView<CoffeeFile> leftTable;

    @FXML
    public TableView<CoffeeFile> rightTable;

    @FXML
    public TableColumn<CoffeeFile, Image> leftIconCol;

    @FXML
    public TableColumn<CoffeeFile, String> leftFileNameCol;

    @FXML
    public TableColumn<CoffeeFile, Long> leftSizeCol;

    @FXML
    public TableColumn<CoffeeFile, Date> leftDateCol;

    @FXML
    public TableColumn<CoffeeFile, Image> rightIconCol;

    @FXML
    public TableColumn<CoffeeFile, String> rightFileNameCol;

    @FXML
    public TableColumn<CoffeeFile, Long> rightSizeCol;

    @FXML
    public TableColumn<CoffeeFile, Date> rightDateCol;

    @FXML
    public ComboBox<String> leftDriveList;

    @FXML
    public ComboBox<String> rightDriveList;

    @FXML
    public Label leftPathLabel;

    @FXML
    public Label rightPathLabel;

    @FXML
    public Button leftRefreshButton;

    @FXML
    public Button rightRefreshButton;

    @FXML
    public Button leftUpButton;

    @FXML
    public Button rightUpButton;

    private FileSystemStrategy model;
    private File leftCurrentWorkingDirectory;
    private File rightCurrentWorkingDirectory;

   public DirectoryBrowserController(FileSystemStrategy strategy) {
        model = strategy;
    }

    @FXML
    public void exitFromApplication() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File firstRoot = initDriveLists();
        initTables(firstRoot);
    }

    @FXML
    public void handleMouseClickOnLeftTable(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            CoffeeFile selectedRow = leftTable.getSelectionModel().getSelectedItem();
            if(selectedRow != null) {
                handleMouseClickOnTables(leftTable, selectedRow);
                leftPathLabel.setText(selectedRow.getPath());
                leftCurrentWorkingDirectory = new File(selectedRow.getPath());
            }
        }
    }

    @FXML
    public void handleMouseClickOnRightTable(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            CoffeeFile selectedRow = rightTable.getSelectionModel().getSelectedItem();
            if(selectedRow != null) {
                handleMouseClickOnTables(rightTable, selectedRow);
                rightPathLabel.setText(selectedRow.getPath());
                rightCurrentWorkingDirectory = new File(selectedRow.getPath());
            }
        }
    }

    private void handleMouseClickOnTables(TableView<CoffeeFile> tableToUpdate, CoffeeFile selectedRow) {
        File selectedFile = new File(selectedRow.getPath());
        if(selectedFile.isDirectory()) {
            refreshTable(tableToUpdate, selectedFile.getAbsolutePath());
        } else if((PlatformUtil.isWindows() || PlatformUtil.isWin7OrLater()) && selectedRow.getName().endsWith(".lnk")) {
            parseWindowsLnk(selectedFile);
        } else try {
            if(PlatformUtil.isWindows()) {
                model.openFileWithAssociatedProgram(selectedFile);
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
                        refreshTable(leftTable, lr.getRealFilename());
                    } catch (IOException e) {
                       showAlertDialog(e.getLocalizedMessage());
                    }
                }
            } catch (ParseException e1) {
                showAlertDialog(e1.getLocalizedMessage());
            } catch (IOException e2) {
                showAlertDialog(e2.getLocalizedMessage());
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
    public void handleLeftComboBoxChangeEvent(ActionEvent event) {
        String selectedDrive = leftDriveList.getSelectionModel().getSelectedItem();
        leftCurrentWorkingDirectory = new File(selectedDrive);
        refreshTable(leftTable, selectedDrive);
        leftPathLabel.setText(selectedDrive);
    }

    @FXML
    public void handleRightComboBoxChangeEvent(ActionEvent event) {
        String selectedDrive = rightDriveList.getValue();
        rightCurrentWorkingDirectory = new File(selectedDrive);
        refreshTable(rightTable, selectedDrive);
        rightPathLabel.setText(selectedDrive);
    }

    @FXML
    public void handleLeftRefreshButtonEvent(MouseEvent mouseEvent) {
        refreshTable(leftTable, leftCurrentWorkingDirectory.getAbsolutePath());
    }

    @FXML
    public void handleRightRefreshButtonEvent(MouseEvent mouseEvent) {
        refreshTable(rightTable, rightCurrentWorkingDirectory.getAbsolutePath());
    }

    /**
     * Method to avoid code duplication: refreshing the table that passed as a parameter
     * @param table Table to refresh e.g.: lefTable or rightTable
     * @param path The path of the selected drive's root folder
     */

    private void refreshTable( TableView<CoffeeFile> table, String path) {
        List<CoffeeFile> content = new ArrayList<>();
        try {
            content = model.getDirectoryContent(new File(path));
        } catch (IOException e) {
            showAlertDialog(e.getMessage());
        }
        table.getItems().setAll(content);
        table.refresh();
    }

    @FXML
    public void handleLeftUpButtonEvent(MouseEvent mouseEvent) {
        String parent = getParent(leftCurrentWorkingDirectory);
        if(parent != null) {
            leftCurrentWorkingDirectory = new File(parent);
            refreshTable(leftTable, parent);
        }
        else {
            leftCurrentWorkingDirectory = new File("/");
            refreshTable(leftTable, leftCurrentWorkingDirectory.getPath());
        }
        leftPathLabel.setText(leftCurrentWorkingDirectory.getPath());
    }

    @FXML
    public void handleRightUpButtonEvent(MouseEvent mouseEvent) {
        String parent = getParent(rightCurrentWorkingDirectory);
        if(parent != null) {
            rightCurrentWorkingDirectory = new File(parent);
            refreshTable(rightTable, parent);
        }
        else {
            rightCurrentWorkingDirectory = new File("/");
            refreshTable(rightTable, rightCurrentWorkingDirectory.getPath());
        }
        rightPathLabel.setText(rightCurrentWorkingDirectory.getPath());
    }

    private String getParent(File children) {
        String path = children.getAbsoluteFile().getParent();
        // getParent() can return null if the File has no parent
        if(path != null) {
            //System.out.println(path.substring(path.lastIndexOf("\\")+1,path.length()));
            return path;
            // no parent, because you are already on a root folder.
        } else return null;
    }

    private void showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cannot run file!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About " + Main.APP_TITLE);
        alert.setHeaderText(Main.APP_TITLE + " is a free, platform-independent file manager application.\n" +
                "Author: kivimango\n" +
                "Version: " + Main.APP_VERSION);
        alert.setContentText("Thank you for using this software.\n" +
        "Please report bugs and issues on the following site: \n" +
                "https://github.com/kivimango/coffee-commander/issues");
        alert.showAndWait();
    }

    private File initDriveLists() {
        List<String> drives = model.getDrives();
        leftDriveList.getItems().setAll((drives));
        leftDriveList.getSelectionModel().selectFirst();
        rightDriveList.getItems().setAll(drives);
        rightDriveList.getSelectionModel().selectFirst();

        File firstRoot = new File(drives.get(0));
        leftCurrentWorkingDirectory = firstRoot;
        rightCurrentWorkingDirectory = firstRoot;
        return firstRoot;
    }

    private void initTables(File firstRoot) {
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

        leftPathLabel.setText(firstRoot.getAbsolutePath());
        rightPathLabel.setText(firstRoot.getAbsolutePath());
    }
}
