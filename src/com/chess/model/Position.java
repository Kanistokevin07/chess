package com.chess.model;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col){
        this.col = col;
        this.row = row;
    }

    public int getRow(){ return row; }

    public int getCol(){ return row; }
}
