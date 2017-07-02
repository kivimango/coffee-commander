package com.kivimango.coffeecommander.model.tasks;

import com.kivimango.coffeecommander.model.CoffeeFile;
import com.kivimango.coffeecommander.model.visitors.CopyVisitor;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import java.nio.file.*;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class CopyTask extends Task<Void> {

    private final ObservableList<CoffeeFile> toCopy;
    private Path target;
    private boolean overwrite = true;
    private boolean preserve = true;
    private int i = 1;
    private int itemCount = 0;


    public CopyTask(Path target, ObservableList<CoffeeFile> list, boolean overwrite, boolean preserve) {
        this.toCopy = list;
        this.target = target;
        this.overwrite = overwrite;
        this.preserve = preserve;
        this.itemCount = list.size();
    }

    @Override
    protected Void call() throws Exception {
        boolean isDir = Files.isDirectory(target);

        // Looping through the selected items and copy them one-by-one
        for(CoffeeFile f : toCopy) {
            if (isCancelled()) {
                break;
            }

            updateMessage(f.getPath());
            updateProgress(i++,itemCount);

            Path source = Paths.get(f.getPath());
            Path dest = (isDir) ? target.resolve(source.getFileName()) : target;

            System.out.println("Source is : " + source);
            System.out.println("Target is : " + dest);

            if(isDir) {
                Files.walkFileTree(source, new CopyVisitor(source, dest, overwrite, preserve));
            } else {
                CopyOption[] options = (overwrite) ? new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS }
                        : new CopyOption[] { REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS };
                if (Files.notExists(target) || overwrite) {
                    Files.copy(source, target, options);
                }
            }
        }
        return null;
    }
}
