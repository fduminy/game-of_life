package fr.duminy.game.life;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;

class StubCellIterator implements CellIterator {
    private int index;
    private int x;
    private int y;
    private CellView cellView;
    private final int gameSize;

    public StubCellIterator(int x, int y) {
        this(x, y, null);
    }

    public StubCellIterator(int x, int y, CellView cellView) {
        this(x, y, cellView, GAME_SIZE);
    }

    public StubCellIterator(int x, int y, CellView cellView, int gameSize) {
        this.gameSize = gameSize;
        this.cellView = cellView;
        locate(x, y);
    }

    @Override
    public void next() {
        index++;
        x++;
        if (x >= gameSize) {
            x = 0;
            y++;
        }
    }

    @Override
    public boolean hasNext() {
        return index < (gameSize * gameSize);
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
    public CellView cellView() {
        return cellView;
    }

    public void locate(int x, int y) {
        index = x + y * gameSize;
        this.x = x;
        this.y = y;
    }
}
