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

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static java.lang.String.format;
import static net.jqwik.api.Arbitraries.integers;
import static net.jqwik.api.Combinators.combine;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class DefaultGameEvolutionTest {
    @Property
    void update_should_not_update_the_game_when_setAlive_is_never_called() {
        Game game = mock(Game.class);

        new DefaultGameEvolution().update(game);

        verifyNoInteractions(game);
    }

    @Property
    void update_should_update_the_game(@ForAll("sequences") @Size(max = 10) ActionSequence<DefaultGameEvolution> actions) {
        Game game = mock(Game.class);
        DefaultGameEvolution gameEvolution = actions.run(new DefaultGameEvolution());

        gameEvolution.update(game);

        actions.runActions().stream().filter(SetAliveAction.class::isInstance).map(SetAliveAction.class::cast)
                .forEach(setAliveAction -> verify(game, atLeast(1)).setAlive(setAliveAction.x, setAliveAction.y, setAliveAction.alive));
    }

    @Property
    void update_should_clear_updates_after_each_call(@ForAll("sequences") @Size(max = 1) ActionSequence<DefaultGameEvolution> actions) {
        Game game = mock(Game.class);
        DefaultGameEvolution gameEvolution = actions.run(new DefaultGameEvolution());

        gameEvolution.update(game); // should clear updates
        clearInvocations(game);
        gameEvolution.update(game);

        verifyNoInteractions(game);
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