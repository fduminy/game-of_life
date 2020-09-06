package fr.duminy.game.life;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static fr.duminy.game.life.DefaultCellViewTest.Position.BOTTOM;
import static fr.duminy.game.life.DefaultCellViewTest.Position.BOTTOM_LEFT;
import static fr.duminy.game.life.DefaultCellViewTest.Position.BOTTOM_RIGHT;
import static fr.duminy.game.life.DefaultCellViewTest.Position.LEFT;
import static fr.duminy.game.life.DefaultCellViewTest.Position.RIGHT;
import static fr.duminy.game.life.DefaultCellViewTest.Position.TOP;
import static fr.duminy.game.life.DefaultCellViewTest.Position.TOP_LEFT;
import static fr.duminy.game.life.DefaultCellViewTest.Position.TOP_RIGHT;
import static fr.duminy.game.life.DefaultCellViewTest.Position.values;
import static fr.duminy.game.life.Mocks.mockCellIterator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
public class DefaultCellViewTest {
    public static final int GAME_SIZE = 3;
    public static final int X_MAX = GAME_SIZE - 1;
    public static final int Y_MAX = GAME_SIZE - 1;

    @Mock
    private Game game;

    @ParameterizedTest
    @EnumSource(Position.class)
    void isAlive_should_return_false_when_no_cell_is_alive(Position targetCell) {
        when(game.isAlive(anyInt(), anyInt())).thenReturn(false);
        when(game.getSize()).thenReturn(GAME_SIZE);
        CellView view = newDefaultCellView(game, 1, 1);
        assertThat(isAlive(view, targetCell)).as("at targetCell %s", targetCell).isFalse();
    }

    @ParameterizedTest
    @EnumSource(Position.class)
    void isAlive_should_return_true_when_all_cells_are_alive(Position targetCell) {
        when(game.isAlive(anyInt(), anyInt())).thenReturn(true);
        when(game.getSize()).thenReturn(GAME_SIZE);
        CellView view = newDefaultCellView(game, 1, 1);
        assertThat(isAlive(view, targetCell)).as("at targetCell %s", targetCell).isTrue();
    }

    @ParameterizedTest
    @EnumSource(Position.class)
    void isAlive_should_return_true_only_when_target_cell_is_alive(Position targetCell, SoftAssertions softly) {
        int x = 1;
        int y = 1;
        when(game.isAlive(anyInt(), anyInt())).then(returnsTrueIf(targetCell, x, y));
        when(game.getSize()).thenReturn(GAME_SIZE);
        CellView view = newDefaultCellView(game, x, y);
        for (Position testedCell : values()) {
            softly.assertThat(isAlive(view, testedCell)).as("at testedCell %s", testedCell).isEqualTo(targetCell == testedCell);
        }
    }

    @Property
    void setLocation(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y) {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        when(game.iterator()).then(answer -> mockCellIterator());
        when(game.isAlive(x, y)).thenReturn(true);
        DefaultCellView cellView = newDefaultCellView(game, x, y);

        for (Position testedCell : values()) {
            int deltaX = testedCell.getDeltaX();
            int deltaY = testedCell.getDeltaY();
            boolean shouldBeAlive = (deltaX == 0) && (deltaY == 0);
            assertThat(cellView.isAlive(deltaX, deltaY))
                    .as("x=%d, y=%d, deltaX=%d, deltaY=%d", x, y, deltaX, deltaY)
                    .isEqualTo(shouldBeAlive);
        }
    }

    enum Position {
        TOP_LEFT(-1, -1),
        TOP(0, -1),
        TOP_RIGHT(1, -1),

        LEFT(-1, 0),
        CENTER(0, 0),
        RIGHT(1, 0),

        BOTTOM_LEFT(-1, 1),
        BOTTOM(0, 1),
        BOTTOM_RIGHT(1, 1);

        private final int deltaX;
        private final int deltaY;

        Position(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

        public int getDeltaX() {
            return deltaX;
        }

        public int getDeltaY() {
            return deltaY;
        }
    }

    @Nested
    @DisplayName("isAlive should return false when outside of game bounds")
    class OutOfBounds {
        private final SoftAssertions softly = new SoftAssertions();

        @Test
        void on_left_side() {
            Game game = gameWithAllCellsAlive();
            for (int y = 0; y < GAME_SIZE; y++) {
                CellView view = newDefaultCellView(game, 0, y);
                view.setLocation(0, y);
                softly.assertThat(isAlive(view, LEFT)).as("y=%d", y).isFalse();
            }
        }

        @Test
        void on_right_side(SoftAssertions softly) {
            Game game = gameWithAllCellsAlive();
            for (int y = 0; y < GAME_SIZE; y++) {
                CellView view = newDefaultCellView(game, X_MAX, y);
                softly.assertThat(isAlive(view, RIGHT)).as("y=%d", y).isFalse();
            }
        }

        @Test
        void on_top_side(SoftAssertions softly) {
            Game game = gameWithAllCellsAlive();
            for (int x = 0; x < GAME_SIZE; x++) {
                CellView view = newDefaultCellView(game, x, 0);
                softly.assertThat(isAlive(view, TOP)).as("x=%d", x).isFalse();
            }
        }

        @Test
        void on_bottom_side(SoftAssertions softly) {
            Game game = gameWithAllCellsAlive();
            for (int x = 0; x < GAME_SIZE; x++) {
                CellView view = newDefaultCellView(game, x, Y_MAX);
                softly.assertThat(isAlive(view, BOTTOM)).as("x=%d", x).isFalse();
            }
        }

        @Test
        void on_corner_side(SoftAssertions softly) {
            Game game = gameWithAllCellsAlive();
            softly.assertThat(isAlive(newDefaultCellView(game, 0, 0), TOP_LEFT)).isFalse();
            softly.assertThat(isAlive(newDefaultCellView(game, X_MAX, 0), TOP_RIGHT)).isFalse();
            softly.assertThat(isAlive(newDefaultCellView(game, X_MAX, Y_MAX), BOTTOM_RIGHT)).isFalse();
            softly.assertThat(isAlive(newDefaultCellView(game, 0, Y_MAX), BOTTOM_LEFT)).isFalse();
        }
    }

    private DefaultCellView newDefaultCellView(Game game, int x, int yMax) {
        DefaultCellView view = new DefaultCellView(game);
        view.setLocation(x, yMax);
        return view;
    }

    // hack to avoid mockito stubbing failure due to unnecessary stubbing
    private Game gameWithAllCellsAlive() {
        return mock(Game.class, (Answer<Object>) invocation -> {
            if (boolean.class.equals(invocation.getMethod().getReturnType())) {
                return true;
            }
            return GAME_SIZE;
        });
    }

    private Answer<Boolean> returnsTrueIf(Position targetCell, int x, int y) {
        return invocation -> {
            int currentX = invocation.getArgument(0);
            int currentY = invocation.getArgument(1);
            return (currentX == (x + targetCell.getDeltaX())) &&
                    (currentY == (y + targetCell.getDeltaY()));
        };
    }

    private boolean isAlive(CellView view, Position position) {
        return view.isAlive(position.getDeltaX(), position.getDeltaY());
    }
}
