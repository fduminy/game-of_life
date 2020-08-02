package fr.duminy.game.life;

import fr.duminy.game.life.CellView.Position;
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

import static fr.duminy.game.life.CellView.Position.BOTTOM;
import static fr.duminy.game.life.CellView.Position.BOTTOM_LEFT;
import static fr.duminy.game.life.CellView.Position.BOTTOM_RIGHT;
import static fr.duminy.game.life.CellView.Position.LEFT;
import static fr.duminy.game.life.CellView.Position.RIGHT;
import static fr.duminy.game.life.CellView.Position.TOP;
import static fr.duminy.game.life.CellView.Position.TOP_LEFT;
import static fr.duminy.game.life.CellView.Position.TOP_RIGHT;
import static fr.duminy.game.life.CellView.Position.values;
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
        CellView view = new DefaultCellView(game, 1, 1);
        assertThat(view.isAlive(targetCell)).as("at targetCell %s", targetCell).isFalse();
    }

    @ParameterizedTest
    @EnumSource(Position.class)
    void isAlive_should_return_true_when_all_cells_are_alive(Position targetCell) {
        when(game.isAlive(anyInt(), anyInt())).thenReturn(true);
        when(game.getSize()).thenReturn(GAME_SIZE);
        CellView view = new DefaultCellView(game, 1, 1);
        assertThat(view.isAlive(targetCell)).as("at targetCell %s", targetCell).isTrue();
    }

    @ParameterizedTest
    @EnumSource(Position.class)
    void isAlive_should_return_true_only_when_target_cell_is_alive(Position targetCell, SoftAssertions softly) {
        when(game.isAlive(anyInt(), anyInt())).then(returnsTrueIf(targetCell));
        when(game.getSize()).thenReturn(GAME_SIZE);
        CellView view = new DefaultCellView(game, 1, 1);
        for (Position testedCell : values()) {
            softly.assertThat(view.isAlive(testedCell)).as("at testedCell %s", testedCell).isEqualTo(targetCell == testedCell);
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
                CellView view = new DefaultCellView(game, 0, y);
                softly.assertThat(view.isAlive(LEFT)).as("y=%d", y).isFalse();
            }
        }

        @Test
        void on_right_side(SoftAssertions softly) {
            Game game = gameWithAllCellsAlive();
            for (int y = 0; y < GAME_SIZE; y++) {
                CellView view = new DefaultCellView(game, X_MAX, y);
                softly.assertThat(view.isAlive(RIGHT)).as("y=%d", y).isFalse();
            }
        }

        @Test
        void on_top_side(SoftAssertions softly) {
            Game game = gameWithAllCellsAlive();
            for (int x = 0; x < GAME_SIZE; x++) {
                CellView view = new DefaultCellView(game, x, 0);
                softly.assertThat(view.isAlive(TOP)).as("x=%d", x).isFalse();
            }
        }

        @Test
        void on_bottom_side(SoftAssertions softly) {
            Game game = gameWithAllCellsAlive();
            for (int x = 0; x < GAME_SIZE; x++) {
                CellView view = new DefaultCellView(game, x, Y_MAX);
                softly.assertThat(view.isAlive(BOTTOM)).as("x=%d", x).isFalse();
            }
        }

        @Test
        void on_corner_side(SoftAssertions softly) {
            Game game = gameWithAllCellsAlive();
            softly.assertThat(new DefaultCellView(game, 0, 0).isAlive(TOP_LEFT)).isFalse();
            softly.assertThat(new DefaultCellView(game, X_MAX, 0).isAlive(TOP_RIGHT)).isFalse();
            softly.assertThat(new DefaultCellView(game, X_MAX, Y_MAX).isAlive(BOTTOM_RIGHT)).isFalse();
            softly.assertThat(new DefaultCellView(game, 0, Y_MAX).isAlive(BOTTOM_LEFT)).isFalse();
        }
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

    private Answer<Boolean> returnsTrueIf(Position targetCell) {
        return invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            return (x == targetCell.getX()) && (y == targetCell.getY());
        };
    }
}
