package com.ravingarinc.oose.assignment1.display;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Maze;
import com.ravingarinc.oose.assignment1.maze.Position;
import com.ravingarinc.oose.assignment1.maze.Symbol;
import com.ravingarinc.oose.assignment1.maze.icon.Icon;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Class responsible for providing a display image to the user and printing to terminal.
 */
public class Viewer {
    private String[][] display;
    private String message;
    private LinkedList<Icon> iconsToUpdate;

    public Viewer(Maze maze) {
        message = "Use W, S, A and D to move up, down, left or right! Try and reach the end '" + Symbol.END + "' symbol!";
        initialiseDisplay(maze);

        iconsToUpdate = new LinkedList<>();
    }

    /**
     * Transforms maze into a displayable format in terms of String values.
     */
    public void initialiseDisplay(Maze maze) {
        int rows = maze.getRows();
        int columns = maze.getColumns();
        display = new String[1 + rows * 2][1 + columns * 4];

        List<List<Icon>> grid = maze.getGrid();

        int r = 0, c;
        for(List<Icon> row : grid) {
            c = 0;
            for(Icon icon : row) {
                String[][] symbol = icon.getSymbol();
                if(symbol.length != 3 || symbol[0].length != 5) {
                    MazeApplication.log(Level.SEVERE, "Got invalid symbol from icon at row " + r + ", column " + c);
                    continue;
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
    }

    public void update(Maze maze) {
        iconsToUpdate.forEach(this::updateIcon);

        //Also update message here. And player movements
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
                display[row+i][column+j] = symbol[i][j];
            }
        }
    }

    public void display() {
        System.out.print(Colour.GREEN + "== The Untitled Maze Game ==\n");
        for(String[] row : display) {
            for(String i : row) {
                System.out.print(i);
            }
            System.out.print("\n");
        }
    }
}
