package com.ravingarinc.oose.assignment1.util;

/**
 * Used to wrap generally user error exceptions which are caught by the main block.
 * They contain the original exception and help in providing easy debugging via printing the stack trace.
 */
public class MazeErrorException extends Exception {
    private final Exception cause;

    public MazeErrorException(String message, Exception cause) {
        super("Encountered possible user error! \n" + message);
        if(cause == null) {
            throw new IllegalArgumentException("Cause of error exception was null! This cannot occur!");
        }
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " \n    Exception; " + cause.getMessage() + " \n    Stack Trace; " + formatStackTrace();
    }

    public String formatStackTrace() {
        StringBuilder format = new StringBuilder();
        if(cause != null) {
            for(StackTraceElement trace : cause.getStackTrace()) {
                format.append(trace.toString()).append("\n");
            }
        }
        return format.toString();
    }

}
