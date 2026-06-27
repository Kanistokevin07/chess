package com.chess.controller;

import com.chess.enums.Color;
import com.chess.game.Game;
import com.chess.model.*;

public class GameFactory {
    public static Game createGame(){
        Board board = new Board();
        setUpInitialPosition(board);
        Game game = new Game(board);
        game.initPieceReferences();

        return game;
    }

    public static void setUpInitialPosition(Board board) {

        // ---------------- BLACK PIECES ----------------

        board.setPiece(new Position(0, 0), new Rook(Color.Black));
        board.setPiece(new Position(0, 1), new Knight(Color.Black));
        board.setPiece(new Position(0, 2), new Bishop(Color.Black));
        board.setPiece(new Position(0, 3), new Queen(Color.Black));
        board.setPiece(new Position(0, 4), new King(Color.Black));
        board.setPiece(new Position(0, 5), new Bishop(Color.Black));
        board.setPiece(new Position(0, 6), new Knight(Color.Black));
        board.setPiece(new Position(0, 7), new Rook(Color.Black));

        for (int col = 0; col < 8; col++) {
            board.setPiece(new Position(1, col), new Pawn(Color.Black));
        }

        // ---------------- WHITE PIECES ----------------

        for (int col = 0; col < 8; col++) {
            board.setPiece(new Position(6, col), new Pawn(Color.White));
        }

        board.setPiece(new Position(7, 0), new Rook(Color.White));
        board.setPiece(new Position(7, 1), new Knight(Color.White));
        board.setPiece(new Position(7, 2), new Bishop(Color.White));
        board.setPiece(new Position(7, 3), new Queen(Color.White));
        board.setPiece(new Position(7, 4), new King(Color.White));
        board.setPiece(new Position(7, 5), new Bishop(Color.White));
        board.setPiece(new Position(7, 6), new Knight(Color.White));
        board.setPiece(new Position(7, 7), new Rook(Color.White));

        System.out.println(board.getPiece(7,1));
        System.out.println(board.getPiece(7,2));
        System.out.println(board.getPiece(7,3));
        System.out.println(board.getPiece(7,4));
        System.out.println(board.getPiece(7,5));
    }


}

