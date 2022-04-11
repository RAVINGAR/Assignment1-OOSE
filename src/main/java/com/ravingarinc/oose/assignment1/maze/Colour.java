package com.ravingarinc.oose.assignment1.maze;

import com.ravingarinc.oose.assignment1.MazeApplication;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public enum Colour {
    RED("31"),
    GREEN("32"),
    YELLOW("33"),
    BLUE("34"),
    MAGENTA("35"),
    CYAN("36"),
    WHITE("37");

    private final String code;

    Colour(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "\033[" + code + "m";
    }

    public String getReadableName() {
        String lowerCase = this.name().toLowerCase();
        return toString() + lowerCase.charAt(0) + lowerCase.substring(1) + "\033[m";
    }

    @NotNull
    public static Colour matchColour(int i) {
        for(Colour c : Colour.values()) {
            if(c.ordinal() + 1 == i) {
                return c;
            }
        }
        MazeApplication.log(Level.WARNING, "Could not match colour for key, using default of white!");
        return Colour.WHITE;
    }
}
