package fr.duminy.game.life;

import java.util.BitSet;

public class DefaultGameModel implements MutableGameModel {
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

    static class DefaultCellIterator implements CellIterator {
        private final GameModel game;
        private final int numberOfCells;
        private int index;
        private int x;
        private int y;

        DefaultCellIterator(GameModel game) {
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
        public CellView cellView() {
            DefaultCellView cellView = new DefaultCellView(game);
            cellView.setLocation(x, y);
            return cellView;
        }
    }

    static class DefaultCellView implements CellView {
        private final GameModel model;
        private int x;
        private int y;

        DefaultCellView(GameModel model) {
            this.model = model;
        }

        @Override
        public void setLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean isAlive(int deltaX, int deltaY) {
            int targetX = x + deltaX;
            int targetY = y + deltaY;
            if (outOfBounds(targetX) || outOfBounds(targetY)) {
                return false;
            }
            return model.isAlive(targetX, targetY);
        }

        private boolean outOfBounds(int coordinate) {
            return (coordinate < 0) || (coordinate >= model.getSize());
        }
    }
}
