package fr.duminy.game.life;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.event.WindowEvent;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static java.awt.event.WindowEvent.WINDOW_CLOSING;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGameViewerTest {
    @Mock
    private Game game;
    @Mock
    private GameLoop gameLoop;

    @Test
    void view() {
        when(game.getSize()).thenReturn(GAME_SIZE);
        DefaultGameViewer gameViewer = new DefaultGameViewer(gameLoop);

        gameViewer.view(game);

        verify(game).getSize();
        for (int y = 0; y < GAME_SIZE; y++) {
            for (int x = 0; x < GAME_SIZE; x++) {
                verify(game).isAlive(x, y);
            }
        }
        gameViewer.window.dispatchEvent(new WindowEvent(gameViewer.window, WINDOW_CLOSING));
        verify(gameLoop).stop();
        verifyNoMoreInteractions(game, gameLoop);
    }
}