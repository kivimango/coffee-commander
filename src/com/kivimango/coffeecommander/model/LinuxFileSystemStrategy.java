package com.kivimango.coffeecommander.model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
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
        if(!directoryContent.isEmpty()) {
            directoryContent.clear();
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry: stream) {
                PosixFileAttributes attr = Files.readAttributes(path, PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                directoryContent.add(new CoffeeFile(iconConverter.convert(entry.toFile()),
                        entry.getFileName().toString(), attr.size(),
                        simpleDate.format(attr.lastModifiedTime().toMillis()),
                        entry.toAbsolutePath().toString(),
                        PosixFilePermissions.toString(attr.permissions())));
                }
            } catch (IOException | DirectoryIteratorException x) {
                throw new IOException(x.getMessage());
            }

        return directoryContent;
    }

    @Override
    public void createNewDirectory(Path path, String name) throws IOException {
        path = Paths.get(path + File.separator + name);
        Files.createDirectory(path);
    }

    @Override
    public void delete(Path path) throws IOException {
        super.delete(path);
    }

    @Override
    public void openFileWithAssociatedProgram(File fileToOpen) throws IOException {
        if(Desktop.isDesktopSupported()) {
            desktop.open(fileToOpen);
        }
    }
}
