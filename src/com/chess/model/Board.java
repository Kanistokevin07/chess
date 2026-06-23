package com.chess.model;

import com.chess.enums.Color;
import com.chess.enums.moveType;
import com.chess.enums.pieceType;

public class Board {
    private Piece[][] squares;

    public Board(){
        squares = new Piece[8][8];
    }

    public Piece getPiece(int row, int col){
        return squares[row][col];
    }

    public Piece getPieceByPosition(Position pos){
        int r = pos.getRow();
        int c = pos.getCol();
        return isInsideBoard(r, c)?squares[r][c]:null;
    }

    public Piece[][] getSquares(){
        return squares;
    }

    public boolean isInsideBoard(int row, int col) {
        return row >= 0 &&
                row < 8 &&
                col >= 0 &&
                col < 8;
    }

    public Position findKing(Color color){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                Piece piece = getPiece(i,j);
                if(piece!=null && piece.getType()==pieceType.King && piece.getColor()==color){
                    return new Position(i,j);
                }
            }
        }
        return new Position(0,0);
    }

    public void applyMove(Move m){
        Piece movedPiece = m.getMovedPiece();
        switch(m.getType()){
            case Normal:
                setPiece(m.getTo(), movedPiece);
                break;
            case Promotion:
                setPiece(m.getTo(), m.getPromotedPiece());
                break;
            case Castle_KingSide:
                setPiece(m.getTo(), m.getMovedPiece());
                int row = m.getFrom().getRow();
                Piece rook = getPiece(row, 7);

                setPiece(new Position(row, 5), rook);
                setPiece(new Position(row, 7), null);

                break;
            case Castle_QueenSide:
                setPiece(m.getTo(), m.getMovedPiece());
                int row1 = m.getFrom().getRow();
                Piece rook1 = getPiece(row1, 0);

                setPiece(new Position(row1, 3), rook1);
                setPiece(new Position(row1, 0), null);

                break;
            case En_Passant:
                setPiece(m.getTo(), movedPiece);

                Position from = m.getFrom();
                Position to = m.getTo();

                Position capturedPos = new Position(from.getRow(), to.getCol());

                setPiece(capturedPos, null);
                break;
        }

        setPiece(m.getFrom(), null);

    }

    public void undoMove(Move m){
        Piece capturedPiece = m.getCapturedPiece();
        Piece movedPiece = m.getMovedPiece();

        if(m.getType() == moveType.En_Passant){

            Position from = m.getFrom();
            Position to = m.getTo();

            setPiece(from, movedPiece);
            setPiece(to, null);


            Position capturedPos = new Position(from.getRow() , to.getCol());

            setPiece(capturedPos, capturedPiece);
        }
        else{

            if(m.getType() == moveType.Promotion) {
                setPiece(m.getFrom(), m.getOriginalPawn());
                //m.getPromotedPiece().setHasMoved(false);
            }

            else
                setPiece(m.getFrom(), movedPiece);



            setPiece(m.getTo(), capturedPiece);

            // ---------------- CASTLING UNDO ----------------
            if(m.getType() == moveType.Castle_KingSide){

                int row = m.getFrom().getRow();
                Piece rook = getPiece(row, 5);

                setPiece(new Position(row, 7), rook);
                setPiece(new Position(row, 5), null);

                rook.setHasMoved(false);
                m.getMovedPiece().setHasMoved(false);
            }

            if(m.getType() == moveType.Castle_QueenSide){

                int row = m.getFrom().getRow();
                Piece rook = getPiece(row, 3);

                setPiece(new Position(row, 0), rook);
                setPiece(new Position(row, 3), null);

                rook.setHasMoved(false);
                m.getMovedPiece().setHasMoved(false);
            }

        }



    }





    public void setPiece(Position pos, Piece piece){
        squares[pos.getRow()][pos.getCol()] = piece;
    }
}
