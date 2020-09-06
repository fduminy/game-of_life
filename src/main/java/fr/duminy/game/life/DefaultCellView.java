package fr.duminy.game.life;

public class DefaultCellView implements CellView {
    private final Game game;
    private int x;
    private int y;

    public DefaultCellView(Game game) {
        this.game = game;
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
        return game.isAlive(targetX, targetY);
    }

    private boolean outOfBounds(int coordinate) {
        return (coordinate < 0) || (coordinate >= game.getSize());
    }
}
