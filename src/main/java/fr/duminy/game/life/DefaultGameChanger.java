package fr.duminy.game.life;

import java.util.function.Function;

public class DefaultGameChanger implements GameChanger {
    void evolve(Game game, GameEvolution gameEvolution, Function<CellIterator, CellView> cellViewSupplier, Rule rule) {
        for (CellIterator cellIterator = game.iterator(); cellIterator.hasNext(); cellIterator.next()) {
            gameEvolution.setAlive(cellIterator.getX(), cellIterator.getY(), rule.evolve(cellViewSupplier.apply(cellIterator)));
        }
    }
}
