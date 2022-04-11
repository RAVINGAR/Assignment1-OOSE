package com.ravingarinc.oose.assignment1.io;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.exception.MazeFormatException;
import com.ravingarinc.oose.assignment1.maze.Maze;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Utility class responsible for loading maze data onto a Maze object
 */
public class MazeReader {
    private Map<String, List<List<String>>> mazeData;
    private Maze maze;
    private int rows;
    private int columns;

    public MazeReader(String filename) throws MazeFormatException {
        if(filename.isEmpty()) {
            throw new MazeFormatException("Filename cannot be empty or null!");
        }
        init(filename);

        mazeData = initialiseMap(rows);

        loadMazeData(filename);
    }

    private void init(String filename) throws MazeFormatException {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if(line != null && !line.isEmpty()) {
                String[] split = line.split(" ");
                rows = Integer.parseInt(split[0]);
                columns = Integer.parseInt(split[1]);
                maze = new Maze(rows, columns);
            }
        }
        catch(FileNotFoundException e) {
            MazeApplication.log(Level.SEVERE, "Could not find file with filename " + filename + "!");
            throw new MazeFormatException("Could not load maze!");
        }
        catch(IOException e) {
            MazeApplication.log(Level.SEVERE, "Encountered IOException! " + e);
            throw new MazeFormatException("Could not load maze!");
        }
    }

    private void loadMazeData(String filename) {
        String line = null;
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine();
            line = reader.readLine();

            while(line != null) {
                try {
                    if(!line.isEmpty()) {
                        String[] split = line.split(" ", 3);
                        addData(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), line);
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    //This is to avoid having to unnecessarily check for valid lengths in the above.
                    MazeApplication.log(Level.SEVERE, "Encountered array out of bounds exception at line; \n'" + line + "'\n This is a syntax error!");
                }
                catch(NumberFormatException e ) {
                    MazeApplication.log(Level.SEVERE, "Could not format string correctly at line; \n'" + line + "'\n This is most likely a syntax error!");
                }

                line = reader.readLine();
            }
        }
        catch(FileNotFoundException e) {
            MazeApplication.log(Level.SEVERE, "Could not find file with filename " + filename + "!");
        }
        catch(IOException e) {
            MazeApplication.log(Level.SEVERE, "Encountered IOException! " + e);
        }
    }

    private void addData(String key, int row, int column, String data) {
        mazeData.get(key).get(row).add(column, data);
    }

    private Map<String, List<List<String>>> initialiseMap(int rows) {
        Map<String, List<List<String>>> map = new HashMap<>();

        String[] keys = {"S", "E", "K", "WH", "WV", "DH", "DV"};
        for(String k : keys) {
            List<List<String>> list = new ArrayList<>();
            for(int r = 0; r < rows; r++) {
                list.add(new ArrayList<>());
            }
            map.put(k, list);
        }
        return map;
    }

    /*
    Maze must be added in this order

    new Key(new Message(new Door(new Wall(new Element))))

     */

    private class MazeElement {

    }
}
