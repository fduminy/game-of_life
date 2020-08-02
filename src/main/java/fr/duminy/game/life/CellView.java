package fr.duminy.game.life;

public interface CellView {
    enum Position {
        TOP_LEFT(0, 0),
        TOP(1, 0),
        TOP_RIGHT(2, 0),

        RIGHT(2, 1),
        CENTER(1, 1),
        LEFT(0, 1),

        BOTTOM_LEFT(0, 2),
        BOTTOM(1, 2),
        BOTTOM_RIGHT(2, 2);

        private final int x;
        private final int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    boolean isAlive(Position position);
}
