package com.game.gui;

import com.game.gui.utils.FontUtils;

import javax.swing.*;

import static com.game.gui.utils.ColorUtils.LOCATION_DESCRIPTION_BACKGROUND;
import static com.game.gui.utils.ColorUtils.LOCATION_DESCRIPTION_TEXT;

public class LocationDescriptionPanel extends JPanel {
    private final JTextArea textArea;

    public LocationDescriptionPanel() {
        textArea = createTextArea();
        this.add(textArea);

        this.setBackground(LOCATION_DESCRIPTION_BACKGROUND.color());
        this.setBounds(586, 462, 391, 111);
    }

    private JTextArea createTextArea() {
        final JTextArea textArea;
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(LOCATION_DESCRIPTION_BACKGROUND.color());
        textArea.setForeground(LOCATION_DESCRIPTION_TEXT.color());
        textArea.setFont(FontUtils.DEFAULT_FONT.deriveFont(16f));
        int padding = 10;
        textArea.setBorder(
                BorderFactory.createEmptyBorder(0, padding, padding, padding));
        textArea.setBounds(0, 0, 391, 111);
        return textArea;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setText(String text) {
        textArea.setText(text);
    }
}
