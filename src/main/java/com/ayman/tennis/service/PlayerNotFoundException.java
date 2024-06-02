package com.ayman.tennis.service;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String lastName){
        super("Player with the last name " + lastName + " could not be found.");
    }
}
