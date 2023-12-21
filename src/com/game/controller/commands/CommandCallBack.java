package com.game.controller.commands;

import com.game.model.Entity;

/*
 * Functional interface, it receives an Entity, it does something (gotta be implemented) then returns true or false
 */
public interface CommandCallBack {
    public boolean execute(Entity target);
}
