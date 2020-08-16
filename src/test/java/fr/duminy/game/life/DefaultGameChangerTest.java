package fr.duminy.game.life;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static fr.duminy.game.life.Mocks.stubCellIterator;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGameChangerTest {
    public static final int NUMBER_OF_CELLS = GAME_SIZE * GAME_SIZE;

    @Mock
    private MutableGameModel gameModel;
    @Mock
    private Game game;
    @Mock
    private GameEvolution gameEvolution;
    @Mock
    private Function<CellIterator, CellView> cellViewSupplier;
    @Mock
    private CellView cellView;
    @Mock
    private Rule rule;

    @ParameterizedTest(name = "cell isAlive={0} after")
    @ValueSource(booleans = {true, false})
    void evolve(boolean cellIsAliveAfter) {
        CellIterator cellIterator = spy(stubCellIterator(0, 0));
        when(game.iterator()).thenReturn(cellIterator);
        when(cellViewSupplier.apply(cellIterator)).thenReturn(cellView);
        when(rule.evolve(cellView)).thenReturn(cellIsAliveAfter);

        new DefaultGameChanger().evolve(game, gameEvolution, cellViewSupplier, rule);

        InOrder inOrder = inOrder(game, gameEvolution, cellViewSupplier, cellView, rule, cellIterator);
        inOrder.verify(game).iterator();
        inOrder.verify(cellIterator).hasNext();
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            inOrder.verify(cellViewSupplier).apply(cellIterator);
            inOrder.verify(rule).evolve(cellView);
            inOrder.verify(gameEvolution).setAlive(anyInt(), anyInt(), eq(cellIsAliveAfter));
            inOrder.verify(cellIterator).next();
            inOrder.verify(cellIterator).hasNext();
        }
        inOrder.verifyNoMoreInteractions();
    }
}