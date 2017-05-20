package com.kivimango.coffeecommander.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSystemDAO {

    FileIconConverter iconConverter = new FileIconConverter();
    List<CoffeeFile> directoryContent = new ArrayList<>();

    public FileSystemDAO() {
    }

    public List<CoffeeFile> getDirectoryContent(File path) {
        if(!directoryContent.isEmpty()) {
            directoryContent.clear();
        }

        File[] content = path.listFiles();
        for(File f: content) {
            directoryContent.add(new CoffeeFile(iconConverter.convert(f), f.getName(), f.length(), new Date(f.lastModified())));
        }
        return directoryContent;
    }
}
