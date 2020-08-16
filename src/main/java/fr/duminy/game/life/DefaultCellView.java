package fr.duminy.game.life;

public class DefaultCellView implements CellView {
    private final Game game;
    private final int x;
    private final int y;

    public DefaultCellView(Game game, int x, int y) {
        this.game = game;
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
