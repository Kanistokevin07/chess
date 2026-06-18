package com.chess.gui;

import com.chess.controller.GameFactory;
import com.chess.game.Game;

import javax.swing.*;
import java.awt.*;

public class ChessFrame extends JFrame {

    private Game game;
    private ChessBoardPanel board;

    public ChessFrame() {

        setTitle("Chess Game");
        setSize(750, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        game = GameFactory.createGame();
        board = new ChessBoardPanel(game);

        add(board);

        JButton newGame = new JButton("New Game");

        newGame.addActionListener(e -> {
            game.reset();
            board.renderPieces();
            board.resetHighlights();
        });

        add(newGame, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChessFrame().setVisible(true);
        });
    }
}