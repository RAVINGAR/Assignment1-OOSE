package com.ravingarinc.oose.assignment1.maze;

import com.ravingarinc.oose.assignment1.MazeApplication;

import java.util.function.Predicate;
import java.util.logging.Level;

public enum Symbol {
    TOP_LEFT_CORNER('┌', false, true, false, true),
    TOP_RIGHT_CORNER('┐', false, true, true, false),
    BOTTOM_LEFT_CORNER('└', true, false, false, true),
    BOTTOM_RIGHT_CORNER('┘', true, false, true, false),
    LEFT_T('├', true, true, false, true),
    RIGHT_T('┤', true, true, true, false),
    TOP_T('┬', false, true, true, true),
    BOTTOM_T('┴', true, false, true, true),
    CROSSWAY('┼', true, true, true, true),
    HORIZONTAL_WALL('─') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            //These can't use a predicate due to or condition. And due to the nature of Java you can't reference local fields, on static ones.
            return up == ' ' && down == ' ' && (left == '─' || left == '▒') || (right == '─' || right == '▒');
        }
    },
    VERTICAL_WALL('│') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return (up == '│' || up == '▒') || (down == '│' || down == '▒') && left == ' ' && right == ' ';
        }
    },
    DOOR('▒'),
    KEY('╕'),
    END('X'),
    UP('^'),
    DOWN('v'),
    LEFT('<'),
    RIGHT('>');

    private final char symbol;

    private final Predicate<Character> upPredicate;
    private final Predicate<Character> downPredicate;
    private final Predicate<Character> leftPredicate;
    private final Predicate<Character> rightPredicate;

    Symbol(char symbol) {
        this.symbol = symbol;
        this.upPredicate = u -> false;
        this.downPredicate = u -> false;
        this.leftPredicate = u -> false;
        this.rightPredicate = u -> false;
    }

    Symbol(char symbol, boolean up, boolean down, boolean left, boolean right) {
        this.symbol = symbol;
        this.upPredicate = up ? v -> (v == '│' || v == '▒') : e -> e == ' ';
        this.downPredicate = down ? v -> (v == '│' || v == '▒') : e -> e == ' ';
        this.leftPredicate = left ? h -> (h == '─' || h == '▒') : e -> e == ' ';
        this.rightPredicate = right ? h -> (h == '─' || h == '▒') : e -> e == ' ';
    }

    @Override
    public String toString() {
        return symbol + "\033[m";
    }

    /**
     * Determines the relevant symbol to use based on the corner block specified.
     * @param r Row of the corner
     * @param c Column of the corner
     * @param symbols The final maze symbol
     * @return The applicable symbol
     */
    public static String getApplicable(int r, int c, String[][] symbols) {
        char up, down, left, right; //Represents the symbols above, below, left and right of the corner

        String applicable = "?";
        try {
            /*
            There are two ways now to find the correct symbol.

            1. create an abstract method called isApplicable(char, char, char, char) which is overriden
            by the enum values. Then iterate through and make each individual comparison

            2. Make a manual if statement check that considers all possible empty characters using probably
            too many if statements which is a lot messier.

            Ideally 2 may require less time to run, 1 is a lot cleaner and way easier to understand and debug
            if anything goes wrong.
             */

            //These checks ensure that we aren't accessing outside the arrays bounds.
            up = r == 0 ? ' ' : convertString(symbols[r-1][c]);
            down = r == symbols.length-1 ? ' ' : convertString(symbols[r+1][c]);
            left = c == 0 ? ' ' : convertString(symbols[r][c-1]);
            right = c == symbols[0].length-1 ? ' ' : convertString(symbols[r][c+1]);

            for(Symbol s : Symbol.values()) {
                if(s.isApplicable(up, down, left, right)) {
                    applicable = s.toString();
                    break;
                }
            }

        }
        catch(ArrayIndexOutOfBoundsException e) {
            MazeApplication.log(Level.WARNING, "Attempted to get applicable symbol with index out of bounds at r: " + r + ", and c: " + c + "!");
        }
        return applicable;
    }

    /**
     * Strips away escape codes
     * @return The char stripped of escape codes
     */
    private static char convertString(String symbol) {
        /*
        How String.length() works

        If symbol == "\033[31m" that is length OF 5
        If symbol == "\033[31mE\033[m" that is length OF 9. E is at the int of 5
        This is the logic used in the below code.
        */
        //Every icon in the maze WILL have a code of \033[m
        if(symbol != null) {
            return symbol.charAt(symbol.length()-4);
        }
        else {
            return ' ';
        }
    }

    public boolean isApplicable(char up, char down, char left, char right) {
        return upPredicate.test(up) && downPredicate.test(down) && leftPredicate.test(left) && rightPredicate.test(right);
    }
}
