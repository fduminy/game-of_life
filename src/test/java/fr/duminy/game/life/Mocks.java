package fr.duminy.game.life;

import org.mockito.Mockito;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static fr.duminy.game.life.DefaultGameChangerTest.NUMBER_OF_CELLS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class Mocks {
    static CellIterator mockCellIterator() {
        return mockCellIterator(0, 0);
    }

    static CellIterator mockCellIterator(int x, int y) {
        CellIterator cellIterator = Mockito.mock(CellIterator.class);
        mock(cellIterator, x, y);
        return cellIterator;
    }

    static void mock(Game game, CellIterator cellIterator) {
        mock(game, cellIterator, 0, 0);
    }

    static CellIterator stubCellIterator(MutableGameModel gameModel) {
        CellIterator cellIterator = spy(stubCellIterator(0, 0));
        when(gameModel.iterator()).thenReturn(cellIterator);
        return cellIterator;
    }

    public static void mock(Game game, CellIterator cellIterator, int x, int y) {
        when(game.iterator()).thenReturn(cellIterator);
        mock(cellIterator, x, y);
    }

    public static CellIterator stubCellIterator(int x, int y, CellView cellView) {
        return new StubCellIterator(x, y, cellView);
    }

    public static CellIterator stubCellIterator(int x, int y) {
        return new StubCellIterator(x, y);
    }

    public static void mock(CellIterator cellIterator, int x, int y) {
        final int[] index = {x + y * GAME_SIZE};
        final int[] _x = {x};
        final int[] _y = {y};
        doAnswer(answer -> {
            index[0]++;
            _x[0]++;
            if (_x[0] >= GAME_SIZE) {
                _x[0] = 0;
                _y[0]++;
            }
            return null;
        }).when(cellIterator).next();
        when(cellIterator.getX()).then(answer -> _x[0]);
        when(cellIterator.getY()).then(answer -> _y[0]);
        when(cellIterator.getIndex()).then(answer -> index[0]);
        when(cellIterator.hasNext()).then(answer -> index[0] < NUMBER_OF_CELLS);
    }
}
