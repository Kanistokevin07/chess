package com.chess.model;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
    public Rook(Color color){
        super(color);
    }

    @Override
    public pieceType getType() {
        return pieceType.Rook;
    }

    @Override
    public List<Move> getPsuedoLegalMoves(Position pos, Board board, Game game) {
        List<Move> moves = new ArrayList<>();

        int[][] directions ={
                {0,1},
                {1,0},
                {-1,0},
                {0,-1}
        };

        addSlidingMoves(board, pos, moves, directions);

        return moves;
    }
}
