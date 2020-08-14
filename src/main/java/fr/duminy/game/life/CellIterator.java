package fr.duminy.game.life;

public interface CellIterator {
    void next();

    boolean hasNext();

    boolean isAlive();

    int getX();

    int getY();

    int getIndex();
}
