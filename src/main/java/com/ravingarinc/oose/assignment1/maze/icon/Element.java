package com.ravingarinc.oose.assignment1.maze.icon;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Direction;

public class Element implements Icon {
    protected final Icon next;

    public Element(Icon next) {
        this.next = next;
    }

    public char[][] getSymbol() {
        char[][] symbol = new char[3][5];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 5; j++) {
                symbol[i][j] = ' ';
            }
        }
        return symbol;
    }

    public boolean onMove(Player player, Direction direction) {
        return true;
    }
}
