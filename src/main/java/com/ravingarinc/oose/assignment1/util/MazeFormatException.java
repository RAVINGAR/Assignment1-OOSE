package com.ravingarinc.oose.assignment1.util;

public class MazeFormatException extends GenericMazeException {
    public MazeFormatException(String message, Exception cause) {
        super("Failed to read maze input file. Encountered error; " + message, cause);
    }

    public MazeFormatException(String message, String className, int line) {
        super(message, className, line);
    }

}
