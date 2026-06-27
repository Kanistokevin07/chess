package com.chess.ai;

import com.chess.enums.Color;
import com.chess.enums.GameStatus;
import com.chess.game.Game;
import com.chess.game.GameState;
import com.chess.model.Move;

import static java.lang.Math.max;

public class MiniMax {
    private final Evaluator evaluator;

    public MiniMax(){
        evaluator = new Evaluator();
    }

    public int minimax(int depth, Game game, Color sideToMove){
        if(depth ==0 || game.isGameOver()){

            GameStatus status = game.getGameStatus();
            if (status == GameStatus.CHECKMATE) {
                // White to move and game over => White is checkmated
                if (sideToMove == Color.White)
                    return -100000;
                // Black to move and game over => Black is checkmated
                return 100000;
            }

            if (status == GameStatus.STALEMATE || status == GameStatus.DRAW)
                return 0;
            return evaluator.evaluate(game);
        }

        if(sideToMove == Color.White){
            int bestScore = Integer.MIN_VALUE;
            for(Move m: game.getAllLegalMoves(Color.White)){
                GameState state = game.applySearchMove(m);
                int score = minimax(depth-1, game, Color.Black);
                game.undoSearchMove(state);
                bestScore = Math.max(bestScore, score);
                return bestScore;
            }
        }
        else{
            int bestScore = Integer.MAX_VALUE;
            for(Move m: game.getAllLegalMoves(Color.Black)){
                GameState state = game.applySearchMove(m);
                int score = minimax(depth-1, game, Color.White);
                game.undoSearchMove(state);
                bestScore = Math.min(bestScore, score);
                return bestScore;
            }
        }

        return -1;
    }

}
