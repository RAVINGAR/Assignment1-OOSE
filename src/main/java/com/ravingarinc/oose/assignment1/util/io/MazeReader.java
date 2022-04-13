package com.ravingarinc.oose.assignment1.util.io;

import com.ravingarinc.oose.assignment1.MazeApplication;
import com.ravingarinc.oose.assignment1.util.IllegalMazeException;
import com.ravingarinc.oose.assignment1.util.MazeErrorException;

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

    //private Map<String, Class<? extends Icon>> types;

    public MazeReader(String filename) throws MazeErrorException, IllegalMazeException {
        if(filename == null || filename.isEmpty()) {
            throw new IllegalMazeException("Filename cannot be null or empty!", MazeReader.class.getName(), 20);
        }
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new MazeErrorException("Could not find file with filename " + filename + "!", e);
        }
    }

    /**
     * Checks for a valid first line, then returns it.
     * @throws MazeErrorException if it encounters an IO exception
     * @throws IllegalMazeException if initial line has incorrect format
     */
    public String getFirstLine() throws MazeErrorException, IllegalMazeException {
        String line;
        try {
            line = reader.readLine();
            if(line == null || line.isEmpty() || line.split(" ").length != 2) {
                throw new IllegalMazeException("Initial line had incorrect format! First line read; \n" + line, MazeReader.class.getName(), 38);
            }
        }
        catch(IOException e) {
            throw new MazeErrorException("Encountered IOException! ", e);
        }
        return line;
    }

    public Map<String, LinkedList<String>> getData() throws MazeErrorException {
        Map<String, LinkedList<String>> data = initialiseMap();

        try {
            reader.readLine();
            String line = reader.readLine();

            while(line != null) {
                try {
                    if(line.isEmpty()) {
                        MazeApplication.logMessage(Level.WARNING, "Line was empty! Skipping..");
                    }
                    else {
                        String[] split = line.split(" ", 2);

                        LinkedList<String> list = data.get(split[0]);
                        if(list == null) {
                            MazeApplication.logMessage(Level.WARNING, () -> "Line had invalid key of '" + split[0] + "'! Skipping..");
                        }
                        else {
                            list.add(split[1]);
                        }
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    //This is to avoid having to unnecessarily check for valid lengths in the above.
                    final String finalLine = line;
                    MazeApplication.logMessage(Level.WARNING, () -> "Encountered syntax error at; \n'" + finalLine + "'\n Skipping..");
                }
                line = reader.readLine();
            }
        }
        catch(IOException e) {
            throw new MazeErrorException("Encountered I/O exception in reading maze data.", e);
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
            MazeApplication.logMessage(Level.SEVERE, () -> "Failed to close BufferedReader in class due to IOException; \n" + e);
        }
    }
}
