package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;
import com.ravingarinc.oose.assignment1.maze.icon.Element;
import com.ravingarinc.oose.assignment1.maze.icon.Icon;

public abstract class Additive implements Icon {
    protected Icon next; //Due to the way we load maze objects, this needs to be changed AFTER initialisation.

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
