package com.kivimango.coffeecommander.model;

import java.util.Comparator;

/**
 * Sorting the filelist based on the file object's isDir property :
 * The list will contains directories first, then the files.
 *
 * @author kivimango
 * @version 1.0
 * @since 0.1
 */

public class FileComparator implements Comparator<CoffeeFile> {

    @Override
    public int compare(CoffeeFile o1, CoffeeFile o2) {
        int b1 = o1.isDir() ? 1 : 0;
        int b2 = o2.isDir() ? 1 : 0;
        return b2 - b1;
    }
}
