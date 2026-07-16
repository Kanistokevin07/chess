package com.chess.ai.evaluation;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;
import com.chess.model.Piece;


public class PawnStructureEvaluator {

    public int evaluate(Game game){
        return evaluateColor(game, Color.White) -
                evaluateColor(game, Color.Black);
    }

    private int evaluateColor(Game game, Color color){
        int score = 0;
        int[] files = new int[8];

        // count pawns per file
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                Piece piece =
                        game.getBoard()
                                .getPiece(row,col);

                if(piece != null &&
                        piece.getColor()==color &&
                        piece.getType()==pieceType.Pawn){

                    files[col]++;
                }
            }
        }

        // doubled pawns
        for(int file=0; file<8; file++){
            if(files[file] > 1){
                score -= (files[file]-1)*15;
            }
        }

        // isolated pawns
        for(int file=0; file<8; file++){
            if(files[file] == 0)
                continue;

            boolean hasNeighbor = false;

            if(file>0 && files[file-1]>0)
                hasNeighbor=true;

            if(file<7 && files[file+1]>0)
                hasNeighbor=true;

            if(!hasNeighbor)
                score -= 20;
        }

        return score;
    }
}