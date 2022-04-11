package com.ravingarinc.oose.assignment1.maze;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.maze.icon.Icon;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Maze {
    List<List<Icon>> grid;
    int rows, columns;
    /*
    WH 2 3, Horizontal _ above 2 3, below 2,2
    Therefore this means trace

    One Icon represents a 5x3
    However it shares row 0 and 2, and column 0 and 4 with other icons!

    Displayable size = 1 + rows * 2
                     = 1 + columns * 4
    01234
    ┌───┐
    | o |
    └───┘
    When parsing maze after initial walls, essentially need to check symbols surrounding the o object
    o───o───o
    |       |
    o───o   o
            |
    o───o───o
     */

    public Maze(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        this.grid = new ArrayList<>();
        for(int r = 0; r < rows; r++) {
            this.grid.add(new ArrayList<>());
        }
    }

    public Icon getIcon(int row, int column) {
        return this.grid.get(row).get(column);
    }

    public void updateMaze() {

    }

    public void putIcon(int row, int column, Icon icon) {
        this.grid.get(row).add(column, icon);
    }

    /**
     * Transforms maze into a displayable format in terms of char characters.
     * @return 2D char array containing values of the maze or null if icon is invalid
     */
    public char[][] getDisplayableMaze() {
        //The final size will be larger than the specified, since Walls
        char[][] display = new char[1 + rows * 2][1 + columns * 4];

        int r = 0, c;
        for(List<Icon> row : grid) {
            c = 0;
            for(Icon icon : row) {
                char[][] symbol = icon.getSymbol();
                if(symbol.length != 3 || symbol[0].length != 5) {
                    return null;
                }
                for(int i = 0; i < 3; i++) {
                    for(int j = 0; j < 5; j++) {
                        display[r+i][c+j] = symbol[i][j];
                    }
                }
                //Top left corner is [r][c]
                //Top right corner is [r][c+4]
                //Bottom left corner is [r+2][c]
                //Bottom right corner is [r+2[c+4]

                /*
                The goal here now is to fill the corner blocks with their corresponding symbols
                However since for every icon only the icons to the LEFT and ABOVE will have been filled
                at every iteration. This means only the top left corner needs to be considered

                Since only the top left corner is considered, what will occur is that the very right wall (edge)
                will not have its corners updated. And neither will the very bottom wall.

                Whilst this method is more complicated to understand it reduces overall runtime complexity
                */

                display[r][c] = Symbol.getApplicable(r, c, display); //Sets the top left corner

                c++;
            }
            //At this point, all corner icons above and left of the most right icon will be filled
            //We are able to now fill the top right corner since this is the most right icon here.

            display[r][c+4] = Symbol.getApplicable(r, c+4, display);
            r++;
        }
        //At this point all icon corners except for the most bottom row will be filled.
        //We could add an if statement in the above for loop to check if it's at the bottom row, however
        //the below method is neater

        //Iterates at the bottom row for every corner piece.
        for(int h = 0; h < 1 + columns * 4; h += 4) {
            display[rows * 2][h] = Symbol.getApplicable(rows * 2, h, display);
        }

        return display;
    }

    /** Will update a singular icon in a maze at a specific location. This should be used to update keys and doors
     *   after player uses them
     * @param row The icon's row
     * @param column The icon's column
     * @param display The maze
     * @return The entire displayable maze, modified for a single icon
     */
    public char[][] updateIcon(int row, int column, char[][] display) {
        Icon icon = this.getIcon(row, column);
        char[][] symbol = icon.getSymbol();

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 5; j++) {
                display[row+i][column+j] = symbol[i][j];
            }
        }
        return display;
    }
}
