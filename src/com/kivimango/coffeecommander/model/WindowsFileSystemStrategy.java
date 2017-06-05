package com.kivimango.coffeecommander.model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class WindowsFileSystemStrategy extends BaseModel implements FileSystemStrategy {


    @Override
    public List<String> getDrives() {
        List<String> drives = new ArrayList<>();
        for(File f : File.listRoots()) {
            drives.add(f.getAbsolutePath());
        }
        return  drives;
    }

    @Override
    public List<CoffeeFile> getDirectoryContent(Path path) {
        if(!directoryContent.isEmpty()) {
            directoryContent.clear();
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry: stream) {
                DosFileAttributes attr = Files.readAttributes(path, DosFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                directoryContent.add(new CoffeeFile(iconConverter.convert(entry.toFile()),
                        entry.getFileName().toString(), attr.size(),
                        simpleDate.format(attr.lastModifiedTime().toMillis()),
                        entry.toAbsolutePath().toString(),
                        attr.toString()));
            }
        } catch (IOException | DirectoryIteratorException x) {
            }
        return directoryContent;
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
