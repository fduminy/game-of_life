package fr.duminy.game.life;

import static java.lang.Math.max;

public class DefaultRule implements Rule {
    static final int NB_ALIVE_NEIGHBOURS_TO_BORN = 3;
    static final int MIN_NB_ALIVE_NEIGHBOURS_TO_SURVIVE = 2;
    static final int MAX_NB_ALIVE_NEIGHBOURS_TO_SURVIVE = 3;
    static final int NB_ALIVE_NEIGHBOURS_TO_CHECK = max(NB_ALIVE_NEIGHBOURS_TO_BORN, MAX_NB_ALIVE_NEIGHBOURS_TO_SURVIVE);

    @Override
    public boolean evolve(CellView view) {
        int nbNeighbours = 0;
        out:
        for (int deltaY = -1; deltaY < 2; deltaY++) {
            for (int deltaX = -1; deltaX < 2; deltaX++) {
                if (!((deltaX == 0) && (deltaY == 0)) && view.isAlive(deltaX, deltaY)) {
                    nbNeighbours++;
                    if (nbNeighbours > NB_ALIVE_NEIGHBOURS_TO_CHECK) {
                        break out;
                    }
                }
            }
        }

        boolean alive = view.isAlive(0, 0);
        if (alive && ((nbNeighbours < MIN_NB_ALIVE_NEIGHBOURS_TO_SURVIVE) || (nbNeighbours > MAX_NB_ALIVE_NEIGHBOURS_TO_SURVIVE))) {
            alive = false;
        } else if (!alive && (nbNeighbours == NB_ALIVE_NEIGHBOURS_TO_BORN)) {
            alive = true;
        }

        return alive;
    }
}
