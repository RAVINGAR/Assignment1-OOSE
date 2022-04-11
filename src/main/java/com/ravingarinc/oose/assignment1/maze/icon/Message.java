package com.ravingarinc.oose.assignment1.maze.icon;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;

public class Message extends Element {
    private final String message;

    public Message(Icon next, String message) {
        super(next);
        this.message = message;
    }

    @Override
    public boolean onMove(Player player, Direction direction) {
        if(next.onMove(player, direction)) {
            player.sendMessage(message);
            return true;
        }
        else {
            return false;
        }
    }
}
