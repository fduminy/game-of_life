package fr.duminy.game.life;

import fr.duminy.game.life.DefaultGameModel.DefaultCellIterator;
import fr.duminy.game.life.DefaultGameModel.DefaultCellView;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
class DefaultCellIteratorTest {
    @SuppressWarnings("unchecked")
    @Test
    void constructor(@Mock Game game, SoftAssertions softly) {
        when(game.getSize()).thenReturn(GAME_SIZE);

        CellIterator cellIterator = new DefaultCellIterator(game);

        softly.assertThat(cellIterator).extracting(CellIterator::getIndex, CellIterator::getX, CellIterator::getY, CellIterator::hasNext)
                .containsExactly(0, 0, 0, true);
    }

    @SuppressWarnings("unchecked")
    @Test
    void next(@Mock Game game, SoftAssertions softly) {
        when(game.getSize()).thenReturn(GAME_SIZE);

        CellIterator cellIterator = new DefaultCellIterator(game);

        for (CellIterator referenceCellIterator = new StubCellIterator(0, 0); referenceCellIterator.hasNext(); referenceCellIterator.next()) {
            softly.assertThat(cellIterator).extracting(CellIterator::getIndex, CellIterator::getX, CellIterator::getY, CellIterator::hasNext)
                    .containsExactly(referenceCellIterator.getIndex(), referenceCellIterator.getX(), referenceCellIterator.getY(), referenceCellIterator.hasNext());
            cellIterator.next();
        }
    }

    @Property
    void isAlive(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y, @ForAll boolean alive) {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        when(game.isAlive(x, y)).thenReturn(alive);

        CellIterator cellIterator = new DefaultCellIterator(game);

        while ((cellIterator.getX() != x) || (cellIterator.getY() != y)) {
            cellIterator.next();
        }
        assertThat(cellIterator.isAlive()).isEqualTo(alive);
    }

    @Property
    void cellView(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y, @ForAll boolean alive) {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        when(game.isAlive(x, y)).thenReturn(alive);

        CellIterator cellIterator = new DefaultCellIterator(game);
        while (!((cellIterator.getX() == x) && (cellIterator.getY() == y))) {
            cellIterator.next();
        }
        CellView cellView = cellIterator.cellView();

        assertThat(cellView).isExactlyInstanceOf(DefaultCellView.class);
        assertThat(cellView.isAlive(0, 0)).isEqualTo(alive);
    }
}