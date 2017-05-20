package com.kivimango.coffeecommander.controllers;

import com.kivimango.coffeecommander.model.CoffeeFile;
import com.kivimango.coffeecommander.model.FileSystemDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.File;
import java.net.URL;
import java.util.*;

public class DirectoryBrowserController implements Initializable{

    @FXML
    public BorderPane borderPane;

    @FXML
    public VBox leftPane;

    @FXML
    public VBox rightPane;

    @FXML
    public MenuBar menuBar;

    @FXML
    public TableView<CoffeeFile> leftTable;

    @FXML
    public TableView<CoffeeFile> rightTable;

    @FXML
    public TableColumn<CoffeeFile, String> leftFileNameCol;

    @FXML
    public TableColumn<CoffeeFile, Long> leftSizeCol;

    @FXML
    public TableColumn<CoffeeFile, Date> leftDateCol;
    public TableColumn<CoffeeFile, String> rightFileNameCol;
    public TableColumn<CoffeeFile, Long> rightSizeCol;
    public TableColumn<CoffeeFile, Date> rightDateCol;

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
        leftFileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        leftSizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        leftDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        rightFileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rightSizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        rightDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        leftTable.getItems().setAll(model.getDirectoryContent(new File("/")));
        rightTable.getItems().setAll(model.getDirectoryContent(new File("/")));
    }
}
