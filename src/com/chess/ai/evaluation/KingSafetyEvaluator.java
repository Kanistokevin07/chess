package com.chess.ai.evaluation;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;
import com.chess.model.Piece;
import com.chess.model.Position;


public class KingSafetyEvaluator {


    public int evaluate(Game game){

        int white =
                evaluateKing(game, Color.White);
        int black =
                evaluateKing(game, Color.Black);

        return white - black;
    }

    private int evaluateKing(Game game, Color color){

        int score = 0;
        Position king =
                game.getBoard()
                        .findKing(color);

        score += evaluatePawnShield(
                game,
                king,
                color
        );
        score -= attackedAroundKing(
                game,
                king,
                opposite(color)
        ) * 10;

        return score;
    }

    private int evaluatePawnShield(
            Game game,
            Position king,
            Color color){

        int penalty = 0;
        int direction = color == Color.White ? -1 : 1;

        int row =
                king.getRow() + direction;

        if(row < 0 || row >=8)
            return 0;

        for(int col = king.getCol()-1;
            col <= king.getCol()+1;
            col++){

            if(col < 0 || col >=8)
                continue;

            Piece piece =
                    game.getBoard()
                            .getPiece(row,col);

            if(piece == null ||
                    piece.getType()!=pieceType.Pawn ||
                    piece.getColor()!=color){

                penalty += 15;
            }
        }
        return -penalty;
    }

    private int attackedAroundKing(
            Game game,
            Position king,
            Color enemy){

        int attacks=0;

        for(int row=king.getRow()-1;
            row<=king.getRow()+1;
            row++){

            for(int col=king.getCol()-1;
                col<=king.getCol()+1;
                col++){

                if(!game.getBoard()
                        .isInsideBoard(row,col))
                    continue;

                if(game.isSquareUnderAttack(
                        new Position(row,col),
                        enemy)){
                    attacks++;
                }
            }
        }
        return attacks;
    }

    private Color opposite(Color c){
        return c==Color.White?
                Color.Black:
                Color.White;
    }
}