package com.kivimango.coffeecommander.model.visitors;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Copies a folder with all contents recursively.
 * @author Ernestas Gruodis (modified by kivimango : replaced adding events with System.err printing in the catch blocks)
 */

@Deprecated
public class OldCopyFileVisitor extends SimpleFileVisitor<Path> {

    private final Path source;
    private final Path target;
    private boolean overwrite = true;
    private CopyOption[] options;

    /**
     * @param source source file path.
     * @param target target file path.
     * @param overwrite {@code true} if existing file should be replaced.
     */

    OldCopyFileVisitor(Path source, Path target, boolean overwrite) {
        this.source = source;
        this.target = target;
        this.overwrite = overwrite;
        options = overwrite ? new CopyOption[]{COPY_ATTRIBUTES, REPLACE_EXISTING} : new CopyOption[0];
    }

    @Override
    public synchronized FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        Path newDir = target.resolve(source.relativize(dir));
        /*
        try {
            Files.copy(dir, newDir, options);
        } catch (FileAlreadyExistsException ex) {
            if (!overwrite) {
                return FileVisitResult.TERMINATE;
            } else {
                return FileVisitResult.CONTINUE;
            }
        } catch (DirectoryNotEmptyException ex) {
            //Ignore
        } catch (IOException ex) {
            return FileVisitResult.SKIP_SUBTREE;
        }*/
        if(!Files.exists(newDir)) {
            try {
                Files.createDirectory(newDir);
                //Files.copy(dir, newDir, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public synchronized FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        Path newFile = target.resolve(source.relativize(file));
        System.out.println(newFile);
        try {
            Files.copy(file, newFile, options);
        } catch (FileAlreadyExistsException ex) {
            System.err.println("File already exists: " + newFile.toString());
        } catch (NoSuchFileException ex) {
            ex.printStackTrace();
            System.err.println("No such file: " + newFile.getParent());
        } catch(DirectoryNotEmptyException dne) {
            System.err.println("Directory not empty: " + newFile.toString());
        } catch (IOException ex) {
            System.err.println("Unable to create a file: " + newFile.toString());
        }
        return FileVisitResult.CONTINUE;
    }

    /*
    @Override
    public synchronized FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        if (exc == null) {
            Path newDir = target.resolve(source.relativize(dir));
            try {
                FileTime time = Files.getLastModifiedTime(dir);
                Files.setLastModifiedTime(newDir, time);
            } catch (IOException ex) {
                System.err.println("Unable to copy all attributes to" + newDir.toString());
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public synchronized FileVisitResult visitFileFailed(Path file, IOException ex) {
        if (ex instanceof FileSystemLoopException) {
            System.err.println("Cycle detected: " + file.toString());
        } else {
            System.err.println("Unable to copy" + file.toString());
        }
        return FileVisitResult.CONTINUE;
    }*/

}
