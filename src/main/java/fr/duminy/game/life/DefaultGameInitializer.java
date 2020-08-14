package fr.duminy.game.life;

import static java.lang.Math.random;

public class DefaultGameInitializer implements GameInitializer {
    @Override
    public void initialize(Game game) {
        for (CellIterator cellIterator = game.iterator(); cellIterator.hasNext(); cellIterator.next()) {
            game.setAlive(cellIterator.getX(), cellIterator.getY(), random() > 0.5);
        }
    }
}
