package com.chess.gui;

import javax.swing.*;
import java.awt.*;

public class ImageLoader {

    public static ImageIcon load(String fileName) {

        java.net.URL url = ImageLoader.class.getResource("/resources/pieces/" + fileName);

        if (url == null) {
            System.out.println("Missing image: " + fileName);
            return null;
        }

        Image img = new ImageIcon(url).getImage();
        Image scaled = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);

        return new ImageIcon(scaled);
    }
}