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
}
