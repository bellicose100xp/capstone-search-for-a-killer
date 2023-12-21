package com.game.view.framework;

/*
 * A custom Exception to throw when the user enters invalid input
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String statement) {
        super(statement);
    }
}
