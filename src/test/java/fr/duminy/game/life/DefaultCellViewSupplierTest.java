package fr.duminy.game.life;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultCellViewSupplierTest {
    @Property
    void apply(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y) {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        DefaultCellViewSupplier cellViewSupplier = new DefaultCellViewSupplier(game);
        CellView cellView1 = cellViewSupplier.apply(x, y);
        CellView cellView2 = cellViewSupplier.apply(x, y);
        assertThat(cellView2).isNotNull().isSameAs(cellView1);
    }
}