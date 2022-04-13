package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;

public class Message extends Additive {
    private final String message;

    public Message(String message, int row, int column) {
        super(row, column);
        this.message = message;
    }

    @Override
    public boolean onMoveTo(Player player, Direction direction) {
        if(next.onMoveTo(player, direction)) {
            player.sendMessage(message);
            return true;
        }
        else {
            return false;
        }
    }
}
