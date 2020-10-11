package fr.duminy.game.life;

public class DefaultGameChanger implements GameChanger {
    @Override
    public void evolve(Game game, GameEvolution gameEvolution, Rule rule) {
        for (CellIterator cellIterator = game.iterator(); cellIterator.hasNext(); cellIterator.next()) {
            gameEvolution.setAlive(cellIterator.getX(), cellIterator.getY(), rule.evolve(cellIterator.cellView()));
        }
    }
}
