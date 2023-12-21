package com.game.gui.toolbar;

import com.game.controller.io.FileUtils;
import com.game.gui.utils.FontUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Map;

import static com.game.gui.utils.ColorUtils.HELP_BACKGROUND;
import static com.game.gui.utils.ColorUtils.HELP_TEXT;


public class HelpIconLabel extends TitleIconLabel {

    public HelpIconLabel(String iconPath, int x, int y, int width, int height) {
        super(iconPath, x, y, width, height);
    }

    public void showHelp() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parent, "Help", false);
        dialog.setLayout(new BorderLayout());

        JTextArea helpTextArea = getHelpTextArea();
        dialog.add(new JScrollPane(helpTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close Dialog");
        closeButton.addActionListener(e -> {
            dialog.dispose(); // Close the dialog when the button is clicked
        });

        dialog.add(closeButton, BorderLayout.SOUTH);
        dialog.setPreferredSize(new Dimension(800, 500));
        dialog.setLocation(parent.getX() + 100, parent.getY() + 100);
        dialog.pack(); // set dialogue size to fit textArea content
        dialog.setVisible(true);
    }

    private static JTextArea getHelpTextArea() {
        Gson gson = new Gson();
        String json = FileUtils.readFromFile("data/Help.json");
        TypeToken<Map<String, String>> mapType = new TypeToken<>() {
        };
        String helpText = gson.fromJson(json, mapType).get("helpMessage");

        JTextArea helpTextArea = new JTextArea(helpText);
        int padding = 10;
        helpTextArea.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        helpTextArea.setEditable(false);
        helpTextArea.setLineWrap(true);
        helpTextArea.setWrapStyleWord(true);
        helpTextArea.setBackground(HELP_BACKGROUND.color());
        helpTextArea.setForeground(HELP_TEXT.color());
        helpTextArea.setFont(FontUtils.DEFAULT_FONT.deriveFont(16f));
        return helpTextArea;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        showHelp();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
