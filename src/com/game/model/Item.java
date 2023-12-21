package com.game.model;

public class Item extends Entity {
    private boolean pickUpable = false;
    private boolean isMurderWeapon;
    private boolean isWeapon;


    public Item() {
    }

    public Item(String name, String decription, boolean canPickUp) {
        super(name, decription);
        setPickUpable(canPickUp);
    }

    public boolean isPickUpable() {
        return pickUpable;
    }

    public void setPickUpable(boolean canPickUp) {
        pickUpable = canPickUp;
    }

    public boolean isMurderWeapon() {
        return isMurderWeapon;
    }

    public void setMurderWeapon(boolean value) {
        this.isMurderWeapon = value;
    }

    public boolean isWeapon() {
        return isWeapon;
    }
}
