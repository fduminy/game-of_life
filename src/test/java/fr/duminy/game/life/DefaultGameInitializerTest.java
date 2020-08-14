package fr.duminy.game.life;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static fr.duminy.game.life.DefaultGameChangerTest.NUMBER_OF_CELLS;
import static fr.duminy.game.life.Mocks.stubCellIterator;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class DefaultGameInitializerTest {
    @Test
    void initialize(@Mock Game game) {
        DefaultGameInitializer initializer = new DefaultGameInitializer();
        CellIterator cellIterator = stubCellIterator(game);

        initializer.initialize(game);

        InOrder inOrder = inOrder(game, cellIterator);
        inOrder.verify(game).iterator();
        inOrder.verify(cellIterator).hasNext();
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            inOrder.verify(game).setAlive(anyInt(), anyInt(), anyBoolean());
            inOrder.verify(cellIterator).next();
            inOrder.verify(cellIterator).hasNext();
        }
        inOrder.verifyNoMoreInteractions();
    }
}