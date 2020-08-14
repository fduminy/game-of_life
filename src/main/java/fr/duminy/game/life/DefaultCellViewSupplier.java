package fr.duminy.game.life;

import java.util.function.Function;

public class DefaultCellViewSupplier implements Function<CellIterator, CellView> {
    private final CellView[] cellViews;

    public DefaultCellViewSupplier(Game game) {
        int size = game.getSize();
        cellViews = new CellView[size * size];
        for (CellIterator cellIterator = game.iterator(); cellIterator.hasNext(); cellIterator.next()) {
            cellViews[cellIterator.getIndex()] = new DefaultCellView(game, cellIterator.getX(), cellIterator.getY());
        }
    }

    @Override
    public CellView apply(CellIterator cellIterator) {
        return cellViews[cellIterator.getIndex()];
    }
}
