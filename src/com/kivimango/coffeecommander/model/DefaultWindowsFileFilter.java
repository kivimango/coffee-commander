package com.kivimango.coffeecommander.model;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;

public class DefaultWindowsFileFilter implements DirectoryStream.Filter<Path> {

    public boolean accept(Path file) throws IOException {
            try {
                DosFileAttributes dfa = Files.readAttributes(file, DosFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                //return (!dfa.isHidden() && !dfa.isSystem());
                return !Files.isHidden(file);
            } catch (Exception x) {
                return false;
            }
        }
}
