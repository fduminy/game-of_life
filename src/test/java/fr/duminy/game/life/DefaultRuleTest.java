package fr.duminy.game.life;

import fr.duminy.game.life.DefaultCellViewTest.Position;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.Size;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static fr.duminy.game.life.DefaultCellViewTest.Position.CENTER;
import static fr.duminy.game.life.DefaultRule.MAX_NB_ALIVE_NEIGHBOURS_TO_SURVIVE;
import static fr.duminy.game.life.DefaultRule.MIN_NB_ALIVE_NEIGHBOURS_TO_SURVIVE;
import static fr.duminy.game.life.DefaultRule.NB_ALIVE_NEIGHBOURS_TO_BORN;
import static fr.duminy.game.life.DefaultRule.NB_ALIVE_NEIGHBOURS_TO_CHECK;
import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("DefaultAnnotationParam")
class DefaultRuleTest {
    private static final Comparator<Position> COMPARATOR = comparing(Position::ordinal);

    @Property
    void a_cell_with_2_or_3_neighbours_survives(@ForAll @Size(min = 0, max = 8) Set<Position> positions) {
        testEvolve(positions, true, nbAliveNeighbours -> (nbAliveNeighbours >= MIN_NB_ALIVE_NEIGHBOURS_TO_SURVIVE) && (nbAliveNeighbours <= MAX_NB_ALIVE_NEIGHBOURS_TO_SURVIVE));
    }

    @Property
    void a_cell_with_3_neighbours_borns(@ForAll @Size(min = 0, max = 8) Set<Position> positions) {
        testEvolve(positions, false, nbAliveNeighbours -> nbAliveNeighbours == NB_ALIVE_NEIGHBOURS_TO_BORN);
    }

    private void testEvolve(Set<Position> positions, boolean cellIsAlive, Predicate<Integer> conditionToBeAlive) {
        assumeFalse(positions.contains(CENTER));
        CellView view = mock(CellView.class);
        when(view.isAlive(CENTER.getDeltaX(), CENTER.getDeltaY())).thenReturn(cellIsAlive);
        for (Position position : positions) {
            when(view.isAlive(position.getDeltaX(), position.getDeltaY())).thenReturn(true);
        }
        int nbAliveNeighbours = positions.size();
        int expectedCallsToIsAlive = 1 + expectedCallsToIsAliveWithoutSelf(positions, nbAliveNeighbours);
        assertAll(
                () -> assertThat(new DefaultRule().evolve(view)).isEqualTo(conditionToBeAlive.test(nbAliveNeighbours)),
                () -> verify(view, times(expectedCallsToIsAlive)).isAlive(anyInt(), anyInt())
        );
    }

    private int expectedCallsToIsAliveWithoutSelf(Set<Position> positions, int nbAliveNeighbours) {
        int expectedCallsToIsAliveWithoutSelf = 8;
        int maxAliveNeighboursToFind = NB_ALIVE_NEIGHBOURS_TO_CHECK + 1;
        if (nbAliveNeighbours >= maxAliveNeighboursToFind) {
            expectedCallsToIsAliveWithoutSelf = maxAliveNeighboursToFind;
            Optional<Position> maxPosition = positions.stream().sorted(COMPARATOR).limit(maxAliveNeighboursToFind)
                    .max(COMPARATOR);
            if (maxPosition.isPresent() && (maxPosition.get().ordinal() > expectedCallsToIsAliveWithoutSelf)) {
                expectedCallsToIsAliveWithoutSelf = maxPosition.get().ordinal();
            }
        }
        return expectedCallsToIsAliveWithoutSelf;
    }
}