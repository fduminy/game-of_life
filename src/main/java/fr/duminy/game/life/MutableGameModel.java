package fr.duminy.game.life;

public interface MutableGameModel extends GameModel {
    void setAlive(int x, int y, boolean alive);
}
