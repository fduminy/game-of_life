package fr.duminy.game.life;

public class DefaultCellIterator implements CellIterator {
    private final Game game;
    private int index;
    private int x;
    private int y;
    private int numberOfCells;

    public DefaultCellIterator(Game game) {
        this.game = game;
        numberOfCells = game.getSize() * game.getSize();
    }

    @Override
    public void next() {
        if (!hasNext()) {
            return;
        }

        index++;
        x++;
        if (x >= game.getSize()) {
            x = 0;
            y++;
        }
    }

    @Override
    public boolean hasNext() {
        return index < numberOfCells;
    }

    @Override
    public boolean isAlive() {
        return game.isAlive(x, y);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
