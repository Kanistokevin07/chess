package com.chess.ai;

import com.chess.enums.Color;
import com.chess.enums.moveType;
import com.chess.enums.pieceType;
import com.chess.game.Game;
import com.chess.game.GameState;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.Position;

import java.util.Comparator;
import java.util.List;

public class MoveOrdering {

    public static void orderMoves(Game game, List<Move> moves, int depth, Move[][] killerMoves,
                                  int[][] historyTable) {
        moves.sort((a, b) -> {
            return scoreMove(game, b, depth, killerMoves, historyTable) -
                    scoreMove(game, a, depth, killerMoves, historyTable);
        });
    }

    private static int scoreMove(Game game, Move move, int depth, Move[][] killerMoves,
                                 int[][] historyTable) {

        int score = 0;

        if (move.equals(killerMoves[depth][0])) {
            score += 1_000_000;
        }
        else if (move.equals(killerMoves[depth][1])) {
            score += 900_000;
        }

        Piece attacker = game.getBoard().getPieceByPosition(move.getFrom());
        Piece victim = move.getCapturedPiece();

        // 1. Captures (highest priority)
        if (victim != null) {
            score += 10000;
            score += mvvLvaScore(move, game);
        }

        // 2. Promotions
        if (move.getType() == moveType.Promotion) {
            score += 9000;
        }

        // 3. Castling
        if (move.getType() == moveType.Castle_KingSide ||
                move.getType() == moveType.Castle_QueenSide) {
            score += 5000;
        }

        // history table scores

        int from = move.getFrom().getRow() * 8 +
                        move.getFrom().getCol();

        int to = move.getTo().getRow() * 8 +
                        move.getTo().getCol();

        score += historyTable[from][to];

        // 4. Quiet moves (low base)
        score += 10;

        return score;
    }

    private static int getPieceValue(pieceType type) {
        return switch (type) {
            case Pawn -> 100;
            case Knight -> 320;
            case Bishop -> 330;
            case Rook -> 500;
            case Queen -> 900;
            case King -> 20000;
        };
    }

    private static int mvvLvaScore(Move move, Game game) {

        Piece attacker = game.getBoard().getPieceByPosition(move.getFrom());
        Piece victim = move.getCapturedPiece();

        if (attacker == null || victim == null)
            return 0;

        return getPieceValue(victim.getType()) * 10
                - getPieceValue(attacker.getType());
    }
}