package com.kivimango.coffeecommander.model;

import com.kivimango.coffeecommander.view.dialog.CopyProgressDialog;
import com.kivimango.coffeecommander.view.dialog.DeleteDialog;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class WindowsFileSystemStrategy extends BaseModel implements FileSystemStrategy {

    DefaultWindowsFileFilter defaultFilter = new DefaultWindowsFileFilter();

    @Override
    public List<String> getDrives() {
        List<String> drives = new ArrayList<>();
        for(File f : File.listRoots()) {
            drives.add(f.getAbsolutePath());
        }
        return drives;
    }

    @Override
    public List<CoffeeFile> getDirectoryContent(Path path) throws IOException {
        if(!directoryContent.isEmpty()) {
            directoryContent.clear();
        }

        /*
        TO-DO : here we asking the file attributes twice, once in teh filter filter accept method, second
        in the loop
        */

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, defaultFilter)) {
            for (Path entry: stream) {
                DosFileAttributes attr = Files.readAttributes(path, DosFileAttributes.class, LinkOption.NOFOLLOW_LINKS);

                directoryContent.add(new CoffeeFile(iconConverter.convert(entry.toFile()),
                        entry.getFileName().toString(), attr.size(),
                        simpleDate.format(attr.lastModifiedTime().toMillis()),
                        entry.toAbsolutePath().toString(),
                       ""));
            }
        }
        return directoryContent;
    }

    @Override
    public void copy(CopyProgressDialog dialog, Path target, ObservableList<CoffeeFile> selectedItems, boolean overwrite, boolean preserve) throws IOException {
        super.copy(dialog, target, selectedItems, overwrite, preserve);
    }

    @Override
    public void delete(DeleteDialog dialog, ObservableList<CoffeeFile> target) throws IOException {
        super.delete(dialog, target);
    }

    @Override
    public void createNewDirectory(Path path, String name) throws IOException {
        path = Paths.get(path + File.separator + name);
        Files.createDirectory(path);
    }

    @Override
    public void openFileWithAssociatedProgram(File fileToOpen) throws IOException {
        if(Desktop.isDesktopSupported()) {
            desktop.open(fileToOpen);
        }
    }
}
