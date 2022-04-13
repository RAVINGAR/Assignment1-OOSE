package com.ravingarinc.oose.assignment1.display;

public class ViewerFormatError extends Exception {

    public ViewerFormatError(String message) {
        super("Viewer encountered format error: " + message);
    }
}
