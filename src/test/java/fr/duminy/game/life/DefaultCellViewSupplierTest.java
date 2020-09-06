package fr.duminy.game.life;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.function.Function;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static fr.duminy.game.life.Mocks.mockCellIterator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class DefaultCellViewSupplierTest {
    @SuppressWarnings("unchecked")
    @Test
    void constructor() {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        CellIterator cellIterator = mock(CellIterator.class);
        Mocks.mock(game, cellIterator);
        Function<Game, CellView> cellViewFactory = mock(Function.class);

        new DefaultCellViewSupplier(game, cellViewFactory);

        InOrder inOrder = inOrder(game, cellIterator, cellViewFactory);
        inOrder.verify(cellViewFactory).apply(game);
        inOrder.verifyNoMoreInteractions();
    }

    @Property
    void apply(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y) {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        when(game.iterator()).then(answer -> mockCellIterator(x, y));
        when(game.isAlive(x, y)).thenReturn(true);
        CellView cellView = mock(CellView.class);
        Function<Game, CellView> cellViewFactory = g -> cellView;
        DefaultCellViewSupplier cellViewSupplier = new DefaultCellViewSupplier(game, cellViewFactory);

        CellIterator cellIteratorForApply = mockCellIterator(x, y);
        CellView cellView1 = cellViewSupplier.apply(cellIteratorForApply);
        CellView cellView2 = cellViewSupplier.apply(cellIteratorForApply);

        assertThat(cellView1).as("x=%d, y=%d", x, y).isSameAs(cellView);
        assertThat(cellView2).as("x=%d, y=%d", x, y).isSameAs(cellView);
        verify(cellView, times(2)).setLocation(x, y);
        verifyNoMoreInteractions(cellView);
    }
}