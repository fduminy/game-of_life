package fr.duminy.game.life;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.event.WindowEvent;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static fr.duminy.game.life.DefaultGameChangerTest.NUMBER_OF_CELLS;
import static fr.duminy.game.life.Mocks.stubCellIterator;
import static java.awt.event.WindowEvent.WINDOW_CLOSING;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGameViewerTest {
    @Mock
    private Game game;
    @Mock
    private MutableGameModel gameModel;
    @Mock
    private GameLoop gameLoop;
    @Mock
    private GameStatistics gameStatistics;

    @Test
    void view() {
        when(game.getSize()).thenReturn(GAME_SIZE);
        CellIterator cellIterator = spy(stubCellIterator(0, 0));
        when(game.iterator()).thenReturn(cellIterator);
        DefaultGameViewer gameViewer = new DefaultGameViewer(gameLoop, gameStatistics);

        gameViewer.view(game);

        InOrder inOrder = inOrder(game, gameLoop, cellIterator, gameStatistics);
        inOrder.verify(game).iterator();
        inOrder.verify(cellIterator).hasNext();
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            inOrder.verify(cellIterator).isAlive();
            inOrder.verify(cellIterator).getX();
            inOrder.verify(cellIterator).getY();
            inOrder.verify(cellIterator).next();
            inOrder.verify(cellIterator).hasNext();
        }
        inOrder.verify(gameStatistics).getStringToView();
        gameViewer.window.dispatchEvent(new WindowEvent(gameViewer.window, WINDOW_CLOSING));
        inOrder.verify(gameLoop).stop();
        inOrder.verifyNoMoreInteractions();
    }
}