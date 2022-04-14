package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;
import com.ravingarinc.oose.assignment1.maze.icon.Element;
import com.ravingarinc.oose.assignment1.maze.icon.Icon;

/**
 * Purpose of this class is to provide the base method implementations for the decorations. As not all decorations
 * need to override the below methods, so overall this just helps reduce repetitive code.
 */
public abstract class Additive implements Icon {
    protected Icon next;

    public Additive(int row, int column) {
        next = new Element(row, column);
    }

    @Override
    public Position getPos() {
        return next.getPos();
    }

    @Override
    public void setNext(Icon next) {
        this.next = next;
    }

    @Override
    public String[][] getSymbol() {
        return next.getSymbol();
    }

    @Override
    public boolean onMoveTo(Player player, Direction direction) {
        return next.onMoveTo(player, direction);
    }

    @Override
    public boolean onMoveFrom(Player player, Direction direction) { return next.onMoveFrom(player, direction); }
}
