package com.kivimango.coffeecommander.model;

import javafx.scene.image.Image;
import java.util.Date;

public class CoffeeFile {

    private Image icon;
    private String name;
    private long size;
    private Date date;

    public CoffeeFile(Image icon, String name, long size, Date date) {
        this.icon = icon;
        this.name = name;
        this.size = size;
        this.date = date;
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

    public long getSize() {
        return size;
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
}
