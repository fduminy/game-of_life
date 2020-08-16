package fr.duminy.game.life;

import static java.lang.Math.random;

public class DefaultGameModelInitializer implements GameModelInitializer {
    @Override
    public void initialize(MutableGameModel gameModel) {
        for (CellIterator cellIterator = gameModel.iterator(); cellIterator.hasNext(); cellIterator.next()) {
            gameModel.setAlive(cellIterator.getX(), cellIterator.getY(), random() > 0.5);
        }
    }
}
