package com.ravingarinc.oose.assignment1.maze.icon.decorations;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Symbol;

public class Wall extends Additive {
    private final Direction blocking;

    public Wall(Direction blocking, int row, int column) {
        super(row, column);
        this.blocking = blocking;
    }

    @Override
    public String[][] getSymbol() {
        String[][] symbol = next.getSymbol();

        //Use of decorator pattern to 'append' to the base symbol. Here, corner icons should not be
        //considered since each 'Wall' does not actually know about any other walls
        //o is represented as a corner symbol which will be considered later for appropriate corner symbols
        switch(blocking) {
            case UP,DOWN -> {
                for(int i = 1; i < 4; i++) {
                    symbol[blocking == Direction.UP ? 0 : 2][i] = Symbol.HORIZONTAL_WALL.toString();
                }
            }
            case LEFT -> symbol[1][0] = Symbol.VERTICAL_WALL.toString();
            case RIGHT -> symbol[1][4] = Symbol.VERTICAL_WALL.toString();
        }

        return symbol;
    }

    @Override
    public boolean onMoveTo(Player player, Direction direction) {
        if(next.onMoveTo(player, direction)) {
            return checkWall(player, direction);
        }
        else {
            return false;
        }
    }

    @Override
    public boolean onMoveFrom(Player player, Direction direction) {
        if(next.onMoveFrom(player, direction)) {
            return checkWall(player, direction);
        }
        else {
            return false;
        }
    }

    private boolean checkWall(Player player, Direction direction) {
        if(direction == blocking) {
            player.sendMessage("You cannot move that way!");
            return false;
        }
        else {
            return true;
        }
    }
}
