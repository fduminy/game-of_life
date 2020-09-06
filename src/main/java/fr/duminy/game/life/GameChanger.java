package fr.duminy.game.life;

public interface GameChanger {
    void evolve(Game game, GameEvolution gameEvolution, CellViewSupplier cellViewSupplier, Rule rule);
}
