package fr.duminy.game.life;

public interface GameModel {
    int getSize();

    boolean isAlive(int x, int y);

    void setAlive(int x, int y, boolean alive);

    CellIterator iterator();
}
