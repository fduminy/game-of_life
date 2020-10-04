package fr.duminy.game.life;

public class DefaultCellView implements CellView {
    private final GameModel model;
    private int x;
    private int y;

    public DefaultCellView(GameModel model) {
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
