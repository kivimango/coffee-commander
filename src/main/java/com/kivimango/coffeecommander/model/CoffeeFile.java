package com.kivimango.coffeecommander.model;

import javafx.scene.image.Image;
import java.util.Comparator;

public class CoffeeFile implements Comparator<CoffeeFile> {

    private Image icon;
    private String name;
    private long size;
    private String date;
    private String path;
    private String permissions;
    private boolean isDir = false;
    private static final String DIR_SZE = "<DIR>";

    public CoffeeFile(Image icon, String name, long size, String date, String path, String permissions, boolean isDir) {
        this.icon = icon;
        this.name = name;
        this.size = size;
        this.date = date;
        this.path = path;
        this.permissions = permissions;
        this.isDir = isDir;
    }

    public Image getIcon() {
        return icon;
    }
    public String getName() {
        return name;
    }
    public String getSize() {
        return (size>0) ? formatSize(size) : DIR_SZE;
    }
    public String getDate() {
        return date;
    }
    public String getPath() { return path; }
    public String getPermissions() {
        return permissions;
    }
    public boolean isDir() { return isDir; }

    /**
     * Converting the file's size to a human readable format.
     * Code taken from here : https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
     * @param size The file's size in bytes
     * @return The formatted string
     */

    private static String formatSize(long size) {
        if (size < 1024) return size + " B";
        int z = (63 - Long.numberOfLeadingZeros(size)) / 10;
        return String.format("%.1f %sB", (double)size / (1L << (z*10)), " KMGTPE".charAt(z));
    }

    @Override
    public int compare(CoffeeFile o1, CoffeeFile o2) {
        int b1 = o1.isDir() ? 1 : 0;
        int b2 = o2.isDir() ? 1 : 0;
        return b2-b1;
    }
}
