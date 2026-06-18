package com.chess.game;

import com.chess.enums.Color;
import com.chess.model.Board;
import com.chess.model.Pawn;
import com.chess.model.Piece;
import com.chess.model.Position;

import static com.chess.enums.Color.Black;

class Main{
    public static void main(String[] args){
        System.out.println("Hello World");

        Board board = new Board();
        Game game = new Game(board);

        Piece p1 = new Pawn(Color.White);
        Piece p2 = new Pawn(Color.White);
        Piece p3 = new Pawn(Color.White);
        Piece p4 = new Pawn(Color.White);
        Piece p5 = new Pawn(Color.White);

        board.setPiece(new Position(1,2), p2);
        System.out.println(board.getPiece(1,2));
    }
}