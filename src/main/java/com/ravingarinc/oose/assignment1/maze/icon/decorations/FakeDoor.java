package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;

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
    protected boolean isOpen() { return reflection.isOpen(); }

    @Override
    protected boolean checkDoor(Player player, Direction direction) {
        if(!this.isOpen()) {
            if(direction == blocking) {
                if(!player.hasKey(colour)) {
                    player.sendMessage("A magical " + colour.getReadableName() + " door blocks your way!");
                    return false;
                }
                else {
                    return true;
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
}
