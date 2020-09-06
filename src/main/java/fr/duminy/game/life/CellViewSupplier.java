package fr.duminy.game.life;

import java.util.function.Function;

interface CellViewSupplier extends Function<CellIterator, CellView> {
}
