package fr.duminy.game.life;

import com.google.code.tempusfugit.temporal.Duration;
import com.google.code.tempusfugit.temporal.Sleeper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static java.lang.Math.random;

public class DefaultGameLoop implements GameLoop {
    static final Duration DELAY = seconds(1);

    private final Game game;
    private final GameViewer gameViewer;
    private final DefaultGameChanger gameChanger;
    private final GameEvolution gameEvolution;
    private final Rule rule;
    private final BiFunction<Integer, Integer, CellView> cellViewSupplier;
    private final AtomicReference<GameLoopThread> gameLoopThread = new AtomicReference<>();
    private final Sleeper sleeper;

    public DefaultGameLoop(Game game, Function<GameLoop, GameViewer> gameViewer, DefaultGameChanger gameChanger, GameEvolution gameEvolution, Rule rule, BiFunction<Integer, Integer, CellView> cellViewSupplier, Sleeper sleeper) {
        this.game = game;
        this.gameViewer = gameViewer.apply(this);
        this.gameChanger = gameChanger;
        this.gameEvolution = gameEvolution;
        this.rule = rule;
        this.cellViewSupplier = cellViewSupplier;
        this.sleeper = sleeper;
    }

    @Override
    public void start() {
        GameLoopThread thread = new GameLoopThread();
        thread.start();
        gameLoopThread.set(thread);

    }

    @Override
    public void stop() {
        GameLoopThread thread = gameLoopThread.get();
        if (thread != null) {
            gameLoopThread.set(null);
            thread.requestStop();
        }
    }

    @Override
    public boolean isRunning() {
        return gameLoopThread.get() != null;
    }

    private final class GameLoopThread extends Thread {
        private final AtomicBoolean stop = new AtomicBoolean(false);

        public GameLoopThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            initGame(game);
            gameViewer.view(game);

            while (!stop.get()) {
                try {
                    sleeper.sleep();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gameChanger.evolve(game, gameEvolution, cellViewSupplier, rule);
                gameEvolution.update(game);
                gameViewer.view(game);
            }
        }

        void requestStop() {
            stop.set(true);
        }

        private void initGame(Game game) {
            int size = game.getSize();
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    game.setAlive(x, y, random() > 0.5);
                }
            }
        }
    }
}