package com.chess.ai.evaluation;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;
import com.chess.model.Piece;

public class RookFileEvaluator {

    public int evaluate(Game game) {

        int score = 0;

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {

                Piece piece = game.getBoard().getPiece(row,col);

                if(piece == null || piece.getType() != pieceType.Rook)
                    continue;


                boolean ownPawn = false;
                boolean enemyPawn = false;


                for(int r = 0; r < 8; r++) {

                    Piece filePiece =
                            game.getBoard().getPiece(r,col);

                    if(filePiece == null)
                        continue;


                    if(filePiece.getType() == pieceType.Pawn) {

                        if(filePiece.getColor() == piece.getColor())
                            ownPawn = true;
                        else
                            enemyPawn = true;
                    }
                }


                int bonus = 0;

                if(!ownPawn && !enemyPawn)
                    bonus = 25;

                else if(!ownPawn)
                    bonus = 10;


                if(piece.getColor() == Color.White)
                    score += bonus;
                else
                    score -= bonus;
            }
        }

        return score;
    }
}