package fr.duminy.game.life;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultGameTest {
    @Property
    void getSize() {
        GameModel gameModel = mock(GameModel.class);
        when(gameModel.getSize()).thenReturn(GAME_SIZE);
        assertThat(new DefaultGame(gameModel).getSize()).isEqualTo(GAME_SIZE);
    }

    @Property
    void isAlive(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y) {
        GameModel gameModel = mock(GameModel.class);
        DefaultGame game = new DefaultGame(gameModel);

        game.isAlive(x, y);

        verify(gameModel).isAlive(x, y);
    }

    @Property
    void setAlive(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y,
                  @ForAll boolean alive) {
        GameModel gameModel = mock(GameModel.class);
        DefaultGame game = new DefaultGame(gameModel);

        game.setAlive(x, y, alive);

        verify(gameModel).setAlive(x, y, alive);
    }

    @Test
    void iterator() {
        GameModel gameModel = mock(GameModel.class);
        CellIterator cellIterator = mock(CellIterator.class);
        when(gameModel.iterator()).thenReturn(cellIterator);
        DefaultGame game = new DefaultGame(gameModel);

        assertThat(game.iterator()).isSameAs(cellIterator);
    }
}