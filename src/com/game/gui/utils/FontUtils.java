package com.game.gui.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontUtils {
    public static Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);
    public static Font EDOSZ_FONT = registerCustomTitleFont();

    public static Font registerCustomTitleFont() {
        try {
            Font customTitleFont =
                    Font.createFont(Font.TRUETYPE_FONT, new File("data/font/edosz.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customTitleFont);
            return customTitleFont;
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException("Error getting custom title font");
        }
    }
}
