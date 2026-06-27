package com.chess.game;

import com.chess.enums.Color;
import com.chess.enums.GameStatus;

public class GameState {

    private Color currentTurn;
    private int halfMoveClock;
    private GameStatus gameStatus;

    private boolean whiteKingMoved;
    private boolean whiteKingsideRookMoved;
    private boolean whiteQueensideRookMoved;

    private boolean blackKingMoved;
    private boolean blackKingsideRookMoved;
    private boolean blackQueensideRookMoved;

    // ---------------- FULL CONSTRUCTOR ----------------
    public GameState(Color currentTurn,
                     int halfMoveClock,
                     GameStatus gameStatus,
                     boolean whiteKingMoved,
                     boolean whiteKingsideRookMoved,
                     boolean whiteQueensideRookMoved,
                     boolean blackKingMoved,
                     boolean blackKingsideRookMoved,
                     boolean blackQueensideRookMoved) {

        this.currentTurn = currentTurn;
        this.halfMoveClock = halfMoveClock;
        this.gameStatus = gameStatus;

        this.whiteKingMoved = whiteKingMoved;
        this.whiteKingsideRookMoved = whiteKingsideRookMoved;
        this.whiteQueensideRookMoved = whiteQueensideRookMoved;

        this.blackKingMoved = blackKingMoved;
        this.blackKingsideRookMoved = blackKingsideRookMoved;
        this.blackQueensideRookMoved = blackQueensideRookMoved;
    }

    // ---------------- DEFAULT CONSTRUCTOR ----------------
    public GameState() {
        this.currentTurn = Color.White;
        this.halfMoveClock = 0;
        this.gameStatus = GameStatus.ONGOING;

        this.whiteKingMoved = false;
        this.whiteKingsideRookMoved = false;
        this.whiteQueensideRookMoved = false;

        this.blackKingMoved = false;
        this.blackKingsideRookMoved = false;
        this.blackQueensideRookMoved = false;
    }

    // ---------------- GETTERS ----------------
    public Color getCurrentTurn() {
        return currentTurn;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public boolean isWhiteKingMoved() {
        return whiteKingMoved;
    }

    public boolean isWhiteKingsideRookMoved() {
        return whiteKingsideRookMoved;
    }

    public boolean isWhiteQueensideRookMoved() {
        return whiteQueensideRookMoved;
    }

    public boolean isBlackKingMoved() {
        return blackKingMoved;
    }

    public boolean isBlackKingsideRookMoved() {
        return blackKingsideRookMoved;
    }

    public boolean isBlackQueensideRookMoved() {
        return blackQueensideRookMoved;
    }

    // ---------------- SETTERS ----------------
    public void setCurrentTurn(Color currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setWhiteKingMoved(boolean whiteKingMoved) {
        this.whiteKingMoved = whiteKingMoved;
    }

    public void setWhiteKingsideRookMoved(boolean whiteKingsideRookMoved) {
        this.whiteKingsideRookMoved = whiteKingsideRookMoved;
    }

    public void setWhiteQueensideRookMoved(boolean whiteQueensideRookMoved) {
        this.whiteQueensideRookMoved = whiteQueensideRookMoved;
    }

    public void setBlackKingMoved(boolean blackKingMoved) {
        this.blackKingMoved = blackKingMoved;
    }

    public void setBlackKingsideRookMoved(boolean blackKingsideRookMoved) {
        this.blackKingsideRookMoved = blackKingsideRookMoved;
    }

    public void setBlackQueensideRookMoved(boolean blackQueensideRookMoved) {
        this.blackQueensideRookMoved = blackQueensideRookMoved;
    }
}