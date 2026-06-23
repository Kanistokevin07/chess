package com.chess.gui;

import com.chess.enums.GameStatus;
import com.chess.enums.moveType;
import com.chess.game.Game;
import com.chess.gui.listener.SquareClickListener;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.enums.pieceType;
import com.chess.enums.Color;
import com.chess.model.Position;
import com.chess.gui.dialog.PromotionDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardPanel extends JPanel implements SquareClickListener {

    public static final int SIZE = 8;

    private Game game;
    private SquarePanel[][] squares = new SquarePanel[8][8];

    private Position selected;
    private List<Move> highlightedMoves = new ArrayList<>();

    public ChessBoardPanel(Game game) {
        this.game = game;
        setLayout(new GridLayout(SIZE, SIZE));
        boolean white = true;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                SquarePanel square = new SquarePanel(white, row, col, this);
                squares[row][col] = square;

                add(square);
                white = !white;
            }
            white = !white;
        }
        renderPieces();
    }

    @Override
    public void onSquareClick(int row, int col) {
        System.out.println("Clicked " + row + "," + col);

        Piece piece = game.getBoard().getPiece(row, col);

        Position clicked = new Position(row, col);

        // CASE 1: no selection yet
        if (selected == null) {

            if (piece == null) return;

            selected = clicked;

            highlightedMoves = game.getLegalMoves(selected);

            for(Move m: highlightedMoves)
                System.out.println(m);

            renderHighlights();

            return;
        }

        // CASE 2: second click → try move
        List<Move> candidates = new ArrayList<>();
        Move chosen = null;

        for(Move m : highlightedMoves){
            if(m.getTo().equals(clicked)){
                candidates.add(m);
            }
        }

        if(candidates.isEmpty()){

            resetHighlights();
            selected = null;
            highlightedMoves.clear();
            return;
        }

        if (candidates.size() == 1) {
            chosen = candidates.get(0);
            game.makeMove(chosen);
        }
        else if(candidates.get(0).getType()
                == moveType.Promotion){

            pieceType selectedType =
                    PromotionDialog.showPromotionDialog(this,
                            candidates.get(0).getMovedPiece().getColor());

            for(Move m : candidates){
                if(m.getPromotedPiece()
                        .getType() == selectedType){

                    chosen = m;
                    break;
                }
            }

            if(chosen != null){
                game.makeMove(chosen);
            }

        }

        System.out.println("chosen: " + chosen);

        resetHighlights();
        selected = null;
        highlightedMoves.clear();
        renderPieces();

        GameStatus status = game.getGameStatus();

        if (status != GameStatus.ONGOING) {
            if(status == GameStatus.CHECKMATE) {
                System.out.println("Checkmate");
                String msg = game.getCurrentTurn()==Color.White?"Black":"White";
                JOptionPane.showMessageDialog(this, status.toString() + " by " +
                                  msg
                        );
            }
            else{
                System.out.println(status.toString());
                JOptionPane.showMessageDialog(this, status.toString());
            }

        }
    }

    private void renderHighlights() {

        resetHighlights();

        // selected square
        squares[selected.getRow()][selected.getCol()].setSelected(true);

        for (Move m : highlightedMoves) {

            int r = m.getTo().getRow();
            int c = m.getTo().getCol();

            Piece target = game.getBoard().getPiece(r, c);

            if (target == null) {
                squares[r][c].setHighlightGreen(true);
            } else {
                squares[r][c].setHighlightRed(true);
            }
        }
    }

    public void resetHighlights() {


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j].setSelected(false);
                squares[i][j].setHighlightGreen(false);
                squares[i][j].setHighlightRed(false);
            }
        }
    }

    public void renderPieces() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = game.getBoard().getPiece(i, j);
                SquarePanel sq = squares[i][j];

                if (p == null) {
                    sq.clearPiece();
                    continue;
                }

                String file = getFileName(p);
                sq.setPieceIcon(ImageLoader.load(file));
            }
        }
    }

    private String getFileName(Piece p) {

        String color = (p.getColor() == Color.White) ? "w" : "b";

        switch (p.getType()) {
            case Pawn:   return color + "p.png";
            case Rook:   return color + "r.png";
            case Knight: return color + "n.png";
            case Bishop: return color + "b.png";
            case Queen:  return color + "q.png";
            case King:   return color + "k.png";
        }

        return null;
    }
}