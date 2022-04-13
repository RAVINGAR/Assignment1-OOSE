package com.ravingarinc.oose.assignment1.util;

import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Maze;
import com.ravingarinc.oose.assignment1.maze.Position;
import com.ravingarinc.oose.assignment1.maze.Symbol;
import com.ravingarinc.oose.assignment1.maze.icon.Icon;

/**
 * Class responsible for providing a display image to the user and printing to terminal.
 */
public class Viewer {
    private String[][] display;
    private String message;

    public Viewer(Maze maze) throws MazeErrorException {
        init(maze);
    }

    /**
     * Transforms maze into a displayable format in terms of String values.
     */
    public final void init(Maze maze) throws MazeErrorException {
        int rows = maze.getRows();
        int columns = maze.getColumns();
        display = new String[1 + rows * 2][1 + columns * 4];

        Icon[][] grid = maze.getGrid();

        try {
            int r, c, displayRow, displayColumn = 0;
            for(r = 0; r < rows; r++) {
                displayRow = r * 2;
                for (c = 0; c < columns; c++) {
                    displayColumn = c * 4;
                    String[][] symbol = grid[r][c].getSymbol();
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 5; j++) {
                            display[displayRow + i][displayColumn + j] = symbol[i][j];
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

                    display[displayRow][displayColumn] = Symbol.getApplicable(displayRow, displayColumn, display); //Sets the top left corner
                }
                //At this point, all corner icons above and left of the most right icon will be filled
                //We are able to now fill the top right corner since this is the most right icon here.
                display[displayRow][displayColumn + 4] = Symbol.getApplicable(displayRow, displayColumn + 4, display);
            }
            //At this point all icon corners except for the most bottom row will be filled.
            //We could add an if statement in the above for loop to check if it's at the bottom row, however
            //the below method is neater

            //Iterates at the bottom row for every corner piece.
            for(int h = 0; h < 1 + columns * 4; h += 4) {
                display[rows * 2][h] = Symbol.getApplicable(rows * 2, h, display);
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new MazeErrorException("Accessed array element of display outside of bounds! At " + e, e);
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void update(Maze maze) {
        Player player = maze.getPlayer();
        message = player.getMessage();

        Icon icon = maze.getNextIconToUpdate();
        while(icon != null) {
            updateIcon(icon);
            icon = maze.getNextIconToUpdate();
        }

        display[player.row() * 2 + 1][player.column() * 4 + 2] = player.getSymbol();
    }

    /** Will update a singular icon in the display maze at a specific location. This should be used to update keys and doors
     *   after player uses them
     * @param icon The icon to be updated
     */
    public void updateIcon(Icon icon) {
        Position pos = icon.getPos();
        String[][] symbol = icon.getSymbol();

        int row = pos.row();
        int column = pos.column();

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 5; j++) {
                if(!((i == 0 && (j == 0 || j == 4)) || (i == 2 && (j == 0 || j == 4)))) {
                    //The above if statement makes sure that symbols aren't placed in the corners.
                    display[row * 2 + i][column * 4 + j] = symbol[i][j];
                }
            }
        }
    }

    public void display(Maze maze) {
        update(maze);

        System.out.print(Colour.GREEN + "== The Untitled Maze Game ==" + Colour.BLANK + "\n");
        for(int r = 0; r < maze.getRows() * 2 + 1; r++) {
            for(int c = 0; c < maze.getColumns() * 4 + 1; c++) {
                System.out.print(display[r][c]);
            }
            System.out.print("\n");
        }
        System.out.println(message);
    }
}
