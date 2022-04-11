package com.ravingarinc.oose.assignment1.maze.icon;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Symbol;

public class Wall extends Element {
    private final Direction blocking;

    public Wall(Icon next, Direction blocking) {
        super(next);
        this.blocking = blocking;
    }

    @Override
    public char[][] getSymbol() {
        char[][] symbol = next.getSymbol();

        //Use of decorator pattern to 'append' to the base symbol. Here, corner icons should not be
        //considered since each 'Wall' does not actually know about any other walls
        //o is represented as a corner symbol which will be considered later for appropriate corner symbols
        switch(blocking) {
            case UP,DOWN -> {
                for(int i = 1; i < 4; i++) {
                    symbol[blocking == Direction.UP ? 0 : 2][i] = Symbol.HORIZONTAL_WALL.getSymbol();
                }
            }
            case LEFT -> {
                symbol[1][0] = Symbol.VERTICAL_WALL.getSymbol();
            }
            case RIGHT -> {
                symbol[1][4] = Symbol.VERTICAL_WALL.getSymbol();
            }
        }

        return symbol;
    }

    @Override
    public boolean onMove(Player player, Direction direction) {
        if(direction == blocking) {
            player.sendMessage("You cannot move that way!");
            return false;
        }
        else {
            return next.onMove(player, direction);
        }
    }
}
