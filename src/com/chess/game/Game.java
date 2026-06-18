package com.chess.game;

import com.chess.controller.GameFactory;
import com.chess.enums.Color;
import com.chess.enums.GameStatus;
import com.chess.enums.pieceType;
import com.chess.model.*;

import java.util.ArrayList;
import java.util.List;

public class Game {
    Board board;
    Color currentTurn;
    List<Move> moveHistory;
    private int halfMoveClock;

    public Game(Board board){
        moveHistory = new ArrayList<>();
        currentTurn = Color.White;
        this.board = board;
        halfMoveClock = 0;
    }

    public void switchTurn(){
        if(currentTurn==Color.White) currentTurn = Color.Black;
        else currentTurn = Color.White;
    }

    public Board getBoard(){ return board; }

    public boolean makeMove(Move move){
        Piece piece = board.getPieceByPosition(move.getFrom());

        if(piece == null)
            return false;

        if(piece.getColor() != currentTurn)
            return false;

        boolean found = false;

        for(Move m: getLegalMoves(move.getFrom())){
            if(m.getTo().equals(move.getTo())){
                found = true;
                break;
            }
        }

        if(!found)
            return false;

        board.applyMove(move);

        if(piece.getType() == pieceType.Pawn || move.getCapturedPiece()!=null)
            halfMoveClock = 0;
        else
            halfMoveClock++;

        piece.setHasMoved(true);
        moveHistory.add(move);
        switchTurn();

        return true;
    }
    public void reset() {
        this.board = new Board();
        GameFactory.setUpInitialPosition(this.board);

        this.moveHistory.clear();
        this.currentTurn = Color.White;

    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public List<Move> getLegalMoves(Position pos){
        int row = pos.getRow();
        int col = pos.getCol();
        Piece piece = board.getPieceByPosition(pos);
        List<Move> legalMoves = new ArrayList<>();

        if(piece==null) return legalMoves;

        for(Move m: piece.getPsuedoLegalMoves(pos, board, this)){
            board.applyMove(m);
            if(!isKingInCheck(piece.getColor()))
                legalMoves.add(m);
            board.undoMove(m);
        }

        return legalMoves;
    }

    public Move getLastMove(){
        return moveHistory.isEmpty()?null:moveHistory.getLast();
    }

    public List<Move> getAllLegalMoves(Color color){
        List<Move> allMoves = new ArrayList<>();

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                Piece piece = board.getPiece(i,j);

                if(piece==null || piece.getColor()!=color)
                    continue;

                allMoves.addAll(getLegalMoves(new Position(i,j)));
            }
        }
        return allMoves;
    }

    public boolean isSquareUnderAttack(Position pos, Color color){

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                Piece target = board.getPiece(i,j);
                if(target!=null && target.getColor()!=color){
                    for(Move m: target.getPsuedoLegalMoves(new Position(i,j), board, this)){
                        if(m.getTo().equals(pos)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isKingInCheck(Color color){

        Position kingPosition = board.findKing(color);
        return isSquareUnderAttack(kingPosition, color);
    }

    public boolean insufficientMaterial(){

        int whiteBishops = 0;
        int blackBishops = 0;

        int whiteKnights = 0;
        int blackKnights = 0;

        int whiteQueens = 0;
        int blackQueens = 0;

        int whiteRooks = 0;
        int blackRooks = 0;

        int whitePawns = 0;
        int blackPawns = 0;

        int whitePieces = 0;
        int blackPieces = 0;

        List<Position> whiteBishopPos = new ArrayList<>();
        List<Position> blackBishopPos = new ArrayList<>();

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){

                Piece piece = board.getPiece(i, j);

                if(piece == null) continue;

                if(piece.getColor() == Color.White){
                    whitePieces++;

                    switch(piece.getType()){
                        case Bishop -> {
                            whiteBishops++;
                            whiteBishopPos.add(new Position(i, j));
                        }
                        case Knight -> whiteKnights++;
                        case Queen  -> whiteQueens++;
                        case Rook   -> whiteRooks++;
                        case Pawn   -> whitePawns++;
                    }

                } else {
                    blackPieces++;

                    switch(piece.getType()){
                        case Bishop -> {
                            blackBishops++;
                            blackBishopPos.add(new Position(i, j));
                        }
                        case Knight -> blackKnights++;
                        case Queen  -> blackQueens++;
                        case Rook   -> blackRooks++;
                        case Pawn   -> blackPawns++;
                    }
                }
            }
        }

        //king vs king
        if(whitePieces == 1 && blackPieces == 1)
            return true;

        //king + minor vs king
        if(whitePieces == 2 && blackPieces == 1){
            if((whiteBishops == 1 || whiteKnights == 1) &&
                    whiteQueens == 0 && whiteRooks == 0 && whitePawns == 0)
                return true;
        }


        if(blackPieces == 2 && whitePieces == 1){
            if((blackBishops == 1 || blackKnights == 1) &&
                    blackQueens == 0 && blackRooks == 0 && blackPawns == 0)
                return true;
        }

        // Bishop + king vs Bishop + king (same color)
        if (whitePieces == 2 && blackPieces == 2 &&
                whiteBishops == 1 && blackBishops == 1 &&
                whiteKnights == 0 && blackKnights == 0 &&
                whiteQueens == 0 && blackQueens == 0 &&
                whiteRooks == 0 && blackRooks == 0 &&
                whitePawns == 0 && blackPawns == 0) {

            Position wBishop = whiteBishopPos.get(0);
            Position bBishop = blackBishopPos.get(0);

            boolean sameColorSquare =
                    (wBishop.getRow() + wBishop.getCol()) % 2 ==
                            (bBishop.getRow() + bBishop.getCol()) % 2;

            if(sameColorSquare)
                return true;
        }


        return false;
    }

    public boolean isDraw(Color color){
        return halfMoveClock >= 100 || insufficientMaterial();
    }


    public boolean isCheckmate(Color color){
        return isKingInCheck(color) && getAllLegalMoves(color).isEmpty();
    }

    public boolean isStalemate(Color color){
        return !isKingInCheck(color) && getAllLegalMoves(color).isEmpty();
    }

    public boolean isGameOver(){
        return isCheckmate(Color.White) || isCheckmate(Color.Black) ||
                isStalemate(Color.Black) || isStalemate(Color.White) || isDraw(Color.White)
                || isDraw(Color.Black);
    }

    public GameStatus getGameStatus(){
        if(isDraw(Color.White) || isDraw(Color.Black))
            return GameStatus.DRAW;

        if(isStalemate(Color.White) || isStalemate(Color.Black))
            return GameStatus.STALEMATE;

        if(isCheckmate(Color.Black) || isCheckmate(Color.Black))
            return GameStatus.CHECKMATE;

        return GameStatus.ONGOING;
    }

}
