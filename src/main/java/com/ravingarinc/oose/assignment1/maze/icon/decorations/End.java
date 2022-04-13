package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Symbol;

public class End extends Additive {

    public End(int row, int column) {
        super(row, column);
    }

    @Override
    public String[][] getSymbol() {
        String[][] symbols = next.getSymbol();

        for(int i = 1; i < 4; i++) {
            if(symbols[1][i].contains(" ")) {
                symbols[1][i] = Symbol.END.toString();
                break;
            }
        }
        return symbols;

    }

    @Override
    public boolean onMoveTo(Player player, Direction direction) {
        return next.onMoveTo(player, direction);
    }
}
