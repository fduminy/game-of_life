package fr.duminy.game.life;

import javax.swing.*;
import java.awt.image.BufferedImage;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class DefaultGameViewer implements GameViewer {
    private JLabel label;
    private BufferedImage image;

    public DefaultGameViewer() {
    }

    @Override
    public void view(Game game) {
        int gameSize = game.getSize();
        if (image == null) {
            label = initView(gameSize);
        }

        for (int y = 0; y < gameSize; y++) {
            for (int x = 0; x < gameSize; x++) {
                boolean alive = game.isAlive(x, y);
                image.setRGB(x, y, alive ? WHITE.getRGB() : BLACK.getRGB());
            }
        }
        label.repaint();
    }

    private JLabel initView(int gameSize) {
        image = new BufferedImage(gameSize, gameSize, TYPE_BYTE_GRAY);

        ImageIcon imageIcon = new ImageIcon(image);
        JLabel label = new JLabel(imageIcon);
        JFrame frame = new JFrame("Game of life");
        frame.setContentPane(label);
        frame.pack();
        frame.setSize(frame.getPreferredSize());
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        return label;
    }
}
