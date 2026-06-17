package com.chess.model;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    public Queen(Color color){
        super(color);
    }

    @Override
    public pieceType getType() {
        return pieceType.Queen;
    }

    @Override
    public List<Move> getPsuedoLegalMoves(Position pos, Board board, Game game) {
        List<Move> moves = new ArrayList<>();

        int [][] directions = {
                {0,1},
                {1,0},
                {0,-1},
                {-1,0},
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1}
        };

        addSlidingMoves(board, pos, moves, directions);

        return moves;
    }
}
