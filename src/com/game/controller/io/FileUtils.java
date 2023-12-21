package com.game.controller.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static String readFromFile(String filePath) {
        Path path = Paths.get(filePath);
        String text = null;

        try {
            text = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public static void writeToFile(String content, String filePath) {
        Path path = Paths.get(filePath);

        // Check if file exists, if not create it including any parent directories
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Write content to file
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
