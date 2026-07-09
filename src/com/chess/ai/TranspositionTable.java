package com.chess.ai;

import java.util.HashMap;

public class TranspositionTable {

    private final HashMap<Long, TTEntry> table = new HashMap<>();

    public TTEntry lookup(long hash) {
        return table.get(hash);
    }

    public void store(TTEntry entry) {

        TTEntry old = table.get(entry.getHash());

        if (old == null || entry.getDepth() >= old.getDepth()) {
            table.put(entry.getHash(), entry);
        }
    }

    public void clear() {
        table.clear();
    }
}