package fr.duminy.game.life;

public class DefaultGame implements Game {
    private MutableGameModel gameModel;

    public DefaultGame(MutableGameModel gameModel) {
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
    public CellIterator iterator() {
        return gameModel.iterator();
    }

    @Override
    public MutableGameModel setModel(MutableGameModel gameModel) {
        MutableGameModel oldModel = this.gameModel;
        this.gameModel = gameModel;
        return oldModel;
    }
}
