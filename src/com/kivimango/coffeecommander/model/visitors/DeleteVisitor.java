package com.kivimango.coffeecommander.model.visitors;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * Deleting a directory's content recursively including files and sub folders.
 * Note : the read-ony files and directories can not be deleted this way.
 *
 * @author kivimango
 * @since 0.1
 * @version 1.0
 */

public class DeleteVisitor extends SimpleFileVisitor<Path> {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return CONTINUE;
    }
}
