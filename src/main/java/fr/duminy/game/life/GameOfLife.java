package fr.duminy.game.life;

import com.google.code.tempusfugit.temporal.Sleeper;
import com.google.code.tempusfugit.temporal.ThreadSleep;

import java.util.function.Supplier;

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
        Supplier<MutableGameModel> gameModelSupplier = () -> new DefaultGameModel(1000);
        MutableGameModel gameModel = gameModelSupplier.get();
        Game game = new DefaultGame(gameModel);
        DefaultGameChanger gameChanger = new DefaultGameChanger(gameModelSupplier);
        Rule rule = new DefaultRule();
        Sleeper sleeper = new ThreadSleep(millis(40));
        GameModelInitializer gameModelInitializer = new DefaultGameModelInitializer();
        GameStatistics gameStatistics = new DefaultGameStatistics();

        gameLoop = new DefaultGameLoop(game, gameModel, g -> new DefaultGameViewer(g, gameStatistics), gameChanger, rule, sleeper, gameModelInitializer, gameStatistics);
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
