package com.chess.gui;

import com.chess.gui.listener.SquareClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SquarePanel extends JPanel {

    private int row;
    private int col;
    private SquareClickListener listener;

    private boolean selected;
    private boolean highlightGreen;
    private boolean highlightRed;
    private Color defaultColor;

    private JLabel pieceLabel;

    public SquarePanel(boolean isWhite, int row, int col, SquareClickListener listener) {

        this.row = row;
        this.col = col;
        this.listener = listener;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onSquareClick(row, col);

            }
        });

        setLayout(new BorderLayout());


        defaultColor = isWhite ? new Color(240, 217, 181)
                : new Color(181, 136, 99);
        setBackground(defaultColor);

        pieceLabel = new JLabel();
        pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pieceLabel.setVerticalAlignment(SwingConstants.CENTER);

        add(pieceLabel, BorderLayout.CENTER);
    }

    public void setPieceIcon(ImageIcon icon) {
        pieceLabel.setIcon(icon);
    }

    public void clearPiece() {
        pieceLabel.setIcon(null);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaintSquare();
    }

    public void setHighlightGreen(boolean value) {
        this.highlightGreen = value;
        repaintSquare();
    }

    public void setHighlightRed(boolean value) {
        this.highlightRed = value;
        repaintSquare();
    }

    private void repaintSquare() {

        if (selected) {
            setBackground(Color.BLUE);
        }
        else if (highlightGreen) {
            setBackground(Color.GREEN);
        }
        else if (highlightRed) {
            setBackground(Color.RED);
        }
        else {
            // normal chess colors
            setBackground(defaultColor);
        }
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}