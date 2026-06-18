package com.chess.model;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col){
        this.col = col;
        this.row = row;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Position other = (Position) obj;

        return this.row == other.row && this.col == other.col;
    }

    public int getRow(){ return row; }

    public int getCol(){ return col; }
}
