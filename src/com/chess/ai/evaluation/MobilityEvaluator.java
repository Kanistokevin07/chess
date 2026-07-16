package com.chess.ai.evaluation;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;
import com.chess.model.Piece;
import com.chess.model.Position;


public class MobilityEvaluator {
    public int evaluate(Game game){

        int whiteMobility = calculateMobility(game, Color.White);
        int blackMobility = calculateMobility(game, Color.Black);

        return whiteMobility - blackMobility;
    }

    private int calculateMobility(Game game, Color color){
        int score = 0;

        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){

                Piece piece = game.getBoard()
                        .getPiece(row,col);

                if(piece == null)
                    continue;

                if(piece.getColor() != color)
                    continue;

                score += getPieceMobility(
                        game,
                        piece,
                        row,
                        col
                );
            }
        }
        return score;
    }

    private int getPieceMobility(Game game,
                                 Piece piece,
                                 int row,
                                 int col){

        return switch(piece.getType()){

            case Knight -> countKnightMoves(game,row,col) * 4;

            case Bishop -> countSlidingMoves(
                    game,row,col,
                    new int[][]{
                            {1,1},
                            {1,-1},
                            {-1,1},
                            {-1,-1}
                    }
            ) * 5;

            case Rook -> countSlidingMoves(
                    game,row,col,
                    new int[][]{
                            {1,0},
                            {-1,0},
                            {0,1},
                            {0,-1}
                    }
            ) * 2;

            case Queen -> countSlidingMoves(
                    game,row,col,
                    new int[][]{
                            {1,1},
                            {1,-1},
                            {-1,1},
                            {-1,-1},
                            {1,0},
                            {-1,0},
                            {0,1},
                            {0,-1}
                    }
            );


            default -> 0;
        };
    }

    private int countKnightMoves(Game game,
                                 int row,
                                 int col){

        int count = 0;
        int[][] moves = {
                {2,1},
                {2,-1},
                {-2,1},
                {-2,-1},
                {1,2},
                {1,-2},
                {-1,2},
                {-1,-2}
        };


        for(int[] move : moves){

            int r = row + move[0];
            int c = col + move[1];


            if(game.getBoard().isInsideBoard(r,c))
                count++;
        }
        return count;
    }

    private int countSlidingMoves(Game game,
                                  int row,
                                  int col,
                                  int[][] directions){

        int count = 0;
        for(int[] dir : directions){

            int r = row + dir[0];
            int c = col + dir[1];

            while(game.getBoard()
                    .isInsideBoard(r,c)){

                Piece piece =
                        game.getBoard().getPiece(r,c);


                if(piece != null)
                    break;

                count++;

                r += dir[0];
                c += dir[1];
            }
        }
        return count;
    }
}