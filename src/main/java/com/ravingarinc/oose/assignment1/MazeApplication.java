package com.ravingarinc.oose.assignment1;

import com.ravingarinc.oose.assignment1.maze.Symbol;
import com.ravingarinc.oose.assignment1.util.IllegalMazeException;
import com.ravingarinc.oose.assignment1.util.Viewer;
import com.ravingarinc.oose.assignment1.util.MazeErrorException;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Maze;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MazeApplication {
    private static final Logger LOGGER = Logger.getLogger(MazeApplication.class.getName());

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
        catch(MazeErrorException | IllegalMazeException e) {
            logMessage(Level.SEVERE, e.getMessage());
        }
        return maze;
    }

    private static Viewer setupViewer(Maze maze) {
        Viewer viewer = null;
        if(maze != null) {
            try {
                viewer = new Viewer(maze);
            }
            catch(MazeErrorException e) {
                logMessage(Level.SEVERE, e.getMessage());
            }
        }
        return viewer;
    }

    private static void run(Maze maze, Viewer viewer) {
        viewer.display(maze);

        boolean running = true;
        while(running) {
            try {
                viewer.setMessage("Use W, S, A and D to move up, down, left or right! Try and reach the end '" + Symbol.END + "' symbol!");
                Direction input = Direction.getDirectionInput((char)System.in.read());
                if(input != null) {
                    maze.handlePlayerMove(input);
                }
            }
            catch(IOException e) {
                viewer.setMessage(Colour.RED + "Something went wrong getting input!" + Colour.BLANK);
                logMessage(Level.SEVERE, () -> "Encountered issue during runtime!" + Arrays.toString(e.getStackTrace()));
            }
            catch(IllegalStateException e) {
                viewer.setMessage(Colour.RED + "Something went wrong moving player! " + Colour.BLANK);
                logMessage(Level.SEVERE, () -> "Encountered issue during runtime!" + Arrays.toString(e.getStackTrace()));
            }

            viewer.display(maze);

            if(maze.finished()) {
                running = false;
            }
        }
    }

    public static void logMessage(Level level, String message) {
        LOGGER.log(level, message);
    }
    public static void logMessage(Level level, Supplier<String> message) {
        LOGGER.log(level, message);
    }
}
