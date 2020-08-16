package fr.duminy.game.life;

import com.google.code.tempusfugit.temporal.Duration;
import com.google.code.tempusfugit.temporal.Sleeper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static com.google.code.tempusfugit.temporal.Duration.seconds;

public class DefaultGameLoop implements GameLoop {
    static final Duration DELAY = seconds(1);

    private final Game game;
    private final MutableGameModel gameModel;
    private final GameViewer gameViewer;
    private final DefaultGameChanger gameChanger;
    private final GameEvolution gameEvolution;
    private final Rule rule;
    private final Function<CellIterator, CellView> cellViewSupplier;
    private final AtomicReference<GameLoopThread> gameLoopThread = new AtomicReference<>();
    private final Sleeper sleeper;
    private final GameModelInitializer gameModelInitializer;

    public DefaultGameLoop(Game game, MutableGameModel gameModel, Function<GameLoop, GameViewer> gameViewer, DefaultGameChanger gameChanger,
                           GameEvolution gameEvolution, Rule rule, Function<CellIterator, CellView> cellViewSupplier,
                           Sleeper sleeper, GameModelInitializer gameModelInitializer) {
        this.game = game;
        this.gameModel = gameModel;
        this.gameViewer = gameViewer.apply(this);
        this.gameChanger = gameChanger;
        this.gameEvolution = gameEvolution;
        this.rule = rule;
        this.cellViewSupplier = cellViewSupplier;
        this.sleeper = sleeper;
        this.gameModelInitializer = gameModelInitializer;
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
            gameModelInitializer.initialize(gameModel);
            gameViewer.view(game);

            while (!stop.get()) {
                try {
                    sleeper.sleep();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gameChanger.evolve(game, gameEvolution, cellViewSupplier, rule);
                gameEvolution.update();
                gameViewer.view(game);
            }
        }

        void requestStop() {
            stop.set(true);
        }
    }
}
