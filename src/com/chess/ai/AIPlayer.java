package com.chess.ai;

import com.chess.enums.Color;
import com.chess.game.Game;
import com.chess.game.GameState;
import com.chess.model.Move;

import java.util.List;

public class AIPlayer {

    private final Color aiColor;
    private final int depth;
    private final MiniMax minimax;
    private Move principalVariationMove;

    public AIPlayer(Color aiColor, int depth) {
        this.aiColor = aiColor;
        this.depth = depth;
        this.minimax = new MiniMax();
    }

    public Move chooseMove(Game game) {
        Move bestMove = null;
        principalVariationMove = null;

        for (int currentDepth = 1; currentDepth <= depth; currentDepth++) {
            bestMove = searchAtDepth(game, currentDepth);
            System.out.println("Finished depth " + currentDepth + " Best = " + bestMove);
        }
        return bestMove;
    }

    private Move searchAtDepth(Game game, int currentDepth) {

        Move bestMove = null;
        int bestScore = (aiColor == Color.White) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        List<Move> moves = game.getAllLegalMoves(aiColor);

        if (principalVariationMove != null && moves.remove(principalVariationMove)) {
            moves.add(0, principalVariationMove);
        }

        for (Move move : moves) {

            GameState state = game.applySearchMove(move);

            int score = minimax.minimax(
                    currentDepth - 1,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    game,
                    game.getCurrentTurn());

            game.undoSearchMove(state);

            if (aiColor == Color.White) {
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            } else {
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        principalVariationMove = bestMove;
        return bestMove;
    }
}