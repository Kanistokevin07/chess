package com.chess.ai;

import com.chess.enums.Color;
import com.chess.game.Game;
import com.chess.game.GameState;
import com.chess.model.Move;

public class AIPlayer {

    private final Color aiColor;
    private final int depth;
    private final MiniMax minimax;

    public AIPlayer(Color aiColor, int depth) {
        this.aiColor = aiColor;
        this.depth = depth;
        this.minimax = new MiniMax();
    }

    public Move chooseMove(Game game) {
        Move bestMove = null;
        int bestScore;

        if (aiColor == Color.White)
            bestScore = Integer.MIN_VALUE;
        else
            bestScore = Integer.MAX_VALUE;

        for (Move move : game.getAllLegalMoves(aiColor)) {

            GameState state = game.applySearchMove(move);
            int score = minimax.minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, game, game.getCurrentTurn());

            game.undoSearchMove(state);

            if(aiColor == Color.White) {
                if(score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }

            }
            else{
                if(score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }

            }
        }

        return bestMove;
    }
}