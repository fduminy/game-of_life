package fr.duminy.game.life;

import java.util.function.Supplier;

public class DefaultGameEvolution implements GameEvolution {
    private final Game game;
    private MutableGameModel gameModel;

    public DefaultGameEvolution(Game game, Supplier<MutableGameModel> gameModelSupplier) {
        this.game = game;
        this.gameModel = gameModelSupplier.get();
    }

    @Override
    public void setAlive(int x, int y, boolean alive) {
        gameModel.setAlive(x, y, alive);
    }

    @Override
    public void update() {
        gameModel = game.setModel(gameModel);
    }
}
