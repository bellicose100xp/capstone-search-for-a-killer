package com.game.gui;

import com.game.controller.AudioController;
import com.game.gui.utils.ImageUtils;
import com.game.model.Item;
import com.game.model.Player;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JournalLabel extends JLabel {
    private final Player player;
    private final JTextArea descriptionTextArea;

    public JournalLabel(Player player, JTextArea descriptionTextArea) {
        this.player = player;
        this.descriptionTextArea = descriptionTextArea;

        ImageIcon journalIcon = ImageUtils.getResizedIcon("data/icons/toolbar/journal.png", 140, 140);
        this.setIcon(journalIcon);

        this.setToolTipText("Evidence Journal");
        this.setBounds(381, 20, 140, 140);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showJournalEntries();
            }
        });
    }

    private void showJournalEntries() {
        AudioController.playSFX(7);
        descriptionTextArea.setText("");
        int count = 1;
        for (Item item : player.getInventory().getItems()) {
            descriptionTextArea.append(count++ + ". " + item.getName() + " - " + item.getDescription() + "\n\n");
        }
    }
}
