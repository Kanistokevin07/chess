package com.chess.ai.evaluation;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;
import com.chess.model.Piece;

public class BishopPairEvaluator {

    public int evaluate(Game game) {
        int whiteBishops = 0;
        int blackBishops = 0;

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {

                Piece piece = game.getBoard().getPiece(row, col);

                if(piece == null)
                    continue;

                if(piece.getType() == pieceType.Bishop) {
                    if(piece.getColor() == Color.White)
                        whiteBishops++;
                    else
                        blackBishops++;
                }
            }
        }

        int score = 0;

        if(whiteBishops >= 2)
            score += 30;

        if(blackBishops >= 2)
            score -= 30;

        return score;
    }
}