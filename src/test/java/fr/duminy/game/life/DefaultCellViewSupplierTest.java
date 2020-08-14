package fr.duminy.game.life;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static fr.duminy.game.life.DefaultGameChangerTest.NUMBER_OF_CELLS;
import static fr.duminy.game.life.Mocks.mockCellIterator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class DefaultCellViewSupplierTest {
    @Test
    void constructor() {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        CellIterator cellIterator = mock(CellIterator.class);
        Mocks.mock(game, cellIterator);

        new DefaultCellViewSupplier(game);

        InOrder inOrder = inOrder(game, cellIterator);
        inOrder.verify(game).iterator();
        inOrder.verify(cellIterator).hasNext();
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            inOrder.verify(cellIterator).getIndex();
            inOrder.verify(cellIterator).getX();
            inOrder.verify(cellIterator).getY();
            inOrder.verify(cellIterator).next();
            inOrder.verify(cellIterator).hasNext();
        }
        inOrder.verifyNoMoreInteractions();
    }

    @Property
    void apply(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y) {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        when(game.iterator()).then(answer -> mockCellIterator());
        DefaultCellViewSupplier cellViewSupplier = new DefaultCellViewSupplier(game);

        CellIterator cellIteratorForApply = mockCellIterator(x, y);
        CellView cellView1 = cellViewSupplier.apply(cellIteratorForApply);
        CellView cellView2 = cellViewSupplier.apply(cellIteratorForApply);

        assertThat(cellView2).as("x=%d, y=%d", x, y).isNotNull().isSameAs(cellView1);
        verify(cellIteratorForApply, times(2)).getIndex();
        verifyNoMoreInteractions(cellIteratorForApply);
    }
}