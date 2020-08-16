package fr.duminy.game.life;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;
import net.jqwik.api.Tuple.Tuple3;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.stateful.Action;
import net.jqwik.api.stateful.ActionSequence;

import java.util.List;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static net.jqwik.api.Arbitraries.integers;
import static net.jqwik.api.Combinators.combine;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultGameEvolutionTest {
    @Property
    void update_should_update_the_game(@ForAll("sequences") @Size(max = 10) ActionSequence<DefaultGameEvolution> actions) {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        DefaultGameEvolution gameEvolution = actions.run(new DefaultGameEvolution(game));

        gameEvolution.update();

        for (int y = 0; y < GAME_SIZE; y++) {
            for (int x = 0; x < GAME_SIZE; x++) {
                final int currentX = x;
                final int currentY = y;
                List<SetAliveAction> currentActions = actions.runActions().stream().filter(SetAliveAction.class::isInstance).map(SetAliveAction.class::cast)
                        .filter(action -> (action.x == currentX) && (action.y == currentY)).collect(toList());
                boolean alive = !currentActions.isEmpty() && currentActions.get(currentActions.size() - 1).alive;
                verify(game).setAlive(x, y, alive);
            }
        }
    }

    @SuppressWarnings("unused")
    @Provide
    Arbitrary<ActionSequence<DefaultGameEvolution>> sequences() {
        return Arbitraries.sequences(setAliveAction());
    }

    private Arbitrary<Action<DefaultGameEvolution>> setAliveAction() {
        IntegerArbitrary arbitraryX = integers().between(0, GAME_SIZE - 1);
        IntegerArbitrary arbitraryY = integers().between(0, GAME_SIZE - 1);
        Arbitrary<Boolean> arbitraryAlive = integers().between(0, 1).map(i -> i == 0);
        Arbitrary<Tuple3<Integer, Integer, Boolean>> arbitrary = combine(arbitraryX, arbitraryY, arbitraryAlive).as(Tuple::of);
        return arbitrary.map(tuple3 -> new SetAliveAction(tuple3.get1(), tuple3.get2(), tuple3.get3()));
    }

    static class SetAliveAction implements Action<DefaultGameEvolution> {
        private final int x;
        private final int y;
        private final boolean alive;

        SetAliveAction(int x, int y, boolean alive) {
            this.x = x;
            this.y = y;
            this.alive = alive;
        }

        @Override
        public DefaultGameEvolution run(DefaultGameEvolution gameEvolution) {
            gameEvolution.setAlive(x, y, alive);
            return gameEvolution;
        }

        @Override
        public String toString() {
            return format("setAlive(%d, %d, %s)", x, y, alive);
        }
    }
}