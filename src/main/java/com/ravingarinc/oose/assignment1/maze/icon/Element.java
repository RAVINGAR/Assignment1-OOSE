package com.ravingarinc.oose.assignment1.maze.icon;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;

public class Element implements Icon {
    private final Position position;

    public Element(int row, int column) {
        this.position = new Position(row, column);
    }

    @Override
    public String[][] getSymbol() {
        String[][] symbol = new String[3][5];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 5; j++) {
                symbol[i][j] = " ";
            }
        }
        return symbol;
    }

    @Override
    public boolean onMove(Player player, Direction direction) {
        return true;
    }

    @Override
    public void setNext(Icon next) {
        throw new IllegalCallerException("Cannot set next on base element!");
    }

    @Override
    public Position getPos() {
        return position;
    }
}
