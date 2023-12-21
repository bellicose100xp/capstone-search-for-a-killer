package com.game.gui;

import com.game.controller.AudioController;
import com.game.controller.io.FileUtils;
import com.game.gui.utils.FontUtils;
import com.game.gui.utils.ImageUtils;
import com.game.gui.utils.Score;
import com.game.model.Inventory;
import com.game.model.Item;
import com.game.model.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import static com.game.gui.utils.ColorUtils.*;

public class PlayerPanel extends JLayeredPane {
    private static final Integer LAYER_0 = 0;
    private static final Integer LAYER_1 = 1;
    private static final int DOUBLE_CLICK_THRESHOLD = 200;
    private static Timer singleClickTimer;
    private LocationPanel locationPanel;
    private JScrollPane evidenceSection;
    private final JLabel scoreSection;
    private int score;
    private final Player player;

    public PlayerPanel(Player player) {
        this.player = player;

        /* Background */
        JPanel background = new JPanel();
        background.setBounds(0, 0, 540, 159);
        background.setBackground(PLAYER_SECTION_BACKGROUND.color());
        this.add(background, LAYER_0);

        /* Player Title */
        JLabel playerTitle = new JLabel("Detective");
        playerTitle.setHorizontalAlignment(SwingConstants.CENTER);
        playerTitle.setVerticalAlignment(SwingConstants.CENTER);
        playerTitle.setForeground(PLAYER_HEADING_TEXT.color());
        playerTitle.setFont(FontUtils.DEFAULT_FONT.deriveFont(36f));
        playerTitle.setBounds(18, 9, 223, 63);
        this.add(playerTitle, LAYER_1);

        /* Score Section */
        this.score = Score.getScoreStart();
        scoreSection = new JLabel("Score: " + score);
        scoreSection.setOpaque(true);
        scoreSection.setHorizontalAlignment(SwingConstants.CENTER);
        scoreSection.setVerticalAlignment(SwingConstants.CENTER);
        scoreSection.setBackground(SCORE_BACKGROUND.color());
        scoreSection.setFont(FontUtils.DEFAULT_FONT.deriveFont(24f));
        scoreSection.setForeground(SCORE_TEXT.color());
        scoreSection.setBounds(254, 9, 265, 58);
        this.add(scoreSection, LAYER_1);

        /* Evidence Section Icons */
        evidenceSection = renderEvidenceSection();
        this.add(evidenceSection, LAYER_1);

        /* JLayeredPane Bounds */
        this.setBounds(30, 71, 540, 159);
    }

    private JScrollPane renderEvidenceSection() {
        // also add single click and double click listener action to each item icon
        int width = 50;
        int height = 50;

        Inventory inventory = player.getInventory();

        JScrollPane scrollPane = LocationPanel.customizeScrollPane(20, 77, 502, 77, PLAYER_SCROLLBAR.color(),
                PLAYER_SECTION_BACKGROUND.color());

        JPanel playerItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
        playerItemsPanel.setBackground(EVIDENCE_ICON_BACKGROUND.color());

        for (Item item : inventory.getItems()) {
            ImageIcon itemIcon;
            try {
                itemIcon = ImageUtils.getResizedIcon(item.getIconPath(), width, height);
            } catch (Exception e) {
                itemIcon = ImageUtils.getResizedIcon(LocationPanel.defaultImagePath, width, height);
            }
            JLabel itemIconLabel = new JLabel(itemIcon);
            itemIconLabel.setToolTipText(item.getName());
            itemIconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {

                        // Double click detected, cancel single click timer
                        if (singleClickTimer != null) {
                            singleClickTimer.stop();
                        }

                        Gson gson = new Gson();
                        String json = FileUtils.readFromFile("data/Utils.json");
                        TypeToken<Map<String, String>> mapType = new TypeToken<>() {
                        };
                        if (player.dropItem(item)) {
                            String text = gson.fromJson(json, mapType).get("itemDroppedMessage");
                            text += item.getName();
                            incrementScore(Score.getScorePickupDrop());
                            locationPanel.updateDescriptionTextArea(text);
                            reloadPlayerPanel();
                            getLocationSection().reloadLocationPanel();
                        } else {
                            AudioController.playSFX(9);
                            String text = gson.fromJson(json, mapType).get("itemCannotDrop");
                            text = item.getName() + text;
                            locationPanel.updateDescriptionTextArea(text);
                        }
                    } else {
                        // Single click detected, start/restart the timer
                        if (singleClickTimer != null) {
                            singleClickTimer.stop();
                        }

                        // Start a timer to perform single click action after a waiting threshold
                        singleClickTimer = new Timer(DOUBLE_CLICK_THRESHOLD, event -> {
                            AudioController.playSFX(8);
                            locationPanel.updateDescriptionTextArea(item.getDescription());
                        });
                        singleClickTimer.setRepeats(false);
                        singleClickTimer.start();
                    }
                }
            });

            playerItemsPanel.add(itemIconLabel);
        }

        JViewport jViewport = new JViewport();
        jViewport.setView(playerItemsPanel);
        scrollPane.setViewport(jViewport);

        return scrollPane;
    }


    public void clearPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    public void reloadPlayerPanel() {
        this.remove(evidenceSection);
        evidenceSection = renderEvidenceSection();
        this.add(evidenceSection, LAYER_1);
    }

    public void incrementScore(int value) {
        score += value;
        if (score < 0) {
            score = 0;
        }
        scoreSection.setText("Score: " + score);
    }

    public int getScore() {
        return score;
    }

    public LocationPanel getLocationSection() {
        return locationPanel;
    }

    public void setLocationSection(LocationPanel locationPanel) {
        this.locationPanel = locationPanel;
    }
}
