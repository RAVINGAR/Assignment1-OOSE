package com.ravingarinc.oose.assignment1.maze;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.util.IllegalMazeException;
import com.ravingarinc.oose.assignment1.util.MazeErrorException;
import com.ravingarinc.oose.assignment1.util.io.MazeReader;
import com.ravingarinc.oose.assignment1.maze.icon.*;
import com.ravingarinc.oose.assignment1.maze.icon.decorations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;

public class Maze {
    private Icon[][] grid;
    private int rows, columns;
    private Player player = null;
    private final List<Icon> iconsToUpdate;

    public Maze(@NotNull String filename) throws MazeErrorException, IllegalMazeException {
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

    private void parseInitialData(String firstLine) throws MazeErrorException {
        try {
            String[] split = firstLine.split(" ");
            rows = Integer.parseInt(split[0]);
            columns = Integer.parseInt(split[1]);
        }
        catch(NumberFormatException e) {
            throw new MazeErrorException("Initial line used incorrect syntax for rows and columns! First line read; \n" + firstLine, e);
        }
    }

    private void parseMazeData(Map<String, LinkedList<String>> data) throws IllegalMazeException {
        //"S", "E", "K", "WH", "WV", "DH", "DV"
        /*
        I am aware of the amount of repeating code here. This method is only called once to initially load data from the
        maze file.
        My only other alternative to reduce the amount of data here was to create a container object that would
        store the Class<? extends Additive> types stored in a map corresponding to the icon keys (S, E, K etc)
        Then use reflection to call a constructor and from that object you would get an object which could then be put
        on the map.
        However I'm unsure if I'm ALLOWED to use reflection to do such a thing, additionally, the below code works and
        I don't have much time left.
         */
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
                            MazeApplication.logMessage(Level.WARNING, "Invalid start location for player, looking for another..");
                        }
                        catch(ArrayIndexOutOfBoundsException e) {
                            MazeApplication.logMessage(Level.WARNING, "Invalid amount of arguments! Skipping..");
                        }
                    }

                    if(player == null) {
                        throw new IllegalMazeException("Invalid maze file! No VALID start locations were specified!", Maze.class.getName(), 121);
                    }
                    else if(!list.isEmpty()) {
                        MazeApplication.logMessage(Level.WARNING, "More than one start location was specified! Using the first one..");
                    }
                }
                case "E" -> list.forEach(element -> {
                    String[] split = element.split(" ", 3);
                    try {
                        int r = Integer.parseInt(split[0]);
                        int c = Integer.parseInt(split[1]);
                        if(0 <= r && r < rows && 0 <= c && c < columns) {
                            this.putIcon(r, c, new End(r, c));
                        }
                        else {
                            MazeApplication.logMessage(Level.WARNING, "Specified END for maze was out of bounds! This maze may not be completable.");
                        }

                    }
                    catch(NumberFormatException e) {
                        MazeApplication.logMessage(Level.WARNING, "Invalid location for element! Skipping..");
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        MazeApplication.logMessage(Level.WARNING, "Invalid amount of arguments! Skipping..");
                    }
                });
                case "K" -> list.forEach(element -> {
                    String[] split = element.split(" ", 3);
                    try {
                        int r = Integer.parseInt(split[0]);
                        int c = Integer.parseInt(split[1]);
                        this.putIcon(r, c, new Key(Colour.matchColour(Integer.parseInt(split[2])), r, c));
                    }
                    catch(NumberFormatException e) {
                        MazeApplication.logMessage(Level.WARNING, "Invalid location for element! Skipping..");
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        MazeApplication.logMessage(Level.WARNING, "Key had no specified colour! Skipping..");
                    }
                });
                case "M" -> list.forEach(element -> {
                    String[] split = element.split(" ", 3);
                    try {
                        int r = Integer.parseInt(split[0]);
                        int c = Integer.parseInt(split[1]);
                        this.putIcon(r, c, new Message(split[2], r, c));
                    }
                    catch(NumberFormatException e) {
                        MazeApplication.logMessage(Level.WARNING, "Invalid location for element! Skipping..");
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        MazeApplication.logMessage(Level.WARNING, "Message had no message! Skipping..");
                    }
                });
                case "WH", "WV" -> list.forEach(element -> {
                    String[] split = element.split(" ", 3);
                    try {
                        int r = Integer.parseInt(split[0]);
                        int c = Integer.parseInt(split[1]);
                        if(parsedKey.equals("WH")) { //If Horizontal
                            this.putIcon(r, c, new Wall(Direction.UP, r, c));
                            this.putIcon(r-1, c, new Wall(Direction.DOWN, r-1, c));
                        }
                        else { //If Vertical
                            this.putIcon(r, c, new Wall(Direction.LEFT, r, c));
                            this.putIcon(r, c-1, new Wall(Direction.RIGHT, r, c-1));
                        }

                    }
                    catch(NumberFormatException e) {
                        MazeApplication.logMessage(Level.WARNING, "Invalid location for element! Skipping..");
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        MazeApplication.logMessage(Level.WARNING, "Message had no message! Skipping..");
                    }
                });
                case "DH", "DV" -> list.forEach(element -> {
                    String[] split = element.split(" ", 3);
                    try {
                        int r = Integer.parseInt(split[0]);
                        int c = Integer.parseInt(split[1]);
                        Colour colour = Colour.matchColour(Integer.parseInt(split[2]));
                        if(parsedKey.equals("DH")) { //If Horizontal
                            Door door = new Door(Direction.UP, colour, r, c);
                            this.putIcon(r, c, door);
                            this.putIcon(r-1, c, new FakeDoor(door, r-1, c));
                        }
                        else { //If Vertical
                            Door door = new Door(Direction.LEFT, colour, r, c);
                            this.putIcon(r, c, door);
                            this.putIcon(r, c-1, new FakeDoor(door, r, c-1));
                        }

                    }
                    catch(NumberFormatException e) {
                        MazeApplication.logMessage(Level.WARNING, "Invalid location for element! Skipping..");
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        MazeApplication.logMessage(Level.WARNING, "Door had no colour! Skipping..");
                    }
                });
                default -> MazeApplication.logMessage(Level.SEVERE, "Found maze data element that should NOT be present. Skipping..");
            }
        }

        fillEmptySpaces();
        fillBorderWalls();
    }

    public boolean finished() { return player.isFinished(); }

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
        try {
            return this.grid[row][column];
        }
        catch(IndexOutOfBoundsException ignored) { //Feel like this is quicker than comparing rows and columns manually.
            return null;
        }
    }

    public void handleInitialPlayerPosition() throws IllegalMazeException {
        Icon current = getIcon(player.row(), player.column());
        if(current == null) {
            throw new IllegalMazeException("Player's current icon was null but it shouldn't be!?", Maze.class.getName(), 225);
        }

        //Since moving to an icon is based on moving to/from it will ALWAYS check if there is a wall or door
        //Since we want the player to start on this position, the onMoveTo must be called, however it has to ignore
        //any walls or doors. The solution was then to add a null check on the direction. If direction is null,
        //then no wall/door checks should have to be made
        current.onMoveTo(player, null);
        iconsToUpdate.add(current);
    }

    public void handlePlayerMove(Direction direction) throws IllegalMazeException {
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
        if(prev == null) {
            throw new IllegalMazeException("Player's current icon was null but it shouldn't be!?", Maze.class.getName(), 235);
        }
        if(prev.onMoveFrom(player, direction)) {
            iconsToUpdate.add(prev);
            Icon next = getIcon(newPos.row(), newPos.column());
            //Next should not be null since if prev was a boundary, there SHOULD be a wall there.
            if(next == null) {
                throw new IllegalMazeException("Player's next icon was null but it shouldn't be!?", Maze.class.getName(), 242);
            }
            if(next.onMoveTo(player, direction.getOpposing())) {
                iconsToUpdate.add(next);
                //Since there will only be ONE door between two squares. Must check both that nothing is blocking on both
                //sides
                player.move(direction);
            }
        }
    }

    @Nullable
    public Icon getNextIconToUpdate() {
        return iconsToUpdate.isEmpty() ? null : iconsToUpdate.remove(0);
    }

    /**
     * Appends to the current Icon at a location or creates a new one if it doesn't exists
     * @param row y axis
     * @param column x axis
     * @param icon icon to append
     */
    private void putIcon(int row, int column, Icon icon) {
        if(0 <= row && row < rows && 0 <= column && column < columns) { //Only will occur if in bounds.
            Icon existing = grid[row][column];
            if(existing != null) {
                icon.setNext(existing);
            }
            //There is no need to setNext of not-existing icon to 'new Element()' as this is done by default.

            grid[row][column] = icon;
        }
        else {
            MazeApplication.logMessage(Level.WARNING, "Attempted to place icon at [" + row + "," + column + "] but that location is out of bounds! Ignoring icon now..");
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
