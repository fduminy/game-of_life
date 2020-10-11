package fr.duminy.game.life;

import java.util.function.Supplier;

public class DefaultGameChanger implements GameChanger {
    private MutableGameModel nextGeneration;

    public DefaultGameChanger(Supplier<MutableGameModel> nextGenerationSupplier) {
        nextGeneration = nextGenerationSupplier.get();
    }

    @Override
    public void evolve(Game game, Rule rule) {
        for (CellIterator cellIterator = game.iterator(); cellIterator.hasNext(); cellIterator.next()) {
            nextGeneration.setAlive(cellIterator.getX(), cellIterator.getY(), rule.evolve(cellIterator.cellView()));
        }
        nextGeneration = game.setModel(nextGeneration); // swap my model and game's one
    }
}
