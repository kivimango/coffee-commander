package com.kivimango.coffeecommander.model;

import javafx.scene.image.Image;
import java.util.Date;

public class CoffeeFile {

    private Image icon;
    private String name;
    private long size;
    private Date date;
    private String path;

    public CoffeeFile(Image icon, String name, long size, Date date, String path) {
        this.icon = icon;
        this.name = name;
        this.size = size;
        this.date = date;
        this.path = path;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return formatSize(size);
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
