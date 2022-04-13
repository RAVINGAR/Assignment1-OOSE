package com.ravingarinc.oose.assignment1.util;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GenericMazeException extends Exception {
    private final Exception cause;
    private final String className;
    private final int line;

    public GenericMazeException(String message, Exception cause) {
        super(message);
        this.cause = cause;
        className = "";
        line = 0;
    }

    public GenericMazeException(String message, String className, int line) {
        super(message);
        cause = null;
        this.className = className;
        this.line = line;
    }

    @Override
    public String getMessage() {
        return cause == null
                ? super.getMessage() + " @ " + className + ", line " + line
                : super.getMessage() + " with exception; \n    " + cause.getMessage() + " \n    Stack Trace; " + formatStackTrace();
    }

    @Nullable
    public Exception getCause() {
        return cause;
    }

    public String formatStackTrace() {
        if(cause != null) {
            return Arrays.toString(cause.getStackTrace());
        }
        return "";
    }
}
