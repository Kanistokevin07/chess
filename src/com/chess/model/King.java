package com.chess.model;

import com.chess.enums.Color;
import com.chess.enums.moveType;
import com.chess.enums.pieceType;
import com.chess.game.Game;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    public King(Color color){
        super(color);
    }

    @Override
    public pieceType getType() {
        return pieceType.King;
    }

    @Override
    public List<Move> getPsuedoLegalMoves(Position pos, Board board, Game game) {
        List<Move> moves = new ArrayList<>();

        int row = pos.getRow();
        int col = pos.getCol();

        int [][] directions = {
                {0,1},
                {1,0},
                {0,-1},
                {-1,0},
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1}
        };

        for(int[] dir: directions){

            int newRow = row+dir[0];
            int newCol = col+dir[1];

            Position newPos = new Position(newRow, newCol);
            Piece target = board.getPieceByPosition(newPos);

            if(!board.isInsideBoard(newRow, newCol))
                continue;

            if(target==null || target.getColor()!=this.color)
                moves.add(new Move(moveType.Normal,pos, newPos, this, target, null));

        }

        if(!this.hasMoved){

            //king side
            Piece rook = board.getPiece(row,7);

            if(rook != null && !rook.hasMoved && rook.getColor() == this.color && rook.getType() == pieceType.Rook){
                Position f1 = new Position(row,5);
                Position g1 = new Position(row,6);

                if(board.getPieceByPosition(f1)==null && board.getPieceByPosition(g1)==null){
                    if(!game.isKingInCheck(this.color) && !game.isSquareUnderAttack(f1, this.color) && !game.isSquareUnderAttack(g1, this.color)){
                        moves.add(new Move(moveType.Castle_KingSide, pos, g1,this, null , null));
                    }
                }
            }

            //queenside
            rook = board.getPiece(row,0);

            if(rook != null && !rook.hasMoved && rook.getColor() == this.color && rook.getType() == pieceType.Rook){
                Position b8 = new Position(row,1);
                Position c8 = new Position(row,2);
                Position d8 = new Position(row,3);


                if(board.getPieceByPosition(b8)==null && board.getPieceByPosition(c8)==null && board.getPieceByPosition(d8)==null){
                    if(!game.isKingInCheck(this.color) && !game.isSquareUnderAttack(c8, this.color)
                            && !game.isSquareUnderAttack(d8, this.color)){
                        moves.add(new Move(moveType.Castle_QueenSide, pos, c8,this, null , null));
                    }
                }
            }
        }
        return moves;
    }
}
