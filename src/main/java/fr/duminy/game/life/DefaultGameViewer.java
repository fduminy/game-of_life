package fr.duminy.game.life;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class DefaultGameViewer implements GameViewer {
    JFrame window;
    private final GameLoop gameLoop;
    private JLabel label;
    private BufferedImage image;

    public DefaultGameViewer(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    @Override
    public void view(Game game) {
        int gameSize = game.getSize();
        initViewIfNotDone(gameSize);
        for (CellIterator cellIterator = game.iterator(); cellIterator.hasNext(); cellIterator.next()) {
            boolean alive = cellIterator.isAlive();
            image.setRGB(cellIterator.getX(), cellIterator.getY(), alive ? WHITE.getRGB() : BLACK.getRGB());
        }
        label.repaint();
    }

    private void initViewIfNotDone(int gameSize) {
        if (window != null) {
            return;
        }

        image = new BufferedImage(gameSize, gameSize, TYPE_BYTE_GRAY);

        ImageIcon imageIcon = new ImageIcon(image);
        label = new JLabel(imageIcon);
        window = new JFrame("Game of life");
        window.setContentPane(label);
        window.pack();
        window.setSize(window.getPreferredSize());
        window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gameLoop.stop();
            }
        });
    }
}
