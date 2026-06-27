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

                int value = evaluatePiece(piece, row, col);

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

    private static int evaluatePiece(Piece piece, int row, int col) {

        return getPieceValue(piece) +
                getPieceSquareValue(piece, row, col);
    }

    private static int getPieceSquareValue(Piece piece, int row, int col) {

        int[][] table = switch (piece.getType()) {
            case Pawn   -> PieceSquareTables.PAWN;
            case Knight -> PieceSquareTables.KNIGHT;
            case Bishop -> PieceSquareTables.BISHOP;
            case Rook   -> PieceSquareTables.ROOK;
            case Queen  -> PieceSquareTables.QUEEN;
            case King   -> PieceSquareTables.KING;
        };

        if (piece.getColor() == Color.White)
            return table[row][col];

        return table[7 - row][col];
    }
}