package com.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * An inventory is a collection of Items.
 */
public class Inventory {
    private List<Item> inventory = new ArrayList<>();

    public Inventory(Item... items) {
        inventory.addAll(Arrays.asList(items));
    }

    public Item findByName(String itemName) {
        throw new NotImplementedException("findByName");
    }

    public List<Item> getItems() {
        return inventory;
    }

    public void add(Item item) {
        this.inventory.add(item);
    }

    public boolean contains(Item item) {
        return inventory.contains(item);
    }

    public boolean remove(Item item) {
        return inventory.remove(item);
    }

    @Override
    public String toString() {
        String returnString = "";
        for (Item item : inventory) {
            returnString = returnString + ", " + item.getName();
        }
        // Ensure string is not empty before trimming
        if (returnString.length() > 2) {
            returnString = returnString.substring(2);
        }
        return returnString;
    }
}
