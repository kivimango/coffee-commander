package com.kivimango.coffeecommander.controllers;

import com.kivimango.coffeecommander.model.CoffeeFile;
import com.kivimango.coffeecommander.model.FileSystemDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DirectoryBrowserController implements Initializable{

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

    private FileSystemDAO model = new FileSystemDAO();

    public DirectoryBrowserController() {
    }

    @FXML
    public void handleKeyInput(KeyEvent keyEvent) {
        /* TO-DO : fix this !
        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.ESCAPE)
        {
           exitFromApplication();
        }
        */
    }

    public void exitFromApplication() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftIconCol.setCellValueFactory(new PropertyValueFactory<>("icon"));
        leftIconCol.setCellFactory(column -> {
            return new TableCell<CoffeeFile, Image>() {
                @Override
                protected void updateItem(Image image, boolean empty) {
                    super.updateItem(image, empty);
                    if(image!=null) {
                        ImageView imageview = new ImageView();
                        imageview.setImage(image);
                        setGraphic(imageview);
                    }
                }
            };
        });
        leftFileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        leftSizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        leftDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        rightIconCol.setCellFactory(column -> {
            return new TableCell<CoffeeFile, Image>() {
                @Override
                protected void updateItem(Image image, boolean empty) {
                    super.updateItem(image, empty);
                    if(image!=null) {
                        ImageView imageview = new ImageView();
                        imageview.setImage(image);
                        setGraphic(imageview);
                    }
                }
            };
        });
        rightIconCol.setCellValueFactory(new PropertyValueFactory<>("icon"));
        rightFileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rightSizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        rightDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        leftTable.getItems().setAll(model.getDirectoryContent(new File("/")));
        rightTable.getItems().setAll(model.getDirectoryContent(new File("/")));

        List<String> drives = model.getDrives();
        leftDriveList.getItems().setAll((drives));
        rightDriveList.getItems().setAll(drives);
    }

    public void handleMouseClickOnLeftTable(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            CoffeeFile selectedRow = leftTable.getSelectionModel().getSelectedItem();
            File selectedFile = new File(selectedRow.getPath());
            if(selectedFile.isDirectory()) {
                refreshTable(leftTable, selectedFile.getAbsolutePath());
            } else {
                try{
                    model.openFileWithAssociatedProgram(selectedFile);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Cannot run file!");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getLocalizedMessage());
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Loading the files and folders of the selected drive's root folder, than refreshing the table with the result.
     * @param event
     * @version 1.0
     * @since 0.1
     */

    @FXML
    public void handleLeftComboBoxChangeEvent(ActionEvent event) {
        String selectedDrive = leftDriveList.getValue();
       refreshTable(leftTable, selectedDrive);
    }

    @FXML
    public void handleRightComboBoxChangeEvent(ActionEvent event) {
        String selectedDrive = rightDriveList.getValue();
        refreshTable(rightTable, selectedDrive);
    }

    /**
     * Method to avoid code duplication: refreshing the table that passed as a parameter
     * @param table Table to refresh e.g.: lefTable or rightTable
     * @param path The path of the selected drive's root folder
     */

    private void refreshTable( TableView<CoffeeFile> table, String path) {
        table.getItems().setAll(model.getDirectoryContent(new File(path)));
        table.refresh();
    }
}
