package fr.duminy.game.life;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static fr.duminy.game.life.DefaultGameChangerTest.NUMBER_OF_CELLS;

class StubCellIterator implements CellIterator {
    private int index;
    private int x;
    private int y;
    private CellView cellView;

    public StubCellIterator(int x, int y) {
        this(x, y, null);
    }

    public StubCellIterator(int x, int y, CellView cellView) {
        index = x + y * GAME_SIZE;
        this.x = x;
        this.y = y;
        this.cellView = cellView;
    }

    @Override
    public void next() {
        index++;
        x++;
        if (x >= GAME_SIZE) {
            x = 0;
            y++;
        }
    }

    @Override
    public boolean hasNext() {
        return index < NUMBER_OF_CELLS;
    }

    @Override
    public boolean isAlive() {
        return false;
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

    @Override
    public CellView cellView() {
        return cellView;
    }
}
