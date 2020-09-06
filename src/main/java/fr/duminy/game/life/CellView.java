package fr.duminy.game.life;

public interface CellView {
    void setLocation(int x, int y);

    boolean isAlive(int deltaX, int deltaY);
}
