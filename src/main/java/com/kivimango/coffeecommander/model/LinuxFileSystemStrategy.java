package com.kivimango.coffeecommander.model;

import com.kivimango.coffeecommander.view.dialog.CopyProgressDialog;
import com.kivimango.coffeecommander.view.dialog.DeleteDialog;
import javafx.collections.ObservableList;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LinuxFileSystemStrategy extends BaseModel implements FileSystemStrategy {

    @Override
    public List<String> getDrives() {
        List<String> drives = new ArrayList<>();
        Iterable<Path> roots = FileSystems.getDefault().getRootDirectories();
        for (Path name: roots) {
            drives.add(name.toAbsolutePath().toString());
        }
        return  drives;
    }

    @Override
    public List<CoffeeFile> getDirectoryContent(Path path) throws IOException {
        List<CoffeeFile> directoryContent = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry: stream) {
                PosixFileAttributes attr = Files.readAttributes(path, PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                directoryContent.add(new CoffeeFile(iconConverter.convert(entry.toFile()),
                        entry.getFileName().toString(), attr.size(),
                        simpleDate.format(attr.lastModifiedTime().toMillis()),
                        entry.toAbsolutePath().toString(),
                        PosixFilePermissions.toString(attr.permissions()), Files.isDirectory(entry)));
                }
            }
        //Collections.sort(directoryContent, fileComparator);
        directoryContent.sort(fileComparator);
        return directoryContent;
    }

    @Override
    public void createNewDirectory(Path path, String name) throws IOException {
        path = Paths.get(path + File.separator + name);
        Files.createDirectory(path);
    }

    @Override
    public void copy(CopyProgressDialog dialog, Path target, ObservableList<CoffeeFile> selectedItems, boolean overwrite,
                     boolean preserve) throws IOException {
        super.copy(dialog, target, selectedItems, overwrite, preserve);
    }

    @Override
    public void delete(DeleteDialog dialog, ObservableList<CoffeeFile> target) throws IOException {
        super.delete(dialog, target);
    }

    @Override
    public void openFileWithAssociatedProgram(File fileToOpen) throws IOException {
        if(Desktop.isDesktopSupported()) {
            desktop.open(fileToOpen);
        }
    }
}
