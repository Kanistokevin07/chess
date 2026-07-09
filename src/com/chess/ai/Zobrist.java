package com.chess.ai;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.game.Game;
import com.chess.model.Move;
import com.chess.model.Piece;

import java.util.Random;

public class Zobrist {

    // [pieceType][color][square]
    private static final long[][][] pieceSquare = new long[6][2][64];

    // side to move
    private static long sideKey;

    // castling rights: [0]=whiteK, [1]=whiteQ, [2]=blackK, [3]=blackQ
    private static final long[] castlingKeys = new long[4];

    // en passant file (0–7)
    private static final long[] enPassantKeys = new long[8];

    private static final Random random = new Random(2024);

    static {
        init();
    }

    private static void init() {

        // piece-square keys
        for (int p = 0; p < 6; p++) {
            for (int c = 0; c < 2; c++) {
                for (int sq = 0; sq < 64; sq++) {
                    pieceSquare[p][c][sq] = randomLong();
                }
            }
        }

        sideKey = randomLong();

        for (int i = 0; i < 4; i++) {
            castlingKeys[i] = randomLong();
        }

        for (int i = 0; i < 8; i++) {
            enPassantKeys[i] = randomLong();
        }
    }

    private static long randomLong() {
        return random.nextLong();
    }

    // -----------------------------
    // HASH FUNCTIONS
    // -----------------------------

    public static long hashPiece(long hash, Piece piece, int square) {

        int pIndex = pieceTypeIndex(piece.getType());
        int cIndex = (piece.getColor() == Color.White) ? 0 : 1;

        return hash ^ pieceSquare[pIndex][cIndex][square];
    }

    public static long hashSide(long hash) {
        return hash ^ sideKey;
    }

    public static long hashCastling(long hash, int index) {
        return hash ^ castlingKeys[index];
    }

    public static long hashEnPassant(long hash, int file) {
        return hash ^ enPassantKeys[file];
    }

    // -----------------------------
    // Helpers
    // -----------------------------

    private static int pieceTypeIndex(pieceType type) {

        return switch (type) {
            case Pawn -> 0;
            case Knight -> 1;
            case Bishop -> 2;
            case Rook -> 3;
            case Queen -> 4;
            case King -> 5;
        };
    }

    public static long computeHash(Game game) {

        long hash = 0L;

        // ---------------- Pieces ----------------
        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {

                Piece piece = game.getBoard().getPiece(row, col);

                if (piece == null)
                    continue;

                int square = row * 8 + col;

                int pieceIndex = pieceTypeIndex(piece.getType());
                int colorIndex = (piece.getColor() == Color.White) ? 0 : 1;

                hash ^= pieceSquare[pieceIndex][colorIndex][square];
            }
        }

        // ---------------- Side to move ----------------
        if (game.getCurrentTurn() == Color.Black) {
            hash ^= sideKey;
        }

        // White castling
        if (!game.getWhiteKing().getHasMoved()) {

            if (game.getWhiteKingsideRook() != null &&
                    !game.getWhiteKingsideRook().getHasMoved()) {
                hash ^= castlingKeys[0];
            }

            if (game.getWhiteQueensideRook() != null &&
                    !game.getWhiteQueensideRook().getHasMoved()) {
                hash ^= castlingKeys[1];
            }
        }

// Black castling
        if (!game.getBlackKing().getHasMoved()) {

            if (game.getBlackKingsideRook() != null &&
                    !game.getBlackKingsideRook().getHasMoved()) {
                hash ^= castlingKeys[2];
            }

            if (game.getBlackQueensideRook() != null &&
                    !game.getBlackQueensideRook().getHasMoved()) {
                hash ^= castlingKeys[3];
            }
        }
        // ---------------- En Passant ----------------

        Move lastMove = game.getLastMove();

        if (lastMove != null &&
                lastMove.getMovedPiece().getType() == pieceType.Pawn) {

            int diff = Math.abs(
                    lastMove.getFrom().getRow()
                            - lastMove.getTo().getRow());

            if (diff == 2) {
                hash ^= enPassantKeys[lastMove.getTo().getCol()];
            }
        }

        return hash;
    }
}