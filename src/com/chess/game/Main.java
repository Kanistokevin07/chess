package com.chess.game;

import com.chess.enums.Color;
import com.chess.enums.moveType;
import com.chess.model.*;

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

        game.getBoard().setPiece(new Position(6,1), p2);
        System.out.println(game.getBoard().getPieceByPosition(new Position(6,1)));
        game.makeMove(new Move(moveType.Normal, new Position(6,1), new Position(5,1), p2, null, null));

        System.out.println(game.getBoard().getPieceByPosition(new Position(5,1)));

        Piece king = new King(Color.White);
        Position pos1 = new Position(7,4);

        Piece rook1 = new Rook(Color.White);
        Position pos2 = new Position(7,7);

        Piece rook2 = new Rook(Color.White);
        Position pos3 = new Position(7,0);

        board.setPiece(pos1, king);
        board.setPiece(pos2, rook1);
        board.setPiece(pos3, rook2);

        for(Move m: game.getLegalMoves(pos1)){
            System.out.println(m);
        }
    }
}