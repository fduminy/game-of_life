package fr.duminy.game.life;

import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.fail;

class GameOfLifeTest {
    @Test
    void start() throws InterruptedException {
        GameOfLife gameOfLife = new GameOfLife();
        gameOfLife.start();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            fail(e);
        }
        gameOfLife.stop();
        gameOfLife.waitForStop();
    }
}