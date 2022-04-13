package com.ravingarinc.oose.assignment1.util;

/**
 * This represents a developer thrown exception to allow for easier debugging.
 * This should only be thrown if some specific act is "illegal", it should not be used to wrap other exceptions.
 */
public class IllegalMazeException extends Exception {
    private final String className;
    private final int line;

    public IllegalMazeException(String message, String className, int line) {
        super("Encountered illegal maze error! \n" + message);
        this.className = className;
        this.line = line;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " in " + className + " @ line " + line;
    }
}
