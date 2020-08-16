package fr.duminy.game.life;

import java.util.BitSet;

public class DefaultGameModel implements GameModel {
    private final int size;
    private final BitSet alive;

    public DefaultGameModel(int size) {
        this.size = size;
        alive = new BitSet(size * size);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isAlive(int x, int y) {
        return alive.get(index(x, y));
    }

    @Override
    public void setAlive(int x, int y, boolean alive) {
        if (alive) {
            this.alive.set(index(x, y));
        } else {
            this.alive.clear(index(x, y));
        }
    }

    @Override
    public CellIterator iterator() {
        return new DefaultCellIterator(this);
    }

    private int index(int x, int y) {
        return x + y * size;
    }
}
