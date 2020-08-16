package fr.duminy.game.life;

import com.google.code.tempusfugit.temporal.Sleeper;
import com.google.code.tempusfugit.temporal.ThreadSleep;

import java.util.function.Function;
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
        DefaultGameChanger gameChanger = new DefaultGameChanger();
        GameEvolution gameEvolution = new DefaultGameEvolution(game, gameModelSupplier);
        Rule rule = new DefaultRule();
        Function<CellIterator, CellView> cellViewSupplier = new DefaultCellViewSupplier(game);
        Sleeper sleeper = new ThreadSleep(millis(40));
        GameModelInitializer gameModelInitializer = new DefaultGameModelInitializer();

        gameLoop = new DefaultGameLoop(game, gameModel, DefaultGameViewer::new, gameChanger, gameEvolution, rule, cellViewSupplier, sleeper, gameModelInitializer);
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
