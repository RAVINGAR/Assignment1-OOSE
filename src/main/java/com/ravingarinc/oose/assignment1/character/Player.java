package com.ravingarinc.oose.assignment1.character;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;
import com.ravingarinc.oose.assignment1.maze.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Player {
    private final Map<Colour, Integer> keys;
    private Position position;
    private String message;
    private String symbol;
    private boolean finished;

    public Player(int row, int column) {
        keys = new HashMap<>();
        finished = false;
        for(Colour c : Colour.values()) {
            keys.put(c, 0);
        }
        position = new Position(row, column);
        message = "Use W, S, A and D to move up, down, left or right! Try and reach the end '" + Symbol.END + "' symbol!";
        symbol = Colour.WHITE + Symbol.UP.toString();
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

    public void finishGame() {
        finished = true;
    }

    public boolean isFinished() { return finished; }

    public void move(Direction direction) {
        symbol = Colour.WHITE + direction.getSymbol();
        position.move(direction);
    }

    public int row() { return position.row(); }
    public int column() { return position.column(); }

    public void sendMessage(String message) {
        this.message = message;
    }

    public String getSymbol() { return symbol; };

    public String getMessage() { return message; }
}
