package com.game.gui.utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageUtils {
    public static ImageIcon getResizedIcon(String path, int width, int height) {
        // Check if path exists

        if (!new File(path).exists()) {
            throw new RuntimeException("Error: file not found: " + path);
        }
        ImageIcon icon = new ImageIcon(path);
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
