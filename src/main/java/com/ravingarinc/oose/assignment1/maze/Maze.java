package com.ravingarinc.oose.assignment1.maze;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.io.MazeFormatException;
import com.ravingarinc.oose.assignment1.io.MazeReader;
import com.ravingarinc.oose.assignment1.maze.icon.*;
import com.ravingarinc.oose.assignment1.maze.icon.decorations.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;

public class Maze {
    private Icon[][] grid;
    private int rows, columns;
    private Player player = null;
    private LinkedList<Icon> iconsToUpdate;
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

    public Maze(String filename) throws MazeFormatException {
        //We use an object for the reader so that only ONE BufferedReader needs to created to read the entire file.
        MazeReader reader = new MazeReader(filename);

        parseInitialData(reader.getFirstLine());

        this.grid = new Icon[rows][columns];
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < columns; c++) {
                grid[r][c] = null;
            }
        }

        parseMazeData(reader.getData());
        reader.closeReader();

        iconsToUpdate = new LinkedList<>();
    }

    private void parseInitialData(String firstLine) throws MazeFormatException {
        try {
            String[] split = firstLine.split(" ");
            rows = Integer.parseInt(split[0]);
            columns = Integer.parseInt(split[1]);
        }
        catch(NumberFormatException e) {
            throw new MazeFormatException("Initial line used incorrect syntax for rows and columns! First line read; \n" + firstLine);
        }
    }

    private void parseMazeData(Map<String, LinkedList<String>> data) throws MazeFormatException {
        /*
        new Key(new Message(new Door(new Wall(new Element))))

        Which method is called first? Of these


        */

        //The order of it is new Label(new Caption(new ImageData);
        //Since each description is this + next
        //Order goes Label + Caption + Image
        //For cat example
        /*
        Order of info Caption: GPS: Rating

        How it prints is Rating: GPS: Caption

        This means it went new Label(new Label(new Caption(new ImageData

        Meaning that the FIRST label class is called first
        As such. In this case,
        Each of the decorations MUST check if next can occur BEFORE they execute their own functions :)
        */

        //"S", "E", "K", "WH", "WV", "DH", "DV"
        //Fixme there is alot of repeating code here. How can it be condensed?
        for(Map.Entry<String, LinkedList<String>> key : data.entrySet()) {
            LinkedList<String> list = key.getValue();
            String parsedKey = key.getKey();
            switch(parsedKey) {
                case "S" -> {
                    while(player == null && !list.isEmpty()) {
                        String[] split = list.removeFirst().split(" ", 3); //Limit of 3 to prevent parsing issues
                        try {
                            int r = Integer.parseInt(split[0]);
                            int c = Integer.parseInt(split[1]);

                            if(0 <= r && r < rows && 0 <= c && c < columns) {
                                player = new Player(r, c);
                            }
                        }
                        catch(NumberFormatException e) {
                            MazeApplication.log(Level.WARNING, "Invalid start location for player, looking for another..");
                        }
                        catch(ArrayIndexOutOfBoundsException e) {
                            MazeApplication.log(Level.WARNING, "Invalid amount of arguments! Skipping..");
                        }
                    }

                    if(player == null) {
                        throw new MazeFormatException("Invalid maze file! No VALID start locations were specified!");
                    }
                }
                case "E" -> {
                    list.forEach(element -> {
                        String[] split = element.split(" ", 3);
                        try {
                            int r = Integer.parseInt(split[0]);
                            int c = Integer.parseInt(split[1]);
                            this.putIcon(r, c, new End(r, c));
                        }
                        catch(NumberFormatException e) {
                            MazeApplication.log(Level.WARNING, "Invalid location for element! Skipping..");
                        }
                        catch(ArrayIndexOutOfBoundsException e) {
                            MazeApplication.log(Level.WARNING, "Invalid amount of arguments! Skipping..");
                        }
                    });
                }
                case "K" -> {
                    list.forEach(element -> {
                        String[] split = element.split(" ", 3);
                        try {
                            int r = Integer.parseInt(split[0]);
                            int c = Integer.parseInt(split[1]);
                            this.putIcon(r, c, new Key(Colour.matchColour(Integer.parseInt(split[2])), r, c));
                        }
                        catch(NumberFormatException e) {
                            MazeApplication.log(Level.WARNING, "Invalid location for element! Skipping..");
                        }
                        catch(ArrayIndexOutOfBoundsException e) {
                            MazeApplication.log(Level.WARNING, "Key had no specified colour! Skipping..");
                        }
                    });
                }
                case "M" -> {
                    list.forEach(element -> {
                        String[] split = element.split(" ", 3);
                        try {
                            int r = Integer.parseInt(split[0]);
                            int c = Integer.parseInt(split[1]);
                            this.putIcon(r, c, new Message(split[2], r, c));
                        }
                        catch(NumberFormatException e) {
                            MazeApplication.log(Level.WARNING, "Invalid location for element! Skipping..");
                        }
                        catch(ArrayIndexOutOfBoundsException e) {
                            MazeApplication.log(Level.WARNING, "Message had no message! Skipping..");
                        }
                    });
                }
                case "WH", "WV" -> {
                    list.forEach(element -> {
                        String[] split = element.split(" ", 3);
                        try {
                            int r = Integer.parseInt(split[0]);
                            int c = Integer.parseInt(split[1]);
                            if(parsedKey.equals("WH")) { //If Horizontal
                                this.putIcon(r, c, new Wall(Direction.UP, r, c));
                                this.putIcon(r+1, c, new Wall(Direction.DOWN, r, c));
                            }
                            else { //If Vertical
                                this.putIcon(r, c, new Wall(Direction.LEFT, r, c));
                                this.putIcon(r, c-1, new Wall(Direction.RIGHT, r, c));
                            }

                        }
                        catch(NumberFormatException e) {
                            MazeApplication.log(Level.WARNING, "Invalid location for element! Skipping..");
                        }
                        catch(ArrayIndexOutOfBoundsException e) {
                            MazeApplication.log(Level.WARNING, "Message had no message! Skipping..");
                        }
                    });
                }
                case "DH", "DV" -> {
                    list.forEach(element -> {
                        String[] split = element.split(" ", 3);
                        try {
                            int r = Integer.parseInt(split[0]);
                            int c = Integer.parseInt(split[1]);
                            Colour colour = Colour.matchColour(Integer.parseInt(split[2]));
                            if(parsedKey.equals("DH")) { //If Horizontal
                                this.putIcon(r, c, new Door(Direction.UP, colour, r, c));
                            }
                            else { //If Vertical
                                this.putIcon(r, c, new Door(Direction.LEFT, colour, r, c));
                            }

                        }
                        catch(NumberFormatException e) {
                            MazeApplication.log(Level.WARNING, "Invalid location for element! Skipping..");
                        }
                        catch(ArrayIndexOutOfBoundsException e) {
                            MazeApplication.log(Level.WARNING, "Door had no colour! Skipping..");
                        }
                    });
                }
                default -> {
                    MazeApplication.log(Level.SEVERE, "Found maze data element that should NOT be present. Skipping..");
                }
            }
        }

        fillEmptySpaces();
        fillBorderWalls();
    }

    private void fillEmptySpaces() {
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < columns; c++) {
                if(this.grid[r][c] == null) {
                    grid[r][c] = new Element(r, c);
                }
            }
        }
    }

    private void fillBorderWalls() {
        for(int c = 0; c < columns; c++) {
            Icon topWall = new Wall(Direction.UP, 0, c);
            topWall.setNext(grid[0][c]);
            grid[0][c] = topWall;

            int bottom = rows-1;
            Icon bottomWall = new Wall(Direction.DOWN, bottom, c);
            bottomWall.setNext(grid[bottom][c]);
            grid[bottom][c] = bottomWall;
        }

        for(int r = 0; r < rows; r++) {
            Icon leftWall = new Wall(Direction.LEFT, r, 0);
            leftWall.setNext(grid[r][0]);
            grid[r][0] = leftWall;

            int right = columns-1;
            Icon rightWall = new Wall(Direction.RIGHT, r, right);
            rightWall.setNext(grid[r][right]);
            grid[r][right] = rightWall;
        }
    }

    public Player getPlayer() { return player; }

    @Nullable
    public Icon getIcon(int row, int column) {
        return 0 <= row && row < rows && 0 <= column && column < columns ? this.grid[row][column] : null;
    }

    public void handlePlayerMove(Direction direction) {
        Position newPos = new Position(player.row(), player.column());
        newPos.move(direction);

        /*
        Let's say player is in square 2,3

        There is a Door NORTH of 2,3. Hence it is also blocking SOUTH of 1,3
        When player moves into a square it should run next.onMove ON THAT SQUARE, however should do so in opposite direction
        (since player is moving between southern barrier of the next square)
        onMove should also be called on the player's current square before they move in their intended direction.
         */
        Icon prev = getIcon(player.row(), player.column());
        if(prev.onMoveFrom(player, direction)) {
            iconsToUpdate.addLast(prev);
            Icon next = getIcon(newPos.row(), newPos.column());
            //Next should not be null since if prev was a boundary, there SHOULD be a wall there.
            if(next.onMoveTo(player, direction.getOpposing())) {
                iconsToUpdate.addLast(next);
                //Since there will only be ONE door between two squares. Must check both that nothing is blocking on both
                //sides
                player.move(direction);
            }
        }
    }

    @Nullable
    public Icon getNextIconToUpdate() {
        return iconsToUpdate.isEmpty() ? null : iconsToUpdate.removeFirst();
    }

    /**
     * Appends to the current Icon at a location or creates a new one if it doesn't exists
     * @param row y axis
     * @param column x axis
     * @param icon icon to append
     */
    public void putIcon(int row, int column, Icon icon) {
        if(0 <= row && row < rows && 0 <= column && column < columns) { //Only will occur if in bounds.

            Icon existing = grid[row][column];
            if(existing != null) {
                icon.setNext(existing);
            }
            //There is no need to setNext of not-existing icon to 'new Element()' as this is done by default.

            grid[row][column] = icon;
        }
    }

    /**
     *
     * @return An unmodifiable grid
     */
    public Icon[][] getGrid() {
        return grid;
    }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }
}
