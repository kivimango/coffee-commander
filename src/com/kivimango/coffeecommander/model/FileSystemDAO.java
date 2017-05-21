package com.kivimango.coffeecommander.model;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSystemDAO {

    private FileIconConverter iconConverter = new FileIconConverter();
    private List<CoffeeFile> directoryContent = new ArrayList<>();
    Desktop desktop = Desktop.getDesktop();

    public FileSystemDAO() {
    }

    public List<String> getDrives() {
        List<String> drives = new ArrayList<>();
        for(File f : File.listRoots()) {
            drives.add(f.getAbsolutePath());
        }
        return  drives;
    }

    public List<CoffeeFile> getDirectoryContent(File path) {
        if(!directoryContent.isEmpty()) {
            directoryContent.clear();
        }

        File[] content = path.listFiles(new HiddenFileFilter());
        for(File f: content) {
            directoryContent.add(new CoffeeFile(iconConverter.convert(f), f.getName(), f.length(), new Date(f.lastModified()), f.getAbsolutePath()));
        }
        return directoryContent;
    }

    public void openFileWithAssociatedProgram(File fileToOpen) throws IOException {
       desktop.open(fileToOpen);
    }

}
