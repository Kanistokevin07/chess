package com.chess.ai.evaluation;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;
import com.chess.model.Piece;

public class EndgameEvaluator {


    public int evaluate(Game game) {

        int material = 0;

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {

                Piece piece = game.getBoard().getPiece(row,col);

                if(piece == null)
                    continue;

                switch(piece.getType()) {
                    case Queen:
                        material += 900;
                        break;

                    case Rook:
                        material += 500;
                        break;

                    case Bishop:
                    case Knight:
                        material += 330;
                        break;

                    default:
                        break;
                }
            }
        }


        // Not endgame
        if(material > 2500)
            return 0;


        int score = 0;


        // encourage king activity
        Piece whiteKing = findKing(game, Color.White);
        Piece blackKing = findKing(game, Color.Black);


        if(whiteKing != null)
            score += 20;

        if(blackKing != null)
            score -= 20;


        return score;
    }



    private Piece findKing(Game game, Color color) {

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {

                Piece piece =
                        game.getBoard()
                                .getPiece(row,col);

                if(piece != null &&
                        piece.getType() == pieceType.King &&
                        piece.getColor() == color)
                    return piece;
            }
        }

        return null;
    }
}