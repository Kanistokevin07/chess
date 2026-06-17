package com.chess.model;
import com.chess.enums.Color;
import com.chess.enums.moveType;
import com.chess.enums.pieceType;
import com.chess.game.Game;

import java.util.List;

public abstract class Piece {
    protected Color color;
    protected boolean hasMoved;

    public Piece(Color color){
        this.hasMoved = false;
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public boolean getHasMoved(){
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved){
        this.hasMoved = hasMoved;
    }

    public abstract pieceType getType();

    public abstract List<Move> getPsuedoLegalMoves(Position pos, Board board, Game game);

    protected void addSlidingMoves(Board board, Position pos, List<Move> moves, int[][] directions){
        int row = pos.getRow();
        int col = pos.getCol();

        for(int[] dir: directions){
            int newRow = row+dir[0];
            int newCol = col+dir[1];

            while(board.isInsideBoard(newRow, newCol)){
                Position newPos = new Position(newRow, newCol);
                Piece target = board.getPieceByPosition(newPos);

                if(target==null)
                    moves.add(new Move(moveType.Normal,pos, newPos, this, target, null));

                else{
                    if(target.getColor()!=this.color)
                        moves.add(new Move(moveType.Normal,pos, newPos, this, target, null));
                    break;
                }


                newRow += dir[0];
                newCol += dir[1];
            }
        }
    }
}
