package com.kivimango.coffeecommander.model.tasks;

import com.kivimango.coffeecommander.model.CoffeeFile;
import com.kivimango.coffeecommander.model.visitors.DeleteVisitor;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Deleting a directory's content recursively (files and sub directories) on a background thread,
 * and updating the progressbar with the current progress and a label with the current filename.
 *
 * @author kivimango
 * @version 1.0
 * @since 0.1
 */

public class DeleteTask extends Task<Void> {

    private ObservableList<CoffeeFile> toDelete;

    public DeleteTask(ObservableList<CoffeeFile> list) {
        toDelete = list;
    }

    @Override
    protected Void call() throws Exception {
        long i = 1;
        for(CoffeeFile f : toDelete) {
            if (isCancelled()) {
                break;
            }

            updateProgress(i++, toDelete.size());
            updateMessage(f.getPath());

            Path p = Paths.get(f.getPath());
            if(Files.isDirectory(p)) {
                Files.walkFileTree(p, new DeleteVisitor());
            } else {
                Files.delete(p);
            }
        }
        return null;
    }
}
