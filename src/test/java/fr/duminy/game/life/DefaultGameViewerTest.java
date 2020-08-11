package fr.duminy.game.life;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGameViewerTest {
    @Mock
    private Game game;

    @Test
    void view() {
        when(game.getSize()).thenReturn(GAME_SIZE);

        new DefaultGameViewer().view(game);

        verify(game).getSize();
        for (int y = 0; y < GAME_SIZE; y++) {
            for (int x = 0; x < GAME_SIZE; x++) {
                verify(game).isAlive(x, y);
            }
        }
        verifyNoMoreInteractions(game);
    }
}