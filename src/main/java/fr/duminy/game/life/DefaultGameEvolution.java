package fr.duminy.game.life;

public class DefaultGameEvolution implements GameEvolution {
    private final Game game;
    private final GameModel gameModel;

    public DefaultGameEvolution(Game game) {
        this.game = game;
        this.gameModel = new DefaultGameModel(game.getSize());
    }

    @Override
    public void setAlive(int x, int y, boolean alive) {
        gameModel.setAlive(x, y, alive);
    }

    @Override
    public void update() {
        for (CellIterator cellIterator = gameModel.iterator(); cellIterator.hasNext(); cellIterator.next()) {
            game.setAlive(cellIterator.getX(), cellIterator.getY(), cellIterator.isAlive());
        }
    }
}
