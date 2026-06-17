package com.chess.model;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{
    public Bishop(Color color){
        super(color);
    }

    @Override
    public pieceType getType() {
        return pieceType.Bishop;
    }

    @Override
    public List<Move> getPsuedoLegalMoves(Position pos, Board board, Game game) {
        List<Move> moves = new ArrayList<>();

        int[][] directions = {
                {1,1},
                {1,-1},
                {-1,-1},
                {-1,1}
        };

        addSlidingMoves(board, pos, moves, directions);

        return moves;
    }
}
