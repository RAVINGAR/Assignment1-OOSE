package com.ravingarinc.oose.assignment1.maze.icon;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Symbol;

public class Door extends Element {
    private final Direction blocking;
    private final Colour colour;
    private boolean isOpen;

    public Door(Icon next, Direction blocking, Colour colour) {
        super(next);
        this.blocking = blocking;
        this.colour = colour;
        isOpen = false;
    }

    @Override
    public char[][] getSymbol() {
        char[][] symbols = next.getSymbol();

        char character = isOpen ? ' ' : Symbol.DOOR.getSymbol();

        switch(blocking) {
            case UP,DOWN -> {
                for(int i = 1; i < 4; i++) {
                    symbols[blocking == Direction.UP ? 0 : 2][i] = character;
                }
            }
            case LEFT -> {
                symbols[1][0] = character;
            }
            case RIGHT -> {
                symbols[1][4] = character;
            }
        }
        return symbols;
    }

    @Override
    public boolean onMove(Player player, Direction direction) {
        if (direction == blocking && !isOpen) {
            if(next.onMove(player, direction)) {
                if (player.hasKey(colour)) {
                    player.removeKey(colour);
                    this.isOpen = true;
                    player.sendMessage("You have unlocked the door!");
                    return true;
                }
            }
        } else {
            return next.onMove(player, direction);
        }
        return false;
    }
}
