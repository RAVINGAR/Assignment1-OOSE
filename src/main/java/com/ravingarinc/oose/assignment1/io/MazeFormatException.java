package com.ravingarinc.oose.assignment1.io;

public class MazeFormatException extends Exception {

    public MazeFormatException(String message) {
        super("Failed to read maze input file. Encountered error; " + message);
    }
}
