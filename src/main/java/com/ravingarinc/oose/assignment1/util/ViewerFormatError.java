package com.ravingarinc.oose.assignment1.util;

public class ViewerFormatError extends GenericMazeException {
    public ViewerFormatError(String message, Exception cause) {
        super("Viewer encountered format error: " + message, cause);
    }


}
