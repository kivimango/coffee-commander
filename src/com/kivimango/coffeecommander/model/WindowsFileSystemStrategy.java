package com.kivimango.coffeecommander.model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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
    public List<CoffeeFile> getDirectoryContent(File path) {
        if(!directoryContent.isEmpty()) {
            directoryContent.clear();
        }

        if(path.isDirectory()) {
            File[] content = path.listFiles(new HiddenFileFilter());
            if(content != null) {
                for(File f: content) {
                    directoryContent.add(new CoffeeFile(iconConverter.convert(f), f.getName(), f.length(), simpleDate.format(f.lastModified()), f.getAbsolutePath()));
                }
            }
        }
        return directoryContent;
    }

    @Override
    public void openFileWithAssociatedProgram(File fileToOpen) throws IOException {
        if(Desktop.isDesktopSupported()) {
            desktop.open(fileToOpen);
        }
    }
}
