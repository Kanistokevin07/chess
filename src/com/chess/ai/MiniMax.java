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
    private final int[][] historyTable = new int[64][64];

    public MiniMax(){
        evaluator = new Evaluator();
    }

    public int minimax(int depth, int alpha, int beta, Game game, Color sideToMove){
        if(depth ==0 || game.isGameOver()){

            GameStatus status = game.getGameStatus();
            System.out.println(game.getGameStatus());

            if (status == GameStatus.CHECKMATE) {
                // White to move and game over => White is checkmated
                if (sideToMove == Color.White)
                    return -100000;
                // Black to move and game over => Black is checkmated
                return 100000;
            }

            if (status == GameStatus.STALEMATE || status == GameStatus.DRAW)
                return 0;
            return quiescence(alpha, beta, game, sideToMove);
        }

        if(sideToMove == Color.White){
            int bestScore = Integer.MIN_VALUE;
            List<Move> moves = game.getAllLegalMoves(sideToMove);
            MoveOrdering.orderMoves(game, moves, depth, killerMoves, historyTable);

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
                        updateHistory(m, depth);
                    }
                    break;
                }
            }
            return bestScore;
        }
        else{
            int bestScore = Integer.MAX_VALUE;

            List<Move> moves = game.getAllLegalMoves(sideToMove);
            MoveOrdering.orderMoves(game, moves, depth, killerMoves, historyTable);

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
                        updateHistory(m, depth);
                    }
                    break;
                }

            }
            return bestScore;
        }
    }

    private int quiescence(int alpha,
                           int beta,
                           Game game,
                           Color sideToMove) {

        int standPat = evaluator.evaluate(game);

        // Beta cutoff
        if(sideToMove == Color.White){

            if(standPat >= beta)
                return beta;

            alpha = Math.max(alpha, standPat);

        }else{

            if(standPat <= alpha)
                return alpha;

            beta = Math.min(beta, standPat);
        }

        List<Move> captures = game.getAllCaptureMoves(sideToMove);
        MoveOrdering.orderMoves(game, captures, 0, killerMoves, historyTable);

        if(sideToMove == Color.White){
            int best = standPat;
            for(Move move : captures){
                GameState state = game.applySearchMove(move);

                int score = quiescence(alpha,
                        beta,
                        game,
                        Color.Black);
                game.undoSearchMove(state);

                best = Math.max(best, score);
                alpha = Math.max(alpha, best);

                if(alpha >= beta)
                    break;
            }
            return best;
        }

        else{

            int best = standPat;
            for(Move move : captures){

                GameState state = game.applySearchMove(move);
                int score = quiescence(alpha,
                        beta,
                        game,
                        Color.White);

                game.undoSearchMove(state);

                best = Math.min(best, score);
                beta = Math.min(beta, best);

                if(alpha >= beta)
                    break;
            }

            return best;
        }
    }

    private void updateHistory(Move move, int depth) {
        int from = move.getFrom().getRow() * 8 + move.getFrom().getCol();
        int to = move.getTo().getRow() * 8 + move.getTo().getCol();
        historyTable[from][to] += depth * depth;
    }

    private void storeKiller(int depth, Move move) {

        if (!move.equals(killerMoves[depth][0])) {
            killerMoves[depth][1] = killerMoves[depth][0];
            killerMoves[depth][0] = move;
        }
    }

}
