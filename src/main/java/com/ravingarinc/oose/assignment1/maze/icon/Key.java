package com.ravingarinc.oose.assignment1.maze.icon;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Symbol;

public class Key extends Element {
    private final Colour colour;
    private boolean isPickedUp;

    public Key(Icon next, Colour colour) {
        super(next);
        this.colour = colour;
        isPickedUp = false;
    }

    @Override
    public char[][] getSymbol() {
        char[][] symbol = next.getSymbol();
        for(int i = 1; i < 4; i++) {
            if(symbol[1][i] == ' ') {
                symbol[1][i] = Symbol.KEY.getSymbol();
                break;
            }
        }
        return symbol;
    }

    //For keys this should be the LAST item in the chain to be called.
    //Since it must check if players can go through doors or not
    public boolean onMove(Player player, Direction direction) {
        if(next.onMove(player, direction)) {
            player.addKey(colour);
            return true;
        }
        else {
            return false;
        }
    }
}
