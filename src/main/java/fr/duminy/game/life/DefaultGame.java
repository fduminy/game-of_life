package fr.duminy.game.life;

public class DefaultGame implements Game {
    private final GameModel gameModel;

    public DefaultGame(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public int getSize() {
        return gameModel.getSize();
    }

    @Override
    public boolean isAlive(int x, int y) {
        return gameModel.isAlive(x, y);
    }

    @Override
    public void setAlive(int x, int y, boolean alive) {
        gameModel.setAlive(x, y, alive);
    }

    @Override
    public CellIterator iterator() {
        return gameModel.iterator();
    }
}
