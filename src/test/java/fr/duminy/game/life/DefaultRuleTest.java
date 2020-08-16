package fr.duminy.game.life;

import fr.duminy.game.life.DefaultCellViewTest.Position;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.Size;

import java.util.Set;

import static fr.duminy.game.life.DefaultCellViewTest.Position.CENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("DefaultAnnotationParam")
class DefaultRuleTest {
    @Property
    void a_cell_with_2_or_3_neighbours_survives(@ForAll @Size(min = 0, max = 8) Set<Position> positions) {
        assumeFalse(positions.contains(CENTER));
        CellView view = mock(CellView.class);
        when(view.isAlive(CENTER.getDeltaX(), CENTER.getDeltaY())).thenReturn(true);
        for (Position position : positions) {
            when(view.isAlive(position.getDeltaX(), position.getDeltaY())).thenReturn(true);
        }
        assertThat(new DefaultRule().evolve(view)).isEqualTo((positions.size() == 2) || (positions.size() == 3));
    }

    @Property
    void a_cell_with_3_neighbours_borns(@ForAll @Size(min = 0, max = 8) Set<Position> positions) {
        assumeFalse(positions.contains(CENTER));
        CellView view = mock(CellView.class);
        when(view.isAlive(CENTER.getDeltaX(), CENTER.getDeltaY())).thenReturn(false);
        for (Position position : positions) {
            when(view.isAlive(position.getDeltaX(), position.getDeltaY())).thenReturn(true);
        }
        assertThat(new DefaultRule().evolve(view)).isEqualTo(positions.size() == 3);
    }
}