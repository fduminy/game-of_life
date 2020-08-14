package fr.duminy.game.life;

import static java.lang.Math.random;

public class DefaultGameInitializer implements GameInitializer {
    @Override
    public void initialize(Game game) {
        int size = game.getSize();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                game.setAlive(x, y, random() > 0.5);
            }
        }
    }
}
