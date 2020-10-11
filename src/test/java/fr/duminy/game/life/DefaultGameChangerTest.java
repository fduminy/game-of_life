package fr.duminy.game.life;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static fr.duminy.game.life.Mocks.stubCellIterator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGameChangerTest {
    public static final int NUMBER_OF_CELLS = GAME_SIZE * GAME_SIZE;

    @Mock
    private Game game;
    @Mock
    private CellView cellView;
    @Mock
    private Rule rule;
    @Mock
    private MutableGameModel currentGeneration;
    @Mock
    private MutableGameModel nextGeneration;

    @ParameterizedTest(name = "cell isAlive={0} after")
    @ValueSource(booleans = {true, false})
    void evolve(boolean cellIsAliveAfter) {
        CellIterator cellIterator = spy(stubCellIterator(0, 0, cellView));
        when(game.iterator()).thenReturn(cellIterator);
        when(rule.evolve(cellView)).thenReturn(cellIsAliveAfter);

        new DefaultGameChanger(() -> nextGeneration).evolve(game, rule);

        InOrder inOrder = inOrder(game, nextGeneration, cellView, rule, cellIterator);
        inOrder.verify(game).iterator();
        inOrder.verify(cellIterator).hasNext();
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            inOrder.verify(rule).evolve(cellView);
            inOrder.verify(nextGeneration).setAlive(anyInt(), anyInt(), eq(cellIsAliveAfter));
            inOrder.verify(cellIterator).next();
            inOrder.verify(cellIterator).hasNext();
        }
        inOrder.verify(game).setModel(nextGeneration);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void evolve_2_times_to_verify_models_are_swapped() {
        StubCellIterator cellIterator = stubCellIterator(0, 0, cellView, 1);
        when(game.iterator()).thenReturn(cellIterator);
        when(game.setModel(any())).thenAnswer(new Answer<>() {
            private MutableGameModel gameModel = currentGeneration;

            @Override
            public Object answer(InvocationOnMock invocation) {
                MutableGameModel result = gameModel;
                gameModel = invocation.getArgument(0);
                return result;
            }
        });
        List<String> interactions = new ArrayList<>();
        catchInteractions(interactions, currentGeneration, "currentGeneration");
        catchInteractions(interactions, nextGeneration, "nextGeneration");

        DefaultGameChanger defaultGameChanger = new DefaultGameChanger(() -> nextGeneration);
        defaultGameChanger.evolve(game, rule);
        cellIterator.locate(0, 0);
        defaultGameChanger.evolve(game, rule);

        assertThat(interactions).containsExactly("nextGeneration.setAlive", "currentGeneration.setAlive");
    }

    private void catchInteractions(List<String> actions, MutableGameModel generation, final String generationName) {
        doAnswer(invocation -> {
            actions.add(generationName + ".setAlive");
            return null;
        }).when(generation).setAlive(anyInt(), anyInt(), anyBoolean());
    }
}