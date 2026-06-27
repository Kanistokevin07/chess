package com.chess.ai;

import com.chess.enums.Color;
import com.chess.enums.GameStatus;
import com.chess.enums.moveType;
import com.chess.game.Game;
import com.chess.game.GameState;
import com.chess.model.Move;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MiniMax {
    private final Evaluator evaluator;
    private Move[][] killerMoves = new Move[100][2];

    public MiniMax(){
        evaluator = new Evaluator();
    }

    public int minimax(int depth, int alpha, int beta, Game game, Color sideToMove){
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
            List<Move> moves = game.getAllLegalMoves(sideToMove);
            MoveOrdering.orderMoves(game, moves, depth, killerMoves);

            for(Move m: moves){
                GameState state = game.applySearchMove(m);

                int score = minimax(depth-1, alpha, beta, game,  Color.Black);
                game.undoSearchMove(state);
                bestScore = Math.max(bestScore, score);

                alpha = max(alpha, bestScore);

                if(alpha>=beta) {
                    if (m.getCapturedPiece() == null &&
                            m.getType() != moveType.Promotion) {

                        storeKiller(depth, m);
                    }
                    break;
                }
            }
            return bestScore;
        }
        else{
            int bestScore = Integer.MAX_VALUE;

            List<Move> moves = game.getAllLegalMoves(sideToMove);
            MoveOrdering.orderMoves(game, moves, depth, killerMoves);

            for(Move m: moves){
                GameState state = game.applySearchMove(m);
                int score = minimax(depth-1, alpha, beta, game, Color.White);

                game.undoSearchMove(state);
                bestScore = min(bestScore, score);

                beta = min(beta, bestScore);
                if(alpha>=beta) {
                    if (m.getCapturedPiece() == null &&
                            m.getType() != moveType.Promotion) {

                        storeKiller(depth, m);
                    }
                    break;
                }

            }
            return bestScore;
        }
    }

    private void storeKiller(int depth, Move move) {

        if (!move.equals(killerMoves[depth][0])) {
            killerMoves[depth][1] = killerMoves[depth][0];
            killerMoves[depth][0] = move;
        }
    }

}
