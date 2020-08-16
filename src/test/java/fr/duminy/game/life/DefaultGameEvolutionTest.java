package fr.duminy.game.life;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.mockito.InOrder;

import java.util.function.Supplier;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultGameEvolutionTest {
    @SuppressWarnings("unchecked")
    @Property
    void constructor(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y,
                     @ForAll boolean alive) {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        MutableGameModel gameModel = mock(MutableGameModel.class);
        Supplier<MutableGameModel> gameModelSupplier = mock(Supplier.class);
        when(gameModelSupplier.get()).thenReturn(gameModel);

        DefaultGameEvolution gameEvolution = new DefaultGameEvolution(game, gameModelSupplier);
        gameEvolution.setAlive(x, y, alive);

        verify(gameModel).setAlive(x, y, alive);
    }

    @SuppressWarnings("unchecked")
    @Property
    void update(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y,
                @ForAll boolean alive) {
        Game game = mock(Game.class);
        when(game.getSize()).thenReturn(GAME_SIZE);
        MutableGameModel generation1 = mock(MutableGameModel.class);
        MutableGameModel generation2 = mock(MutableGameModel.class);
        when(game.setModel(generation2)).thenReturn(generation1);
        Supplier<MutableGameModel> gameModelSupplier = mock(Supplier.class);
        when(gameModelSupplier.get()).thenReturn(generation2);
        DefaultGameEvolution gameEvolution = new DefaultGameEvolution(game, gameModelSupplier);

        gameEvolution.update(); // should switch between generation1 and generation2
        gameEvolution.setAlive(x, y, alive);

        InOrder inOrder = inOrder(game, generation1, generation2);
        inOrder.verify(game).setModel(generation2);
        inOrder.verify(generation1).setAlive(x, y, alive);
        inOrder.verifyNoMoreInteractions();
    }
}