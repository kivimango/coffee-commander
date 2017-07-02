package com.kivimango.coffeecommander.controllers;

import com.kivimango.coffeecommander.model.CoffeeFile;
import com.kivimango.coffeecommander.model.FileSystemStrategy;
import com.kivimango.coffeecommander.model.LinuxFileSystemStrategy;
import com.kivimango.coffeecommander.model.WindowsFileSystemStrategy;
import com.sun.javafx.PlatformUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Wrapper for all of the other controllers.
 * Supporting communications between other controllers.
 *
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public class MainController implements Initializable {

    /** Reference of the other controllers injected by the FXMLLoader */

    @FXML private MenubarController menubarController;
    @FXML private DirectoryBrowserController directoryBrowserController;
    @FXML private BottomToolbarController bottomToolbarController;

    /**
     * Injecting the main controller's reference to the sub controllers.
     * All of the controllers must have an injectMain() method to able to set the reference.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menubarController.injectMain(this);
        directoryBrowserController.setStrategy(getStrategyBasedOnHostSystem());
        directoryBrowserController.injectMain(this);
        directoryBrowserController.init();
        bottomToolbarController.injectMain(this);
        bottomToolbarController.setStrategy(getStrategyBasedOnHostSystem());
    }

    private FileSystemStrategy getStrategyBasedOnHostSystem() {
        if(PlatformUtil.isWindows()) {
            return new WindowsFileSystemStrategy();
        } else if(PlatformUtil.isLinux()) {
            return new LinuxFileSystemStrategy();
        } else return null;
    }

    /** Getters */

    /**
     * Sharing the DirectoryBrowser's left TableView component between other controllers.
     * @return the leftTable's reference
     */

    TableView<CoffeeFile> getLeftTable() {
        return directoryBrowserController.getLeftTable();
    }

    /**
     * Sharing the DirectoryBrowser's right TableView component between other controllers.
     * @return the rightTable's reference
     */

    TableView<CoffeeFile> getRightTable() {
        return directoryBrowserController.getRightTable();
    }

    /**
     * Sharing the DirectoryBrowser's left Label component's value between other controllers.
     * @return the leftCurrentWorkingDirectory's value;
     */

    Path getLeftPath() {
        return  directoryBrowserController.getLeftPath();
    }

    /**
     * Sharing the DirectoryBrowser's right Label component's value between other controllers.
     * @return the rightCurrentWorkingDirectory's value;
     */

    Path getRightPath() {
        return  directoryBrowserController.getRightPath();
    }

    /**
     * Sharing the DirectoryBrowser's lastFocused properties value between other controllers.
     * @return 0: leftTable, 1: rightTable is focused the last time user clicked on them.;
     */

    short getLastFocusedTable() {
        return directoryBrowserController.getLastFocused();
    }

    /**
     * Method to allow the other controllers to able refresh a table's content after some operation.
     */

    void refreshTable(TableView<CoffeeFile> tableToRefresh, Path directoryToUpdate) {
        directoryBrowserController.refreshTable(tableToRefresh, directoryToUpdate);
    }
}