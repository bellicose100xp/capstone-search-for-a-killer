package com.game.controller;

import com.game.model.Character;
import com.game.model.Item;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LoadControllerTest {

    @org.junit.Before
    public void setUp() throws Exception {
        LoadController.loadAllEntities();
    }

    @Test
    public void randomMurderWeaponTest() {
        /*
         * Checking 10 (or otherwise defined) runs in a row if murder weapon is random.
         * Test should pass if at least 1 of the 10 tests are different.
         * Theoretically, this test CAN fail if we are unlucky and get the same 10 items in a row.
         */
        int testRunsAmt = 10;
        Item murderWeapon = LoadController.getMurderWeapon();
        for (int i = 0; i < testRunsAmt; i++) {
            LoadController.loadAllEntities();
            if (LoadController.getMurderWeapon() != murderWeapon) {
                assertTrue(true);
                return;
            }
        }
        fail("Murder weapon not found to be random, was always: " + murderWeapon);
    }

    @Test
    public void randomWeaponUpdatesDescriptionTest() {
        /*
         * Checks the murder weapon to see if the description was updated.
         * If the description was updated, then badDescription and description should be equal
         */
        Item murderWeapon = LoadController.getMurderWeapon();
        assertEquals(murderWeapon.getDescription(), murderWeapon.getBadDescription());

        // Also checking the first non-murderer weapon and confirming their descriptions are NOT equal
        List<Map.Entry<String, Item>> itemsList = new ArrayList<>(LoadController.getItems().entrySet());
        for (Map.Entry<String, Item> item : itemsList) {
            if (!item.getValue().getName().equals(murderWeapon.getName())) {
                assertNotEquals(item.getValue().getDescription(), item.getValue().getBadDescription());
                return;
            }
        }
    }

    @Test
    public void randomMurdererTest() {
        /*
         * Checking 10 (or otherwise defined) runs in a row if murderer is random.
         * Test should pass if at least 1 of the 10 tests are different.
         * Theoretically, this test CAN fail if we are unlucky and get the same 10 items in a row.
         */
        int testRunsAmt = 10;
        Character murderer = LoadController.getMurderer();
        for (int i = 0; i < testRunsAmt; i++) {
            LoadController.loadAllEntities();
            if (LoadController.getMurderer() != murderer) {
                assertTrue(true);
                return;
            }
        }
        fail("Murder not found to be random, was always: " + murderer);
    }

    @Test
    public void randomizeItemsInRoomsTest() {
        /*
         * Checking 10 (or otherwise defined) runs in a row if items in a room are random.
         * Test should pass if at least 1 of the 10 tests are different.
         * Theoretically, this test CAN fail if we are unlucky and get the same 10 rooms with equal items in a row.
         */
        int testRunsAmt = 10;
        String roomToTest = "Office";
        List<Item> itemsOriginal = LoadController.getRooms().get(roomToTest).getInventory().getItems();
        for (int i = 0; i < testRunsAmt; i++) {
            LoadController.loadAllEntities();
            List<Item> itemsUpdated = LoadController.getRooms().get(roomToTest).getInventory().getItems();
            for (Item item : itemsUpdated) {
                if (!itemsOriginal.contains(item)) {
                    assertTrue(true);
                    return;
                }
            }
        }
        fail(roomToTest + " always had the same items.");
    }
}