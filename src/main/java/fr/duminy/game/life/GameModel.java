package fr.duminy.game.life;

public interface GameModel {
    int getSize();

    boolean isAlive(int x, int y);

    CellIterator iterator();
}
