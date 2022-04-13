package com.ravingarinc.oose.assignment1.maze.icon;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;
import com.ravingarinc.oose.assignment1.maze.Symbol;

public class Element implements Icon {
    protected final Position position;

    private final static String defaultMessage = "Use W, S, A and D to move up, down, left or right! Try and reach the end '" + Symbol.END + "' symbol!";

    public Element(int row, int column) {
        this.position = new Position(row, column);
    }

    @Override
    public String[][] getSymbol() {
        String[][] symbol = new String[3][5];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 5; j++) {
                symbol[i][j] = " \033[m";
            }
        }
        return symbol;
    }

    @Override
    public boolean onMoveTo(Player player, Direction direction) {
        player.sendMessage(defaultMessage);
        return true;
    }

    @Override
    public boolean onMoveFrom(Player player, Direction direction) {
        player.sendMessage(defaultMessage);
        return true;
    }

    @Override
    public void setNext(Icon next) {
        throw new IllegalStateException("Cannot set next on base element!");
    }

    @Override
    public Position getPos() {
        return position;
    }
}
