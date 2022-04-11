package com.ravingarinc.oose.assignment1.maze;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int row() {
        return row;
    }
    public int column() {
        return column;
    }

    public void addRow(int r) {
        row += r;
    }
    public void addColumn(int c) {
        column += c;
    }

    public void move(Direction direction) {
        switch(direction) {
            case UP -> row--;
            case DOWN -> row++;
            case LEFT -> column--;
            case RIGHT -> column++;
        }
    }
}
