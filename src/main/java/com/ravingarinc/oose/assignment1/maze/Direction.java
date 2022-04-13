package com.ravingarinc.oose.assignment1.maze;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction getOpposing() {
        return switch(this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public String getSymbol() {
        return Symbol.valueOf(this.name()).toString();
    }

    public static Direction getDirectionInput(char input) {
        return switch(input) {
            case 'w', 'W' -> Direction.UP;
            case 's', 'S' -> Direction.DOWN;
            case 'a', 'A' -> Direction.LEFT;
            case 'd', 'D' -> Direction.RIGHT;
            default -> null;
        };
    }
}
