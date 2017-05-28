package com.kivimango.coffeecommander.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Converting a Swing file system icon to a JavaFX Image to able to display in a TableView column.
 * Swing ImageIcon/Icon object does not compatible with JavaFX TableView's imageView.
 * Code copied <a href="from http://stackoverflow.com/questions/28034432/javafx-file-listview-with-icon-and-file-name">here</a>
 *
 * @author kivimango
 * @version 1.0
 * @since 0.1
 */

public class FileIconConverter {

    //final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
    FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    public FileIconConverter() {
    }

    public Image convert(File f) {
        //Icon icon = fc.getUI().getFileView(fc).getIcon(f);
        Icon icon = fileSystemView.getSystemIcon(f);
        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        Image fxImage = SwingFXUtils.toFXImage(
                bufferedImage, null);
        return fxImage;
    }
}
