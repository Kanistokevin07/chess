package com.chess.ai;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;
import com.chess.model.Piece;

public class Evaluator {

    public static int evaluate(Game game) {
        int score = 0;

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {

                Piece piece = game.getBoard().getPiece(row, col);
                if(piece == null)
                    continue;

                int value = getPieceValue(piece);

                if(piece.getColor() == Color.White)
                    score += value;
                else
                    score -= value;
            }
        }
        return score;
    }

    private static int getPieceValue(Piece piece) {

        return switch(piece.getType()) {
            case Pawn -> 100;
            case Knight -> 320;
            case Bishop -> 330;
            case Rook -> 500;
            case Queen -> 900;
            case King -> 20000;
        };
    }
}