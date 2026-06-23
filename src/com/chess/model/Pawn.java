package com.chess.model;

import com.chess.enums.Color;
import com.chess.enums.moveType;
import com.chess.enums.pieceType;
import com.chess.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Color color){
        super(color);
    }

    @Override
    public pieceType getType() {
        return pieceType.Pawn;
    }

    @Override
    public List<Move> getPsuedoLegalMoves(Position pos, Board board, Game game) {
        List<Move> moves = new ArrayList<>();

        int row = pos.getRow();
        int col = pos.getCol();
        int direction;

        boolean startingRank, promotionRank;

        if(color==Color.White){
            direction = -1;
            startingRank = (row==6);
            promotionRank = (row==1);
        }
        else{
            direction = 1;
            startingRank = (row==1);
            promotionRank = (row==6);
        }

        int nextRow = row+1*direction;
        int leftCol = col-1;
        int rightCol = col+1;

        int twoRows = row+2*direction;

        Position onePos = new Position(nextRow, col);
        Position twoPos = new Position(twoRows, col);

        Piece oneMove = board.getPieceByPosition(onePos);
        Piece twoMove = board.getPieceByPosition(twoPos);


        Position rightPos = new Position(nextRow, rightCol);
        Piece rightTarget = board.getPieceByPosition((rightPos));

        Position leftPos = new Position(nextRow, leftCol);
        Piece leftTarget = board.getPieceByPosition(leftPos);

        if(promotionRank){

            if(board.isInsideBoard(nextRow, col) && oneMove == null) {
                Move m1 = new Move(moveType.Promotion, pos, onePos, this, null, new Queen(color));
                Move m2 = new Move(moveType.Promotion, pos, onePos, this, null, new Rook(color));
                Move m3 = new Move(moveType.Promotion, pos, onePos, this, null, new Knight(color));
                Move m4 = new Move(moveType.Promotion, pos, onePos, this, null, new Bishop(color));

                m1.setOriginalPawn(this);
                m2.setOriginalPawn(this);
                m3.setOriginalPawn(this);
                m4.setOriginalPawn(this);

                moves.add(m1);
                moves.add(m2);
                moves.add(m3);
                moves.add(m4);
            }

            if(board.isInsideBoard(nextRow, leftCol) && leftTarget!=null && leftTarget.getColor()!=color) {
                Move m1 = new Move(moveType.Promotion, pos, leftPos, this, leftTarget, new Queen(color));
                Move m2 = new Move(moveType.Promotion, pos, leftPos, this, leftTarget, new Rook(color));
                Move m3 = new Move(moveType.Promotion, pos, leftPos, this, leftTarget, new Knight(color));
                Move m4 = new Move(moveType.Promotion, pos, leftPos, this, leftTarget, new Bishop(color));

                m1.setOriginalPawn(this);
                m2.setOriginalPawn(this);
                m3.setOriginalPawn(this);
                m4.setOriginalPawn(this);

                moves.add(m1);
                moves.add(m2);
                moves.add(m3);
                moves.add(m4);
            }

            if(board.isInsideBoard(nextRow, rightCol) && rightTarget!=null && rightTarget.getColor()!=color ){
                Move m1 = new Move(moveType.Promotion, pos, rightPos, this, rightTarget, new Queen(color));
                Move m2 = new Move(moveType.Promotion, pos, rightPos, this, rightTarget, new Bishop(color));
                Move m3 = new Move(moveType.Promotion, pos, rightPos, this, rightTarget, new Rook(color));
                Move m4 = new Move(moveType.Promotion, pos, rightPos, this, rightTarget, new Knight(color));

                m1.setOriginalPawn(this);
                m2.setOriginalPawn(this);
                m3.setOriginalPawn(this);
                m4.setOriginalPawn(this);

                moves.add(m1);
                moves.add(m2);
                moves.add(m3);
                moves.add(m4);
            }

        }

        else{
            if(board.isInsideBoard(nextRow, col) && oneMove == null){
                moves.add(new Move(moveType.Normal,pos, onePos,this , null, null));
            }

            if(startingRank && board.isInsideBoard(nextRow, col) && oneMove == null && board.isInsideBoard(twoRows, col) && twoMove == null)
                moves.add(new Move(moveType.Normal,pos, new Position(twoRows, col), this, null, null));




            if(leftTarget != null && leftTarget.getColor()!=this.color){
                moves.add(new Move(moveType.Normal,pos, leftPos, this, leftTarget, null));
            }



            if(rightTarget != null && rightTarget.getColor()!=this.color){
                moves.add(new Move(moveType.Normal,pos, rightPos, this, rightTarget, null));
            }
        }

        //enpassant

        Move lastMove = game.getLastMove();

        if (lastMove != null && lastMove.getMovedPiece().getColor()!=this.color
         && lastMove.getMovedPiece() instanceof Pawn) {

            int lastFromRow = lastMove.getFrom().getRow();
            int lastToRow = lastMove.getTo().getRow();
            int lastToCol = lastMove.getTo().getCol();

            // check if pawn moved 2 squares
            if (Math.abs(lastFromRow - lastToRow) == 2) {

                row = pos.getRow();
                col = pos.getCol();

                // must be same row as opponent pawn after move
                if (row == lastToRow) {

                    // LEFT en passant
                    if (col - 1 == lastToCol) {
                        Position target = new Position(row + direction, col - 1);

                        if (board.isInsideBoard(target.getRow(), target.getCol())) {
                            moves.add(new Move(
                                    moveType.En_Passant,
                                    pos,
                                    target,
                                    this,
                                    lastMove.getMovedPiece(),
                                    null
                            ));
                        }
                    }

                    // RIGHT en passant
                    if (col + 1 == lastToCol) {
                        Position target = new Position(row + direction, col + 1);

                        if (board.isInsideBoard(target.getRow(), target.getCol())) {
                            moves.add(new Move(
                                    moveType.En_Passant,
                                    pos,
                                    target,
                                    this,
                                    lastMove.getMovedPiece(),
                                    null
                            ));
                        }
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public List<Position> getAttackedSquares(Position pos, Board board, Game game){
        List<Position> attacked = new ArrayList<>();

        int dir = (color == Color.White) ? -1 : 1;

        int row = pos.getRow();
        int col = pos.getCol();

        Position left = new Position(row + dir, col - 1);
        Position right = new Position(row + dir, col + 1);

        if(board.isInsideBoard(left.getRow(), left.getCol()))
            attacked.add(left);

        if(board.isInsideBoard(right.getRow(), right.getCol()))
            attacked.add(right);

        return attacked;
    }
}
