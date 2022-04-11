package com.ravingarinc.oose.assignment1.character;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.maze.Colour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Player {
    Map<Colour, Integer> keys;

    public Player() {
        keys = new HashMap<>();
        for(Colour c : Colour.values()) {
            keys.put(c, 0);
        }
    }

    public boolean hasKey(Colour colour) {
        return keys.get(colour) > 0;
    }

    public void addKey(Colour colour) {
        Integer i = keys.get(colour);
        keys.put(colour, i+1);
    }

    public void removeKey(Colour colour) {
        Integer i = keys.get(colour);
        if(i == 0) {
            MazeApplication.log(Level.WARNING, "Cannot remove key of player when player has 0 keys!");
        }
        else {
            keys.put(colour, i-1);
        }
    }

    public void sendMessage(String message) {

    }
}
