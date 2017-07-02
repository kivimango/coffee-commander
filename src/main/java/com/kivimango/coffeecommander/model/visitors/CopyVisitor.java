package com.kivimango.coffeecommander.model.visitors;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Copies a folder with all contents recursively.
 * @author kivimango
 * @version 1.0
 * @since 0.1
 */

public class CopyVisitor extends SimpleFileVisitor<Path> {

    private final Path source;
    private final Path target;
    private final boolean overwrite;
    private final boolean preserve;

    /**
     * @param source source file path.
     * @param target target file path.
     * @param overwrite {@code true} if existing file should be replaced.
     * @param preserve {@code true} if to keep original file dates.
     */

    public CopyVisitor(Path source, Path target, boolean overwrite, boolean preserve) {
        this.source = source;
        this.target = target;
        this.overwrite = overwrite;
        this.preserve = preserve;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws FileAlreadyExistsException {
        // before visiting entries in a directory we copy the directory
        // (okay if directory already exists).
        CopyOption[] options = (preserve) ? new CopyOption[] { COPY_ATTRIBUTES } : new CopyOption[0];
        Path newDir = target.resolve(source.relativize(dir));
        try {
            Files.copy(dir, newDir, options);
        } catch (IOException x) {
            System.err.format("Unable to create: %s: %s%n", newDir, x);
            return SKIP_SUBTREE;
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        copyFile(file, target.resolve(source.relativize(file)),
                overwrite, preserve);
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        // fix up modification time of directory when done
        if (exc == null && preserve) {
            Path newdir = target.resolve(source.relativize(dir));
            try {
                FileTime time = Files.getLastModifiedTime(dir);
                Files.setLastModifiedTime(newdir, time);
            } catch (IOException x) {
                System.err.format("Unable to copy all attributes to: %s: %s%n", newdir, x);
            }
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        if (exc instanceof FileSystemLoopException) {
            System.err.println("cycle detected: " + file);
        } else {
            System.err.format("Unable to copy: %s: %s%n", file, exc);
        }
        return CONTINUE;
    }

    private void copyFile(Path source, Path target, boolean overwrite, boolean preserve) {
        /** CopyOption[] options = (preserve || overwrite) ?
         new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING } :
         new CopyOption[] { REPLACE_EXISTING };
         */
        CopyOption[] options = new CopyOption[2];
        if(overwrite && preserve) {
            options = new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING };
        } else if(!overwrite && preserve) {
            options = new CopyOption[] { COPY_ATTRIBUTES};
        } else if(overwrite && !preserve) {
            options = new CopyOption[] { REPLACE_EXISTING};
        } else if(!overwrite && !preserve) {
            options = new CopyOption[] {};
        }
        if (Files.notExists(target) || overwrite) {
            try {
                Files.copy(source, target, options);
            } catch (IOException x) {
                System.err.format("Unable to copy: %s: %s%n", source, x);
            }
        }
    }
}