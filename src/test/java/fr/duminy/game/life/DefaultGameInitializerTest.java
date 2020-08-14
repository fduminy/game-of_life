package fr.duminy.game.life;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGameInitializerTest {
    @Test
    void initialize(@Mock Game game) {
        DefaultGameInitializer initializer = new DefaultGameInitializer();
        when(game.getSize()).thenReturn(GAME_SIZE);

        initializer.initialize(game);

        for (int y = 0; y < GAME_SIZE; y++) {
            for (int x = 0; x < GAME_SIZE; x++) {
                verify(game).setAlive(eq(x), eq(y), anyBoolean());
            }
        }
    }
}