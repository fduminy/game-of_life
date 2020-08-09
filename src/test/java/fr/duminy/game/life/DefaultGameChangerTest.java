package fr.duminy.game.life;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.BiFunction;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGameChangerTest {
    public static final int NUMBER_OF_CELLS = GAME_SIZE * GAME_SIZE;

    @Mock
    private Game game;
    @Mock
    private GameEvolution gameEvolution;
    @Mock
    private BiFunction<Integer, Integer, CellView> cellViewSupplier;
    @Mock
    private CellView cellView;
    @Mock
    private Rule rule;

    @ParameterizedTest(name = "cell isAlive={0} after")
    @ValueSource(booleans = {true, false})
    void evolve(boolean cellIsAliveAfter) {
        when(game.getSize()).thenReturn(GAME_SIZE);
        when(cellViewSupplier.apply(anyInt(), anyInt())).thenReturn(cellView);
        when(rule.evolve(cellView)).thenReturn(cellIsAliveAfter);

        new DefaultGameChanger().evolve(game, gameEvolution, cellViewSupplier, rule);

        verify(game).getSize();
        verify(rule, times(NUMBER_OF_CELLS)).evolve(cellView);
        verify(gameEvolution, times(NUMBER_OF_CELLS)).setAlive(anyInt(), anyInt(), eq(cellIsAliveAfter));
        for (int y = 0; y < GAME_SIZE; y++) {
            for (int x = 0; x < GAME_SIZE; x++) {
                verify(cellViewSupplier).apply(x, y);
            }
        }
        verifyNoMoreInteractions(game, gameEvolution, cellViewSupplier, cellView, rule);
    }
}