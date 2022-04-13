package com.ravingarinc.oose.assignment1;

import com.ravingarinc.oose.assignment1.util.Viewer;
import com.ravingarinc.oose.assignment1.util.ViewerFormatError;
import com.ravingarinc.oose.assignment1.util.MazeFormatException;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Maze;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MazeApplication {
    private static final Logger Log = Logger.getLogger(MazeApplication.class.getName());

    public static void main(String[] args) {
        final Maze maze = setupMaze();
        final Viewer viewer = setupViewer(maze);

        if(maze == null || viewer == null) {
            System.out.println("Encountered errors on load. Program will now exit.");
        }
        else {
            run(maze, viewer);
        }
    }

    private static Maze setupMaze() {
        Maze maze = null;
        try {
            maze = new Maze("maze.txt");
        }
        catch(MazeFormatException e) {
            log(Level.SEVERE, e.getMessage());
        }
        return maze;
    }

    private static Viewer setupViewer(Maze maze) {
        Viewer viewer = null;
        if(maze != null) {
            try {
                viewer = new Viewer(maze);
            }
            catch(ViewerFormatError e) {
                log(Level.SEVERE, e.getMessage());
            }
        }
        return viewer;
    }

    private static void run(Maze maze, Viewer viewer) {
        viewer.display(maze);

        boolean running = true;
        while(running) {
            try {
                Direction input = Direction.getDirectionInput((char)System.in.read());
                if(input != null) {
                    maze.handlePlayerMove(input);
                }
            }
            catch(IOException e) {
                viewer.setMessage(Colour.RED + "Something went wrong getting input!" + e + Colour.BLANK);
                Log.severe("Encountered issue during runtime!" + Arrays.toString(e.getStackTrace()));
            }
            catch(IllegalStateException e) {
                viewer.setMessage(Colour.RED + "Something went wrong moving player! " + e + Colour.BLANK);
                Log.severe("Encountered issue during runtime!" + Arrays.toString(e.getStackTrace()));
            }

            viewer.display(maze);

            if(maze.finished()) {
                running = false;
            }
        }
    }

    public static void log(Level level, String message) {
        Log.log(level, message);
    }
}
