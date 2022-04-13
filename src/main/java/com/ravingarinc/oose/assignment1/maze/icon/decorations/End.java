package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Colour;
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
                symbols[1][i] = Colour.YELLOW + Symbol.END.toString() + Colour.BLANK;
                break;
            }
        }
        return symbols;

    }

    @Override
    public boolean onMoveTo(Player player, Direction direction) {
        if(next.onMoveTo(player, direction)) {
            player.finishGame();
            return true;
        }
        else {
            return false;
        }
    }
}
