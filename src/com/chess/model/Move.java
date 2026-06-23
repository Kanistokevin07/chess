package com.chess.model;
import com.chess.enums.moveType;
import com.chess.enums.pieceType;

public class Move {
    private Position from;
    private Position to;
    private Piece movedPiece;
    private Piece capturedPiece;
    private moveType type;
    private Piece promotedPiece;
    private boolean movedPiecePrevHasMoved;
    private Piece originalPawn;

    public Move(moveType type,Position from, Position to, Piece movedPiece, Piece capturedPiece, Piece promotedPiece){
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.type = type;
        this.promotedPiece = promotedPiece;
        movedPiecePrevHasMoved = false;
    }

    public boolean getMovedPiecePrevHasMoved() {
        return movedPiecePrevHasMoved;
    }

    public void setOriginalPawn(Piece pawn){
        originalPawn = pawn;
    }

    public Piece getOriginalPawn(){
        return originalPawn;
    }

    public void setMovedPiecePrevHasMoved(boolean flag){
        movedPiecePrevHasMoved = flag;
    }

    public moveType getType(){ return type; }

    public Position getTo(){
         return to;
    }

    public Position getFrom(){
        return from;
    }

    public void setMovedPiece(Piece movedPiece){
        this.movedPiece = movedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece){
        this.capturedPiece = capturedPiece;
    }

    public Piece getMovedPiece(){
        return movedPiece;
    }

    public Piece getCapturedPiece(){
        return capturedPiece;
    }

    public Piece getPromotedPiece(){ return promotedPiece; }

    @Override
    public String toString() {

        String fromStr = posToString(from);
        String toStr = posToString(to);

        String captured = (capturedPiece == null)
                ? "none"
                : capturedPiece.getType().toString().toLowerCase();

        return fromStr + " → " + toStr +
                " | Captured: " + captured +
                " | Type: " + type;
    }

    private String posToString(Position p) {
        char file = (char) ('a' + p.getCol());
        int rank = 8 - p.getRow();
        return "" + file + rank;
    }

}
