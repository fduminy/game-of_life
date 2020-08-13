package fr.duminy.game.life;

import java.util.function.BiFunction;

public class DefaultCellViewSupplier implements BiFunction<Integer, Integer, CellView> {
    private final Game game;
    private final CellView[] cellViews;

    public DefaultCellViewSupplier(Game game) {
        this.game = game;
        cellViews = new CellView[game.getSize() * game.getSize()];
        int size = game.getSize();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                cellViews[index(game, x, y)] = new DefaultCellView(game, x, y);
            }
        }
    }

    @Override
    public CellView apply(Integer x, Integer y) {
        return cellViews[index(game, x, y)];
    }

    private static int index(Game game, Integer x, Integer y) {
        return x + y * game.getSize();
    }
}
