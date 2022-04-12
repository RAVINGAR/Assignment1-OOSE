package com.ravingarinc.oose.assignment1.maze;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.character.Player;
import com.ravingarinc.oose.assignment1.io.MazeFormatException;
import com.ravingarinc.oose.assignment1.maze.icon.*;
import com.ravingarinc.oose.assignment1.maze.icon.decorations.*;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class Maze {
    private List<List<Icon>> grid;
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
        MazeReader reader = new MazeReader(filename);

        this.grid = new ArrayList<>();
        for(int r = 0; r < rows; r++) {
            this.grid.add(new ArrayList<>());
        }

        parseMazeData(reader.getData());

        reader.closeReader();

        iconsToUpdate = new LinkedList<>();
    }

    private void parseMazeData(Map<String, LinkedList<String>> data) throws MazeFormatException {
        /*
        - There should only be 1 start position. The player is created at the start position
        - End positions are abstracted icon's that have a different on move position
         */

        /*
        Maze must be added in this order

        new Key(new Message(new Door(new Wall(new Element))))

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

                            if(0 < r && r < rows && 0 < c && c < columns) {
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
    }

    public Player getPlayer() { return player; }

    @Nullable
    public Icon getIcon(int row, int column) {
        return 0 < row && row < rows && 0 < column && column < columns ? this.grid.get(row).get(column) : null;
    }

    public void updateMaze() {

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
        Icon next = getIcon(newPos.row(), newPos.column());
        if(next != null) { //If next is null, means that indicated position is out of bounds.
            Icon prev = getIcon(player.row(), player.column());
            if(prev.onMove(player, direction)) {
                iconsToUpdate.addLast(prev);
                if(next.onMove(player, direction.getOpposing())) {
                    iconsToUpdate.addLast(next);
                    //Since there will only be ONE door between two squares. Must check both that nothing is blocking on both
                    //sides
                    player.move(direction);
                }
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
        if(0 < row && row < rows && 0 < column && column < columns) { //Only will occur if in bounds.
            Icon existing = this.grid.get(row).get(column); //We do not call getIcon() here as that may return null if out of bounds.
            if(existing != null) {
                icon.setNext(existing);
            } //There is no need to setNext of icon to 'new Element()' as this is done by default.
            this.grid.get(row).add(column, icon);
        }
    }

    /**
     *
     * @return An unmodifiable grid
     */
    public List<List<Icon>> getGrid() {
        List<List<Icon>> unmodifiable = new ArrayList<>();
        for(List<Icon> row : grid) {
            unmodifiable.add(Collections.unmodifiableList(row));
        }
        return Collections.unmodifiableList(unmodifiable);
    }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }

    /*
    The reason this is a private inner class is to reduce the amount of parameters that need to be passed between methods
    Additionally, the goal is to only initialise the BufferedReader once, since it only needs to read the file once.

    As such, I have split different responsibilities into different methods. And to do this there needs to be one universal
    BufferedReader available.
    */
    private class MazeReader {
        private Map<String, LinkedList<String>> data;
        private BufferedReader reader;

        public MazeReader(String filename) throws MazeFormatException {
            if(filename != null && filename.isEmpty()) {
                throw new MazeFormatException("Filename cannot be null or empty!");
            }

            try {
               reader = new BufferedReader(new FileReader(filename));

            } catch (FileNotFoundException e) {
                throw new MazeFormatException("Could not find file with filename " + filename + "!");
            }

            init(); //Initialise rows and column. todo maybe move this to Maze somehow.
            data = initialiseMap();
            readMazeData();
        }

        /**
         * Checks for a valid first line. Will throw an
         * @throws MazeFormatException if it encounters an IO exception
         */
        private void init() throws MazeFormatException {
            String line = null;
            try {
                line = reader.readLine();
                if(line != null && !line.isEmpty()) {
                    String[] split = line.split(" ");
                    rows = Integer.parseInt(split[0]);
                    columns = Integer.parseInt(split[1]);
                }
                else {
                    throw new MazeFormatException("Initial line had incorrect format! First line read; \n" + line);
                }
            }
            catch(IOException e) {
                throw new MazeFormatException("Encountered IOException! " + e);
            }
            catch(NumberFormatException e) {
                throw new MazeFormatException("Initial line used incorrect syntax for rows and columns! First line read; \n" + line);
            }
        }

        private void readMazeData() {
            try {
                reader.readLine();
                String line = reader.readLine();

                while(line != null) {
                    try {
                        if(!line.isEmpty()) {
                            String[] split = line.split(" ", 2);
                            addData(split[0], split[1]);
                        }
                        else {
                            MazeApplication.log(Level.WARNING, "Line was empty! Skipping..");
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        //This is to avoid having to unnecessarily check for valid lengths in the above.
                        MazeApplication.log(Level.WARNING, "Encountered syntax error at; \n'" + line + "'\n Skipping..");
                    }

                    line = reader.readLine();
                }
            }
            catch(IOException e) {
                MazeApplication.log(Level.SEVERE, "Encountered IOException! " + e);
            }
        }

        private Map<String, LinkedList<String>> getData() {
            return this.data;
        }

        private void addData(String key, String data) {
            LinkedList<String> list = this.data.get(key);
            if(list == null) {
                MazeApplication.log(Level.WARNING, "Line had invalid key of '" + key + "'! Skipping..");
            }
            else {
                list.add(data);
            }
        }

        private Map<String, LinkedList<String>> initialiseMap() {
            Map<String, LinkedList<String>> map = new LinkedHashMap<>(); //Using a LinkedHashMap to maintain insertion order.

            String[] keys = {"S", "E", "K", "M", "WH", "WV", "DH", "DV"};
            for(String k : keys) {
                map.put(k, new LinkedList<>());
            }
            return map;
        }

        /**
         * Attempts to close the buffered reader initialised in this class
         */
        public void closeReader() {
            try {
                this.reader.close();
                reader = null;
            }
            catch(IOException e) {
                MazeApplication.log(Level.WARNING, "Failed to close BufferedReader in class due to IOException; \n" + e);
            }
        }
    }
}
