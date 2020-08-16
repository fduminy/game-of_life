package fr.duminy.game.life;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SoftAssertionsExtension.class)
class DefaultGameTest {
    @Property
    void getSize() {
        MutableGameModel gameModel = mock(MutableGameModel.class);
        when(gameModel.getSize()).thenReturn(GAME_SIZE);
        assertThat(new DefaultGame(gameModel).getSize()).isEqualTo(GAME_SIZE);
    }

    @Property
    void isAlive(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y) {
        MutableGameModel gameModel = mock(MutableGameModel.class);
        DefaultGame game = new DefaultGame(gameModel);

        game.isAlive(x, y);

        verify(gameModel).isAlive(x, y);
    }

    @Test
    void iterator() {
        MutableGameModel gameModel = mock(MutableGameModel.class);
        CellIterator cellIterator = mock(CellIterator.class);
        when(gameModel.iterator()).thenReturn(cellIterator);
        DefaultGame game = new DefaultGame(gameModel);

        assertThat(game.iterator()).isSameAs(cellIterator);
    }

    @Test
    void setModel(SoftAssertions softly) {
        MutableGameModel initialModel = mock(MutableGameModel.class);
        DefaultGame game = new DefaultGame(initialModel);
        MutableGameModel gameModel = mock(MutableGameModel.class);
        when(gameModel.getSize()).thenReturn(GAME_SIZE);

        GameModel previousModel = game.setModel(gameModel);

        softly.assertThat(game.getSize()).isEqualTo(GAME_SIZE);
        softly.assertThat(previousModel).isSameAs(initialModel);
    }
}