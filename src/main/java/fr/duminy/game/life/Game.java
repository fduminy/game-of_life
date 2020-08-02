package fr.duminy.game.life;

public interface Game {
    int getSize();

    boolean isAlive(int x, int y);
}
