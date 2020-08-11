package fr.duminy.game.life;

import java.util.function.BiFunction;

public class DefaultGameChanger implements GameChanger {
    void evolve(Game game, GameEvolution gameEvolution, BiFunction<Integer, Integer, CellView> cellViewSupplier, Rule rule) {
        int size = game.getSize();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                gameEvolution.setAlive(x, y, rule.evolve(cellViewSupplier.apply(x, y)));
            }
        }
    }
}