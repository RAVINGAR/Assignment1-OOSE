package com.ravingarinc.oose.assignment1.util;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.util.MazeFormatException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;

public class MazeReader {
    private BufferedReader reader;

    public MazeReader(String filename) throws MazeFormatException {
        if(filename == null || filename.isEmpty()) {
            throw new MazeFormatException("Filename cannot be null or empty!", MazeReader.class.getName(), 20);
        }
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new MazeFormatException("Could not find file with filename " + filename + "!", e);
        }
    }

    /**
     * Checks for a valid first line. Will throw an
     * @throws MazeFormatException if it encounters an IO exception
     */
    public String getFirstLine() throws MazeFormatException {
        String line;
        try {
            line = reader.readLine();
            if(line == null || line.isEmpty() || line.split(" ").length != 2) {
                throw new MazeFormatException("Initial line had incorrect format! First line read; \n" + line, MazeReader.class.getName(), 38);
            }
        }
        catch(IOException e) {
            throw new MazeFormatException("Encountered IOException! ", e);
        }
        return line;
    }

    public Map<String, LinkedList<String>> getData() {
        Map<String, LinkedList<String>> data = initialiseMap();

        try {
            reader.readLine();
            String line = reader.readLine();

            while(line != null) {
                try {
                    if(!line.isEmpty()) {
                        String[] split = line.split(" ", 2);

                        LinkedList<String> list = data.get(split[0]);
                        if(list == null) {
                            MazeApplication.log(Level.WARNING, "Line had invalid key of '" + split[0] + "'! Skipping..");
                        }
                        else {
                            list.add(split[1]);
                        }
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

        return data;
    }

    private Map<String, LinkedList<String>> initialiseMap() {
        Map<String, LinkedList<String>> map = new LinkedHashMap<>(); //Using a LinkedHashMap to maintain insertion order.

        String[] keys = {"S", "E", "WV", "WH", "DH", "DV", "M", "K"};
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
