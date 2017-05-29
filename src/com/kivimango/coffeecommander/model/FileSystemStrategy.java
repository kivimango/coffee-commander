package com.kivimango.coffeecommander.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileSystemStrategy {
    List<String> getDrives();
    List<CoffeeFile> getDirectoryContent(File path) throws IOException;
    void createNewDirectory(Path path, String name) throws IOException;
    void openFileWithAssociatedProgram(File fileToOpen) throws IOException;
}
