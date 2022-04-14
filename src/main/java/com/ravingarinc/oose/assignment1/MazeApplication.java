package com.ravingarinc.oose.assignment1;

import com.ravingarinc.oose.assignment1.maze.Symbol;
import com.ravingarinc.oose.assignment1.util.IllegalMazeException;
import com.ravingarinc.oose.assignment1.util.Viewer;
import com.ravingarinc.oose.assignment1.util.MazeErrorException;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Maze;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MazeApplication {
    private static final Logger LOGGER = Logger.getLogger(MazeApplication.class.getName());

    public static void main(String[] args) {

        System.out.println("Welcome to the Untitled Maze Game!");
        String filename = getFilename();

        final Maze maze = setupMaze(filename);
        final Viewer viewer = setupViewer(maze);

        if(maze == null || viewer == null) {
            System.out.println("Encountered errors on load. Program will now exit.");
        }
        else {
            run(maze, viewer);
        }
    }

    @NotNull
    private static String getFilename() {
        Scanner sc = new Scanner(System.in);
        String filename = null;
        while(filename == null) {
            System.out.print("Please enter a valid filename: ");
            try {
                filename = sc.next();
                if(filename.isEmpty()) {
                    throw new NoSuchElementException("Filename was empty!");
                }
            }
            catch(NoSuchElementException e) {
                System.out.println("Invalid input! Try again!");
                logMessage(Level.INFO, e.getMessage());
                sc.next();
            }
        }

        sc.close();
        return filename;
    }

    private static Maze setupMaze(String filename) {
        Maze maze = null;
        try {
            maze = new Maze(filename);
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
                System.out.print(e.getMessage());
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
                logMessage(Level.SEVERE, () -> "Encountered issue during runtime!" + e.getMessage());
            }
            catch(IllegalMazeException e) {
                viewer.setMessage(Colour.RED + "Something went wrong moving player! " + Colour.BLANK);
                logMessage(Level.SEVERE, () -> "Encountered issue during runtime!" + e.getMessage());
            }

            viewer.display(maze);

            if(maze.finished()) {
                running = false;
            }
        }
        viewer.display(maze);
    }

    public static void logMessage(Level level, String message) {
        LOGGER.log(level, message);
    }
    public static void logMessage(Level level, Supplier<String> message) {
        LOGGER.log(level, message);
    }
}
