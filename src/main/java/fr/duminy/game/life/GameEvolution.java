package fr.duminy.game.life;

public interface GameEvolution {
    void setAlive(int x, int y, boolean alive);

    void update();
}
