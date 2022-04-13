package com.ravingarinc.oose.assignment1.maze.icon;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;

public interface Icon {
    String[][] getSymbol();

    /**
     * Called when a player attempts to move TO this icon
     * @param player the player
     * @param direction The direction opposite of where the player is moving
     * @return True if player can move to this icon
     */
    boolean onMoveTo(Player player, Direction direction);

    /**
     * Called when a player attempts to move FROM this icon
     * @param player the player
     * @param direction the direction the player is moving.
     * @return True if player can move from this icon.
     */
    boolean onMoveFrom(Player player, Direction direction);

    void setNext(Icon next);

    Position getPos();
}
