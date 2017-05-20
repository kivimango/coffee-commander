package com.kivimango.coffeecommander.model;

import java.util.Date;

public class CoffeeFile {

    private String name;
    private long size;
    private Date date;

    public CoffeeFile(String name, long size, Date date) {
        this.name = name;
        this.size = size;
        this.date = date;
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
