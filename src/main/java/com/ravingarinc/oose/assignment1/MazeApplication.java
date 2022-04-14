package com.ravingarinc.oose.assignment1;

import com.ravingarinc.oose.assignment1.util.IllegalMazeException;
import com.ravingarinc.oose.assignment1.util.Viewer;
import com.ravingarinc.oose.assignment1.util.MazeErrorException;
import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Maze;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MazeApplication {
    private static final Logger LOGGER = Logger.getLogger(MazeApplication.class.getName());
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        System.out.println("Welcome to the Untitled Maze Game!");
        String filename;
        Maze maze = null;
        Viewer viewer = null;
        try {
            filename = getFilename();
            maze = new Maze(filename);
            viewer = new Viewer(maze);
        }
        catch(MazeErrorException | IllegalMazeException e) {
            logMessage(Level.SEVERE, e.getMessage());
        }

        if(maze == null || viewer == null) { //If getting filename, creating maze or creating viewer does not load properly. Will exit program
            System.out.println("Encountered errors on load. Program will now exit.");
        }
        else {
            run(maze, viewer);
        }
        scanner.close();
    }

    @NotNull
    private static String getFilename() throws MazeErrorException {
        String filename = null;
        while(filename == null) {
            System.out.print("Please enter a valid filename: ");
            try {
                filename = scanner.nextLine();
                if(filename.isEmpty()) {
                    throw new NoSuchElementException("Filename was empty!");
                }

                FileReader file = new FileReader(filename); //This acts as a check to whether file exists.
                file.close();
            }
            catch(NoSuchElementException e) {
                System.out.println("Invalid input! Try again!");
                scanner.next();
            }
            catch(FileNotFoundException e) {
                System.out.println("File was not found! Please try again!");
                filename = null;
            }
            catch (IOException e) {
                throw new MazeErrorException("Could not close file!", e);
            }
        }
        return filename;
    }

    private static void run(Maze maze, Viewer viewer) {
        try {
            maze.handleInitialPlayerPosition();
        }
        catch(IllegalMazeException e) {
            logMessage(Level.SEVERE, e.getMessage());
        }
        viewer.display(maze);
        boolean running = true;
        while(running) {
            try {
                String str = scanner.next();
                if(str == null || str.length() != 1) {
                    throw new InputMismatchException();
                }
                Direction input = Direction.getDirectionInput(str.charAt(0));
                if(input != null) {
                    maze.handlePlayerMove(input);
                }
            }
            catch(InputMismatchException e) {
                viewer.setMessage(Colour.RED + "Incorrect input, try again!" + Colour.BLANK);
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
