package com.chess.gui.dialog;

import com.chess.enums.Color;
import com.chess.enums.pieceType;
import com.chess.gui.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class PromotionDialog {

    public static pieceType showPromotionDialog(
            Component parent,
            Color color
    ) {

        final pieceType[] selected = {null};

        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(parent),
                "Choose Promotion",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));

        String prefix =
                color == Color.White ? "w" : "b";

        JButton queenBtn =
                new JButton(ImageLoader.load(prefix + "q.png"));

        JButton rookBtn =
                new JButton(ImageLoader.load(prefix + "r.png"));

        JButton bishopBtn =
                new JButton(ImageLoader.load(prefix + "b.png"));

        JButton knightBtn =
                new JButton(ImageLoader.load(prefix + "n.png"));

        queenBtn.addActionListener(e -> {
            selected[0] = pieceType.Queen;
            dialog.dispose();
        });

        rookBtn.addActionListener(e -> {
            selected[0] = pieceType.Rook;
            dialog.dispose();
        });

        bishopBtn.addActionListener(e -> {
            selected[0] = pieceType.Bishop;
            dialog.dispose();
        });

        knightBtn.addActionListener(e -> {
            selected[0] = pieceType.Knight;
            dialog.dispose();
        });

        panel.add(queenBtn);
        panel.add(rookBtn);
        panel.add(bishopBtn);
        panel.add(knightBtn);

        dialog.add(panel);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return selected[0];
    }
}