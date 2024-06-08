package com.ayman.tennis.service;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String lastName){
        super("Player with the last name " + lastName + " already exists.");
    }
}
