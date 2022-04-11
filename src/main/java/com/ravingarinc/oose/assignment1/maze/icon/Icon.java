package com.ravingarinc.oose.assignment1.maze.icon;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;

public interface Icon {
    String[][] getSymbol();

    /**
     * Called when a player attempts to move to this icon
     * @param direction The direction in which the player is trying to move
     * @return True if player can move to this icon
     */
    boolean onMove(Player player, Direction direction);

    void setNext(Icon next);

    Position getPos();
}
