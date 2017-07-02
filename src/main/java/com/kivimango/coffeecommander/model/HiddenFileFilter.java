package com.kivimango.coffeecommander.model;

import java.io.File;
import java.io.FileFilter;

public class HiddenFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return !pathname.isHidden();
    }
}
