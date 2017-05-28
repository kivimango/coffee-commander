package com.kivimango.coffeecommander.model;

import com.kivimango.coffeecommander.util.FileIconConverter;
import java.awt.Desktop;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BaseModel {

    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    FileIconConverter iconConverter = new FileIconConverter();
    List<CoffeeFile> directoryContent = new ArrayList<>();
    Desktop desktop = Desktop.getDesktop();

}
