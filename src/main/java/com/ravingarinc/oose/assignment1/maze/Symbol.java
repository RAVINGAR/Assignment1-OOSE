package com.ravingarinc.oose.assignment1.maze;

import com.ravingarinc.oose.assignment1.MazeApplication;

import java.util.logging.Level;

public enum Symbol {
    HORIZONTAL_WALL('─') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return false;
        }
    },
    VERTICAL_WALL('│') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return false;
        }
    },
    TOP_LEFT_CORNER('┌') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return up == ' ' && down == '│' && left == ' ' && right == '─';
        }
    }, //Applicable if UP is ' ', DOWN is '|', LEFT is ' ', RIGHT is '-'
    TOP_RIGHT_CORNER('┐') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return up == ' ' && down == '│' && left == '─' && right == ' ';
        }
    }, //Applicable if UP is ' ', DOWN is '|', LEFT is '-', RIGHT is ''
    BOTTOM_LEFT_CORNER('└') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return up == '│' && down == ' ' && left == ' ' && right == '─';
        }
    }, //Applicable if UP is '|', DOWN is '', LEFT is ' ', RIGHT is '-'
    BOTTOM_RIGHT_CORNER('┘') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return up == '│' && down == ' ' && left == '─' && right == ' ';
        }
    }, //Applicable if UP is '|', DOWN is '', LEFT is '-', RIGHT is ''
    LEFT_T('├') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return up == '│' && down == '│' && left == ' ' && right == '─';
        }
    }, //Applicable if UP is '|', DOWN is '|', LEFT is ' ', RIGHT is '─'
    RIGHT_T('┤') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return up == '│' && down == '│' && left == '─' && right == ' ';
        }
    }, //Applicable if UP is '|', DOWN is '|', LEFT is '-', RIGHT is ''
    TOP_T('┬') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return up == ' ' && down == '│' && left == '─' && right == '─';
        }
    }, //Applicable if UP is ' ', DOWN is '|', LEFT is '-', RIGHT is '-'
    BOTTOM_T('┴') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return up == '│' && down == ' ' && left == '─' && right == '─';
        }
    }, //Applicable if UP is '|', DOWN is '', LEFT is '-', RIGHT is '-'
    CROSSWAY('┼') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return up == '│' && down == '│' && left == '─' && right == '─';
        }
    }, //Applicable if UP is '|', DOWN is '|', LEFT is '-', RIGHT is '-'
    DOOR('▒') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return false;
        }
    },
    KEY('╕') {
        @Override
        public boolean isApplicable(char up, char down, char left, char right) {
            return false;
        }
    };

    private final char symbol;
    Symbol(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    /**
     * Determines the relevant symbol to use based on the corner block specified.
     * @param r Row of the corner
     * @param c Column of the corner
     * @param symbols The final maze symbol
     * @return The applicable symbol
     */
    public static char getApplicable(int r, int c, char[][] symbols) {
        char up, down, left, right; //Represents the symbols above, below, left and right of the corner

        char applicable = '?';
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
            up = r == 0 ? ' ' : symbols[r-1][c];
            down = r == symbols.length-1 ? ' ' : symbols[r+1][c];
            left = c == 0 ? ' ' : symbols[r][c-1];
            right = c == symbols[0].length-1 ? ' ' : symbols[r][c+1];

            for(Symbol s : Symbol.values()) {
                if(s.isApplicable(up, down, left, right)) {
                    applicable = s.getSymbol();
                    break;
                }
            }

        }
        catch(ArrayIndexOutOfBoundsException e) {
            MazeApplication.log(Level.WARNING, "Attempted to get applicable symbol with index out of bounds at r: " + r + ", and c: " + c + "!");
        }
        return applicable;
    }

    public abstract boolean isApplicable(char up, char down, char left, char right);
}
