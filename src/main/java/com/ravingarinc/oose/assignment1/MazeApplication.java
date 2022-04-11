package com.ravingarinc.oose.assignment1;

import com.ravingarinc.oose.assignment1.display.Viewer;
import com.ravingarinc.oose.assignment1.io.MazeFormatException;
import com.ravingarinc.oose.assignment1.maze.Maze;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MazeApplication {
    private static final Logger logger = Logger.getLogger(MazeApplication.class.getName());

    public static void main(String[] args) {
        final Maze maze = setupMaze();

        final Viewer viewer = setupViewer(maze);
    }

    private static Maze setupMaze() {
        Maze maze;
        try {
            /*
            The reason that MazeReader is used as an object rather than as a static utility class is due to a number of
            reasons; mainly reason is to reduce the amount of parameters that need to be passed between multiple method calls.
            This is further explained in the MazeReader class itself.
             */
            maze = new Maze("maze.txt");

        }
        catch(MazeFormatException e) {
            log(Level.SEVERE, e.getMessage());
            maze = null;
        }
        return maze;
    }

    private static Viewer setupViewer(Maze maze) {
        return new Viewer(maze);
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}
