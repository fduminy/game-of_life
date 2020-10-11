package fr.duminy.game.life;

public interface GameChanger {
    void evolve(Game game, GameEvolution gameEvolution, Rule rule);
}
