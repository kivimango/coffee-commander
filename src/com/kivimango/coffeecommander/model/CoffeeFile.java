package com.kivimango.coffeecommander.model;

import javafx.scene.image.Image;

public class CoffeeFile {

    private Image icon;
    private String name;
    private long size;
    private String date;
    private String path;

    public CoffeeFile(Image icon, String name, long size, String date, String path) {
        this.icon = icon;
        this.name = name;
        this.size = size;
        this.date = date;
        this.path = path;
    }

    public Image getIcon() {
        return icon;
    }
    public String getName() {
        return name;
    }
    public String getSize() {
        return formatSize(size);
    }
    public String getDate() {
        return date;
    }
    public String getPath() {
        return path;
    }

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
}
