package fr.duminy.game.life;

import com.google.code.tempusfugit.temporal.Sleeper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static java.lang.System.nanoTime;
import static java.time.Duration.ofNanos;

public class DefaultGameLoop implements GameLoop {
    private final Game game;
    private final MutableGameModel gameModel;
    private final GameViewer gameViewer;
    private final GameChanger gameChanger;
    private final Rule rule;
    private final AtomicReference<GameLoopThread> gameLoopThread = new AtomicReference<>();
    private final Sleeper sleeper;
    private final GameModelInitializer gameModelInitializer;
    private final GameStatistics gameStatistics;

    public DefaultGameLoop(Game game, MutableGameModel gameModel, Function<GameLoop, GameViewer> gameViewer, DefaultGameChanger gameChanger,
                           Rule rule, Sleeper sleeper, GameModelInitializer gameModelInitializer, GameStatistics gameStatistics) {
        this.game = game;
        this.gameModel = gameModel;
        this.gameViewer = gameViewer.apply(this);
        this.gameChanger = gameChanger;
        this.rule = rule;
        this.sleeper = sleeper;
        this.gameModelInitializer = gameModelInitializer;
        this.gameStatistics = gameStatistics;
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
            generate(gameStatistics, () -> {
                gameModelInitializer.initialize(gameModel);
                gameViewer.view(game);
            });

            while (!stop.get()) {
                try {
                    sleeper.sleep();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                generate(gameStatistics, () -> {
                    gameChanger.evolve(game, rule);
                    gameViewer.view(game);
                });
            }
        }

        private void generate(GameStatistics statistics, Runnable runnable) {
            long start = nanoTime();
            long end = start;
            try {
                runnable.run();
                end = nanoTime();
            } finally {
                statistics.addGeneration(ofNanos(end - start));
            }
        }

        void requestStop() {
            stop.set(true);
        }
    }
}
