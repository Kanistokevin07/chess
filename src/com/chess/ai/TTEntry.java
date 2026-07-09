package com.chess.ai;

import com.chess.model.Move;

public class TTEntry {

    private final long hash;
    private final int depth;
    private final int score;
    private final Move bestMove;
    private final EntryType type;

    public TTEntry(long hash,
                   int depth,
                   int score,
                   Move bestMove,
                   EntryType type) {

        this.hash = hash;
        this.depth = depth;
        this.score = score;
        this.bestMove = bestMove;
        this.type = type;
    }

    public long getHash() {
        return hash;
    }

    public int getDepth() {
        return depth;
    }

    public int getScore() {
        return score;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public EntryType getType() {
        return type;
    }
}