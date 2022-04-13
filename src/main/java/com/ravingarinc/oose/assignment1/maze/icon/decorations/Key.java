package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Symbol;

public class Key extends Additive {
    private final Colour colour;
    private boolean pickedUp;

    public Key(Colour colour, int row, int column) {
        super(row, column);
        this.colour = colour;
        pickedUp = false;
    }

    @Override
    public String[][] getSymbol() {
        String[][] symbol = next.getSymbol();
        if(!pickedUp) {
            for(int i = 1; i < 4; i++) {
                if(symbol[1][i].contains(" ")) {
                    symbol[1][i] = colour.toString() + Symbol.KEY;
                    break;
                }
            }
        }
        return symbol;
    }

    //For keys this should be the LAST item in the chain to be called.
    //Since it must check if players can go through doors or not
    public boolean onMoveTo(Player player, Direction direction) {
        if(next.onMoveTo(player, direction)) {
            if(!pickedUp) {
                player.addKey(colour);
                pickedUp = true;
            }
            return true;
        }
        else {
            return false;
        }
    }
}
