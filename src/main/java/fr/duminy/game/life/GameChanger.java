package fr.duminy.game.life;

import java.util.function.Function;

public interface GameChanger {
    void evolve(Game game, GameEvolution gameEvolution, Function<CellIterator, CellView> cellViewSupplier, Rule rule);
}
