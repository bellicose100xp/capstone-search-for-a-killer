package com.game.gui;

import com.game.controller.AudioController;
import com.game.controller.LoadController;
import com.game.controller.io.FileUtils;
import com.game.gui.utils.FontUtils;
import com.game.gui.utils.ImageUtils;
import com.game.gui.utils.Score;
import com.game.model.Character;
import com.game.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import static com.game.gui.utils.ColorUtils.*;

public class LocationPanel extends JLayeredPane {
    private static final Integer LAYER_0 = 0;
    private static final Integer LAYER_1 = 1;
    private static final int DOUBLE_CLICK_THRESHOLD = 200;
    private static Timer singleClickTimer;
    private PlayerPanel playerPanel;
    private JPanel itemsSection;
    private JPanel getCharacterSection;
    private JPanel characterSection;
    private JScrollPane itemPane;
    private JScrollPane characterPane;
    private static JTextArea descriptionTextArea;
    private Player player;
    protected static final String defaultImagePath = "data/icons/toolbar/question.png";

    public LocationPanel(PlayerPanel playerPanel, Player player) {
        /* Get a reference to player section */
        this.playerPanel = playerPanel;
        this.player = player;

        /* Background */
        JPanel background = new JPanel();
        background.setBounds(0, 0, 540, 333);
        background.setBackground(LOCATION_SECTION_BACKGROUND.color());
        this.add(background, LAYER_0);

        // Render initial icons for the items in the items section
        itemPane = renderItemSection();
        this.add(itemPane, LAYER_1);

        /* Character Section */

        characterPane = renderCharacterSection();
        this.add(characterPane, LAYER_1);

        /* Scrollable Description / Action Text / Journal Entries Area */
        JScrollPane scrollableDescriptionPane = getDescriptionScrollPane();
        this.add(scrollableDescriptionPane, LAYER_1);

        /* Journal Entries */
        JLabel journalIconLabel = new JournalLabel(player, descriptionTextArea);
        this.add(journalIconLabel, LAYER_1);

        /* Location Section JLayeredPane Bounds */
        this.setBounds(30, 240, 540, 333);
    }

    private JScrollPane renderCharacterSection() {
        int width = 50;
        int height = 50;

        Room currentRoom = LoadController.getRooms().get(player.getCurrentLocation());
        List<Character> characters = currentRoom.getCharactersInRoom();

        JScrollPane scrollPane = customizeScrollPane(20, 96, 361, 76, ITEM_SCROLLBAR.color(),
                ITEM_ICON_BACKGROUND.color());

        JPanel characterPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
        characterPanel.setBackground(ITEM_ICON_BACKGROUND.color());

        for (Character character : characters) {
            ImageIcon characterIcon;
            try {
                characterIcon = ImageUtils.getResizedIcon(character.getIconPath(), width, height);
            } catch (Exception e) {
                characterIcon = ImageUtils.getResizedIcon(defaultImagePath, width, height);
            }
            JLabel characterIconLabel = new JLabel(characterIcon);
            characterIconLabel.setToolTipText(character.getName());
            characterIconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                        // Double click detected, cancel single click timer
                        if (singleClickTimer != null) {
                            singleClickTimer.stop();
                        }
                        playerPanel.incrementScore(Score.getScoreTalk());
                        AudioController.playSFX(10);
                        updateDescriptionTextArea(character.getName() + ": \"" + character.getClue() + "\"");
                    } else {
                        // Single click detected, start/restart the timer
                        if (singleClickTimer != null) {
                            singleClickTimer.stop();
                        }

                        // Start a timer to perform single click action after a waiting threshold
                        singleClickTimer = new Timer(DOUBLE_CLICK_THRESHOLD, event -> {
                            AudioController.playSFX(8);
                            updateDescriptionTextArea(character.getName() + ": " + character.getDescription());
                        });
                        singleClickTimer.setRepeats(false);
                        singleClickTimer.start();
                    }
                }
            });

            characterPanel.add(characterIconLabel);
        }

        JViewport jViewport = new JViewport();
        jViewport.setView(characterPanel);
        scrollPane.setViewport(jViewport);

        return scrollPane;
    }

    private JScrollPane renderItemSection() {
        int width = 50;
        int height = 50;

        Room currentRoom = LoadController.getRooms().get(player.getCurrentLocation());
        Inventory inventory = currentRoom.getInventory();

        JScrollPane scrollPane = customizeScrollPane(20, 17, 361, 76, ITEM_SCROLLBAR.color(),
                ITEM_ICON_BACKGROUND.color());

        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
        itemPanel.setBackground(ITEM_ICON_BACKGROUND.color());

        for (Item item : inventory.getItems()) {
            ImageIcon itemIcon;
            try {
                itemIcon = ImageUtils.getResizedIcon(item.getIconPath(), width, height);
            } catch (Exception e) {
                itemIcon = ImageUtils.getResizedIcon(defaultImagePath, width, height);
            }
            JLabel itemIconLabel = new JLabel(itemIcon);
            itemIconLabel.setToolTipText(item.getName());
            itemIconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                        // Double click detected, cancel the single-click timer
                        if (singleClickTimer != null) {
                            singleClickTimer.stop();
                        }

                        Gson gson = new Gson();
                        String json = FileUtils.readFromFile("data/Utils.json");
                        TypeToken<Map<String, String>> mapType = new TypeToken<>() {
                        };
                        if (player.pickupItem(item)) {
                            String text = gson.fromJson(json, mapType).get("itemAcquiredMessage");
                            text += item.getName();
                            playerPanel.incrementScore(Score.getScorePickupDrop());
                            updateDescriptionTextArea(text);
                            reloadLocationPanel();
                            playerPanel.reloadPlayerPanel();
                        } else {
                            AudioController.playSFX(9);
                            String text = gson.fromJson(json, mapType).get("itemCannotAcquire");
                            text = item.getName() + text;
                            updateDescriptionTextArea(text);
                        }
                    } else {
                        // Single click detected, start/restart the timer
                        if (singleClickTimer != null) {
                            singleClickTimer.stop();
                        }

                        // Start a timer to perform single click action after a waiting threshold
                        singleClickTimer = new Timer(DOUBLE_CLICK_THRESHOLD, event -> {
                            AudioController.playSFX(8);
                            updateDescriptionTextArea(item.getDescription());
                        });
                        singleClickTimer.setRepeats(false);
                        singleClickTimer.start();
                    }
                }
            });

            itemPanel.add(itemIconLabel);
        }

        JViewport jViewport = new JViewport();
        jViewport.setView(itemPanel);
        scrollPane.setViewport(jViewport);

        return scrollPane;
    }

    protected static JScrollPane customizeScrollPane(int x, int y, int width, int height, Color thumbColorIn,
                                                     Color trackColorIn) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(x, y, width, height);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = thumbColorIn;
                this.trackColor = trackColorIn;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(ITEM_SCROLLBAR.color());
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(ITEM_SCROLLBAR.color());
                return button;
            }
        });
        scrollPane.setBorder(null);
        return scrollPane;
    }

    public void clearPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    public void reloadLocationPanel() {
        this.remove(itemPane);
        itemPane = renderItemSection();
        this.add(itemPane, LAYER_1);

        this.remove(characterPane);
        characterPane = renderCharacterSection();
        this.add(characterPane, LAYER_1);
    }

    private JScrollPane getDescriptionScrollPane() {
        descriptionTextArea = createDescriptionTextArea();

        // Create Scroll Pane and add description text area to scroll pane
        JScrollPane scrollableDescriptionPane = new JScrollPane();
        scrollableDescriptionPane.setBackground(ITEM_DESCRIPTION_BACKGROUND.color());
        scrollableDescriptionPane.setViewportView(descriptionTextArea);

        // Scroll pane specific setting
        scrollableDescriptionPane.setBounds(20, 174, 502, 142);
        scrollableDescriptionPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollableDescriptionPane;
    }

    private JTextArea createDescriptionTextArea() {
        JTextArea description = new JTextArea();
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setBackground(ITEM_DESCRIPTION_BACKGROUND.color());
        description.setForeground(ITEM_TEXT.color());
        description.setFont(FontUtils.DEFAULT_FONT.deriveFont(16f));
        int padding = 10;
        description.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        description.setBounds(5, 5, 502, 142);
        return description;
    }

    public void updateDescriptionTextArea(String text) {
        getDescriptionTextArea().setText(text);
    }

    public static JTextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public void setDescriptionTextArea(JTextArea descriptionTextArea) {
        LocationPanel.descriptionTextArea = descriptionTextArea;
    }
}
