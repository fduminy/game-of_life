package fr.duminy.game.life;

import com.google.code.tempusfugit.temporal.Sleeper;
import com.google.code.tempusfugit.temporal.ThreadSleep;

import java.util.function.Function;

import static com.google.code.tempusfugit.temporal.Duration.millis;
import static java.lang.Thread.sleep;

@SuppressWarnings("BusyWait")
public class GameOfLife {
    public static void main(String[] args) throws InterruptedException {
        GameOfLife gameOfLife = new GameOfLife();
        gameOfLife.start();
        gameOfLife.waitForStop();
    }

    private final GameLoop gameLoop;

    GameOfLife() {
        Game game = new DefaultGame(1000);
        DefaultGameChanger gameChanger = new DefaultGameChanger();
        GameEvolution gameEvolution = new DefaultGameEvolution();
        Rule rule = new DefaultRule();
        Function<CellIterator, CellView> cellViewSupplier = new DefaultCellViewSupplier(game);
        Sleeper sleeper = new ThreadSleep(millis(40));
        GameInitializer gameInitializer = new DefaultGameInitializer();

        gameLoop = new DefaultGameLoop(game, DefaultGameViewer::new, gameChanger, gameEvolution, rule, cellViewSupplier, sleeper, gameInitializer);
    }

    void start() throws InterruptedException {
        gameLoop.start();

        // wait for start
        while (!gameLoop.isRunning()) {
            sleep(100);
        }
    }

    void stop() {
        gameLoop.stop();
    }

    void waitForStop() throws InterruptedException {
        while (gameLoop.isRunning()) {
            sleep(100);
        }
    }
}
