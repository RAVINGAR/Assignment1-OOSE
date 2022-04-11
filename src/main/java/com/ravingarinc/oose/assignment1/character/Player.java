package com.ravingarinc.oose.assignment1.character;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Player {
    private final Map<Colour, Integer> keys;
    private Position position;

    public Player(int row, int column) {
        keys = new HashMap<>();
        for(Colour c : Colour.values()) {
            keys.put(c, 0);
        }
        position = new Position(row, column);
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

    public void move(Direction direction) {
        position.move(direction);
    }

    public Position getPos() {
        return position;
    }

    public void sendMessage(String message) {

    }
}
