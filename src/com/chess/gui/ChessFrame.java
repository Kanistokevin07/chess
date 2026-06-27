package com.chess.gui;

import com.chess.controller.GameFactory;
import com.chess.enums.GameMode;
import com.chess.game.Game;

import javax.swing.*;
import java.awt.*;

public class ChessFrame extends JFrame {

    private Game game;
    private ChessBoardPanel board;

    public ChessFrame() {

        setTitle("Java Chess Engine");
        setSize(750, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showMainMenu();
    }

    private void showMainMenu() {

        JPanel menu = new JPanel(new GridBagLayout());

        JPanel buttons = new JPanel(new GridLayout(3, 1, 10, 15));

        JButton pvp = new JButton("Player vs Player");
        JButton pvai = new JButton("Player vs AI");
        JButton exit = new JButton("Exit");

        buttons.add(pvp);
        buttons.add(pvai);
        buttons.add(exit);

        menu.add(buttons);

        setContentPane(menu);

        pvp.addActionListener(e ->
                startGame(GameMode.PLAYER_VS_PLAYER));

        pvai.addActionListener(e ->
                startGame(GameMode.PLAYER_VS_AI));

        exit.addActionListener(e -> System.exit(0));

        revalidate();
        repaint();
    }

    private void startGame(GameMode mode) {

        game = GameFactory.createGame();
        board = new ChessBoardPanel(game, mode);

        JButton newGame = new JButton("Main Menu");
        newGame.addActionListener(e -> showMainMenu());

        JPanel gamePanel = new JPanel(new BorderLayout());

        gamePanel.add(board, BorderLayout.CENTER);
        gamePanel.add(newGame, BorderLayout.SOUTH);

        setContentPane(gamePanel);

        revalidate();
        repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new ChessFrame().setVisible(true));
    }
}