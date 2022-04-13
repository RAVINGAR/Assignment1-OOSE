package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Symbol;

public class Door extends Additive {
    protected final Direction blocking;
    protected final Colour colour;
    private boolean isOpen; //This is private intentionally, as we don't want the FakeDoor being able to modify this.

    public Door(Direction blocking, Colour colour, int row, int column) {
        super(row, column);
        this.blocking = blocking;
        this.colour = colour;
        isOpen = false;
    }

    @Override
    public String[][] getSymbol() {
        String[][] symbols = next.getSymbol();

        String character = isOpen ? " " : colour.toString() + Symbol.DOOR;

        switch(blocking) {
            case UP,DOWN -> {
                for(int i = 1; i < 4; i++) {
                    symbols[blocking == Direction.UP ? 0 : 2][i] = character;
                }
            }
            case LEFT -> symbols[1][0] = character;
            case RIGHT -> symbols[1][4] = character;
        }
        return symbols;
    }

    @Override
    public boolean onMoveTo(Player player, Direction direction) {
        if(next.onMoveTo(player, direction)) {
            return checkDoor(player, direction);
        }
        return false;
    }

    @Override
    public boolean onMoveFrom(Player player, Direction direction) {
        if(next.onMoveFrom(player, direction)) {
            return checkDoor(player, direction);
        }
        return false;
    }

    protected boolean checkDoor(Player player, Direction direction) {
        if(!isOpen) {
            if(direction == blocking) {
                if(player.hasKey(colour)) {
                    player.removeKey(colour);
                    isOpen = true;
                    player.sendMessage("You have unlocked the door!");
                    return true;
                }
                else {
                    player.sendMessage("A magical " + colour.getReadableName() + " door blocks your way!");
                    return false;
                }
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }

    protected boolean isOpen() { return isOpen; }

    protected Direction getBlockDirection() { return blocking; }

    protected Colour getDoorColour() { return colour; }
}
