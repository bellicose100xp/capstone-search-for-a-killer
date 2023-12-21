package com.game.ui;

import com.game.view.framework.InputCollector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

public class GamePanel extends JPanel {
    public static JTextArea consoleOutput = new JTextArea();
    public static TextField inputBox;
    public static JTextArea helpText = new JTextArea();

    public GamePanel() {
        /*
         * Console Output Text Area
         */
        consoleOutput.setEditable(false);
        consoleOutput.setLineWrap(true);
        consoleOutput.setWrapStyleWord(true);
        consoleOutput.setFont(new Font("Monospaced", 0, 14));
        // consoleOutput.setBounds(71, 74 , 807, 382);

        /*
         * Scroll pane container for console output
         */
        JScrollPane scrollPane = new JScrollPane(consoleOutput);
        scrollPane.setBounds(20, 50, 1140, 450);
        this.add(scrollPane);

        /*
         * Input Box
         */
        inputBox = new TextField();
        inputBox.setBounds(20, 510, 1140, 30);
        inputBox.setFont(new Font("Monospaced", 0, 14));
        inputBox.addActionListener(e -> {
            String inputText = inputBox.getText();
            // Adding a newline character so if you just press "enter", empty input will be processed
            InputCollector.textFieldInput = inputText.trim().toLowerCase() + "\n";
            appendTextToOutputConsole(inputText);
            clearInputBox();
        });
        this.add(inputBox);

        /*
         * Game Title
         */

        JLabel gameTitle = new JLabel("Search For A Killer");
        gameTitle.setBounds(459, 10, 1140, 30);
        gameTitle.setFont(new Font(null, 0, 25));
        this.add(gameTitle);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                super.keyPressed(e);
                inputBox.requestFocus();
            }
        });

        /*
         * Help button to display pop-up help dialogue
         */
        JButton helpButton = new JButton("Help");
        helpButton.setBounds(930, 10, 100, 30);
        helpButton.setFocusable(false);
        helpButton.setHorizontalTextPosition(JButton.CENTER);
        helpButton.setVerticalTextPosition(JButton.CENTER);
        helpButton.addActionListener(e -> showHelpDialogWindow());
        helpText.setText(
                "Follow the on-screen prompts to start the game...\nAdditional help will be provided during gameplay.");
        this.add(helpButton);

        /*
         * Create Exit Button
         */
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(1040, 10, 100, 30);
        exitButton.setFocusable(false);
        exitButton.setHorizontalTextPosition(JButton.CENTER);
        exitButton.setVerticalTextPosition(JButton.CENTER);
        exitButton.addActionListener(e -> System.exit(0));
        this.add(exitButton);

        /*
         * GamePanel Specific Settings
         */
        this.setBounds(0, 0, 1200, 600);
        this.setLayout(null);
    }

    private void showHelpDialogWindow() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog helpDialog = new JDialog(parent, "Help", false);
        helpDialog.setLayout(new BorderLayout());

        int padding = 10;
        helpText.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        helpDialog.add(helpText, BorderLayout.CENTER);
        helpDialog.setLocationRelativeTo(parent);
        helpDialog.pack();
        helpDialog.setVisible(true);
    }

    public static void appendTextToOutputConsole(String text) {
        consoleOutput.append(text);
        consoleOutput.setCaretPosition(consoleOutput.getDocument().getLength());
    }

    public static void clearInputBox() {
        inputBox.setText("");
    }
}
