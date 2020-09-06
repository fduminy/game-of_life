package fr.duminy.game.life;

import java.util.function.Function;

public class DefaultCellViewSupplier implements CellViewSupplier {
    private final CellView cellView;

    public DefaultCellViewSupplier(Game game, Function<Game, CellView> cellViewSupplier) {
        cellView = cellViewSupplier.apply(game);
    }

    @Override
    public CellView apply(CellIterator cellIterator) {
        cellView.setLocation(cellIterator.getX(), cellIterator.getY());
        return cellView;
    }
}
