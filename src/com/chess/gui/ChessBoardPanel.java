package com.chess.gui;

import com.chess.ai.AIPlayer;
import com.chess.enums.*;
import com.chess.enums.Color;
import com.chess.game.Game;
import com.chess.gui.listener.SquareClickListener;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.Position;
import com.chess.gui.dialog.PromotionDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChessBoardPanel extends JPanel implements SquareClickListener {

    public static final int SIZE = 8;

    private Game game;
    private SquarePanel[][] squares = new SquarePanel[8][8];
    private AIPlayer aiPlayer;
    private GameMode gameMode;

    private final ExecutorService aiExecutor =
            Executors.newSingleThreadExecutor();
    private boolean aiThinking = false;
    private JLabel statusLabel;

    private Position selected;
    private List<Move> highlightedMoves = new ArrayList<>();

    private JButton undoBtn;
    private JButton redoBtn;

    public ChessBoardPanel(Game game, GameMode gameMode) {
        this.game = game;
        this.gameMode = gameMode;

        if (gameMode == GameMode.PLAYER_VS_AI) {
            aiPlayer = new AIPlayer(Color.Black, 4);
        }

        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        boolean white = true;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                SquarePanel square = new SquarePanel(white, row, col, this);
                squares[row][col] = square;

                boardPanel.add(square);
                white = !white;
            }
            white = !white;
        }

        JPanel controlPanel = new JPanel(new FlowLayout());

        undoBtn = new JButton("Undo");
        redoBtn = new JButton("Redo");

        statusLabel = new JLabel("Your turn");

        controlPanel.add(undoBtn);
        controlPanel.add(redoBtn);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        undoBtn.addActionListener(e -> onUndo());
        redoBtn.addActionListener(e -> onRedo());

        renderPieces();
    }

    @Override
    public void onSquareClick(int row, int col) {
        System.out.println("Clicked " + row + "," + col);
        if(aiThinking)
            return;

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

        for (Move m : highlightedMoves) {
            if (m.getTo().equals(clicked)) {
                candidates.add(m);
            }
        }

        if (candidates.isEmpty()) {
            resetHighlights();
            selected = null;
            highlightedMoves.clear();
            return;
        }

        // Normal move
        if (candidates.size() == 1) {
            chosen = candidates.get(0);
        }

        // Promotion
        else if (candidates.get(0).getType() == moveType.Promotion) {

            pieceType selectedType =
                    PromotionDialog.showPromotionDialog(
                            this,
                            candidates.get(0).getMovedPiece().getColor());

            for (Move m : candidates) {
                if (m.getPromotedPiece().getType() == selectedType) {
                    chosen = m;
                    break;
                }
            }
        }

// ---------------- Execute Human Move ----------------

        if (chosen != null && game.makeMove(chosen)) {

            // Show player's move immediately
            renderPieces();

            // Clear UI state
            resetHighlights();
            selected = null;
            highlightedMoves.clear();

            // Check whether game already ended
            GameStatus status = game.getGameStatus();

            if (gameMode == GameMode.PLAYER_VS_AI && status == GameStatus.ONGOING &&
                    game.getCurrentTurn() == Color.Black) {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                updateStatus("AI is thinking...");

                aiExecutor.submit(() -> {
                    aiThinking = true;
                    Move aiMove = aiPlayer.chooseMove(game);

                    SwingUtilities.invokeLater(() -> {
                        if(aiMove != null) {
                            game.makeMove(aiMove);
                            renderPieces();
                            updateStatus("Your turn");
                        }

                        setCursor(Cursor.getDefaultCursor());
                        GameStatus aiStatus =
                                game.getGameStatus();

                        if(aiStatus != GameStatus.ONGOING) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    aiStatus.toString()
                            );
                        }

                        aiThinking = false;
                    });
                });
            }

            if (status != GameStatus.ONGOING) {

                if (status == GameStatus.CHECKMATE) {

                    String winner =
                            game.getCurrentTurn() == Color.White
                                    ? "Black"
                                    : "White";

                    JOptionPane.showMessageDialog(
                            this,
                            "CHECKMATE\nWinner : " + winner
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            status.toString()
                    );
                }
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

    private void onUndo() {
        if(gameMode == GameMode.PLAYER_VS_AI){
            game.undo();
        }
        game.undo();

        selected = null;
        highlightedMoves.clear();

        resetHighlights();
        renderPieces();
    }

    private void onRedo() {
        game.redo();

        selected = null;
        highlightedMoves.clear();

        resetHighlights();
        renderPieces();
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

    private void updateStatus(String text){
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(text);
        });
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