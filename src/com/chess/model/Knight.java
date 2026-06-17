package com.chess.model;

import com.chess.enums.Color;
import com.chess.enums.moveType;
import com.chess.enums.pieceType;
import com.chess.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{
    public Knight(Color color){
        super(color);
    }

    @Override
    public pieceType getType() {
        return pieceType.Knight;
    }

    @Override
    public List<Move> getPsuedoLegalMoves(Position pos, Board board, Game game) {
        List<Move> moves = new ArrayList<>();
        int row = pos.getRow();
        int col = pos.getCol();

        int[] rdir = {1, 2, -1, -2};
        int[] cdir = {1, 2, -1, -2};

        for(int x: rdir){
            for(int y: cdir){

                if(Math.abs(x)+Math.abs(y)==3 && board.isInsideBoard(row+x,col+y)){

                    Piece target = board.getPiece(row+x, col+y);
                    if(target==null || target.getColor()!=this.color)
                        moves.add(new Move(moveType.Normal,pos, new Position(row+x,col+y), this, target, null));
                }
            }
        }

        return moves;
    }
}
