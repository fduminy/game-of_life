package fr.duminy.game.life;

import com.google.code.tempusfugit.temporal.Sleeper;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static com.google.code.tempusfugit.temporal.Timeout.timeout;
import static com.google.code.tempusfugit.temporal.WaitFor.waitOrTimeout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
class DefaultGameLoopTest {
    @Mock
    private Game game;
    @Mock
    private MutableGameModel gameModel;
    @Mock
    private GameViewer gameViewer;
    @Mock
    private DefaultGameChanger gameChanger;
    @Mock
    private GameEvolution gameEvolution;
    @Mock
    private Rule rule;
    @Mock
    private Function<CellIterator, CellView> cellViewSupplier;
    @Mock
    private Sleeper sleeper;
    @Mock
    private GameModelInitializer gameModelInitializer;
    @Mock
    private GameStatistics gameStatistics;

    @Test
    void start() throws InterruptedException, TimeoutException {
        GameLoop gameLoop = new DefaultGameLoop(game, gameModel, loop -> gameViewer, gameChanger, gameEvolution, rule,
                cellViewSupplier, sleeper, gameModelInitializer, gameStatistics);
        int maxIterations = 3;
        List<String> events = new ArrayList<>();
        doAnswer(a -> events.add("init")).when(gameModelInitializer).initialize(gameModel);
        doAnswer(a -> events.add("view")).when(gameViewer).view(game);
        AtomicInteger sleepCount = new AtomicInteger();
        final Sleeper finalSleeper = sleeper;
        doAnswer(answer -> {
            events.add("sleep");
            if (sleepCount.incrementAndGet() >= maxIterations) {
                synchronized (finalSleeper) {
                    finalSleeper.wait(); // lock thread
                }
            }
            return null;
        }).when(sleeper).sleep();
        doAnswer(a -> events.add("evolve")).when(gameChanger).evolve(game, gameEvolution, cellViewSupplier, rule);
        doAnswer(a -> events.add("update")).when(gameEvolution).update();
        doAnswer(a -> events.add("stats")).when(gameStatistics).addGeneration(any());

        gameLoop.start();
        waitOrTimeout(() -> sleepCount.get() >= maxIterations, timeout(seconds(1)));
        gameLoop.stop();

        assertThat(events).containsExactly("init", "view", "stats", "sleep", "evolve", "update", "view", "stats", "sleep", "evolve", "update", "view", "stats", "sleep");
    }

    @Test
    void isRunning(SoftAssertions softly) {
        GameLoop gameLoop = new DefaultGameLoop(game, gameModel, loop -> gameViewer, gameChanger, gameEvolution, rule, cellViewSupplier, sleeper, gameModelInitializer, gameStatistics);
        softly.assertThat(gameLoop.isRunning()).isFalse();

        gameLoop.start();
        softly.assertThat(gameLoop.isRunning()).isTrue();

        gameLoop.stop();
        softly.assertThat(gameLoop.isRunning()).isFalse();
    }
}