package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Symbol;

/*
FakeDoor represents a reflection of a Door. Due to the way the maze program works, if some sort of barrier
exists on a grid square. It MUST exist on both the side of the grid square AND on the opposing grid square's wall
As such a 'FakeDoor' which represents merely a visual representation of the door which must be present
 */
public class FakeDoor extends Door {
    private final Door reflection;

    public FakeDoor(Door reflection, int row, int column) {
        super(reflection.getBlockDirection().getOpposing(), reflection.getDoorColour(), row, column);
        this.reflection = reflection;
    }

    @Override
    public String[][] getSymbol() {
        String[][] symbols = next.getSymbol();

        String character = reflection.isOpen() ? " " : colour.toString() + Symbol.DOOR;

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
    protected boolean isOpen() {
        return reflection.isOpen();
    }

    @Override
    protected boolean checkDoor(Player player, Direction direction) {
        if(this.isOpen()) {
            return true;
        }
        else {
            if(direction == blocking) {
                if(player.hasKey(colour)) {
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
    }
}
