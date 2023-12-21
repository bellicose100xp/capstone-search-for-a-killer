package com.game.gui;

import com.game.controller.LoadController;
import com.game.gui.result.ReportedSelections;
import com.game.model.Character;
import com.game.model.Item;
import com.game.model.Player;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static com.game.gui.result.ReportedSelections.*;
import static org.junit.Assert.assertEquals;

public class ReportKillerTest {
    private ReportKiller reportKiller;
    private Character correctMurderer;
    private Item correctWeapon;
    private Character wrongCharacter;
    private Item wrongWeapon;

    @Before
    public void setUp() throws Exception {
        Player player = LoadController.getPlayer();
        PlayerPanel playerPanel = new PlayerPanel(player);
        reportKiller = new ReportKiller(new JFrame(), playerPanel);

        LoadController.loadAllEntities();
        correctMurderer = LoadController.getCharacters().get("Butler");
        wrongCharacter = LoadController.getCharacters().get("Nanny");
        correctWeapon = LoadController.getItems().get("Knife");
        wrongWeapon = LoadController.getItems().get("Pen");

        reportKiller.setMurderer(correctMurderer);
        reportKiller.setMurderWeapon(correctWeapon);
    }

    @Test
    public void reportedSelectionsHandler_MissingSelection_NoSelection() {
        ReportedSelections reportedSelections = reportKiller.reportedSelectionsHandler(null, null);
        assertEquals(MISSING_SELECTION, reportedSelections);
    }

    @Test
    public void reportedSelectionsHandler_MissingSelection_MissingCharacter() {
        ReportedSelections reportedSelections = reportKiller.reportedSelectionsHandler(null, correctWeapon);
        assertEquals(MISSING_SELECTION, reportedSelections);
    }

    @Test
    public void reportedSelectionsHandler_MissingSelection_MissingWeapon() {
        ReportedSelections reportedSelections = reportKiller.reportedSelectionsHandler(correctMurderer, null);
        assertEquals(MISSING_SELECTION, reportedSelections);
    }

    @Test
    public void reportedSelectionsHandler_WrongCharacter() {
        ReportedSelections reportedSelections = reportKiller.reportedSelectionsHandler(wrongCharacter, correctWeapon);
        assertEquals(WRONG_CHARACTER, reportedSelections);
    }

    @Test
    public void reportedSelectionsHandler_WrongWeapon() {
        ReportedSelections reportedSelections = reportKiller.reportedSelectionsHandler(correctMurderer, wrongWeapon);
        assertEquals(WRONG_WEAPON, reportedSelections);
    }

    @Test
    public void reportedSelectionsHandler_CorrectSelection() {
        ReportedSelections reportedSelections = reportKiller.reportedSelectionsHandler(correctMurderer, correctWeapon);
        assertEquals(CORRECT_SELECTION, reportedSelections);
    }
}