package fr.duminy.game.life;

public class DefaultRule implements Rule {
    @Override
    public boolean evolve(CellView view) {
        int count = 0;
        for (int deltaY = -1; deltaY < 2; deltaY++) {
            for (int deltaX = -1; deltaX < 2; deltaX++) {
                if (!((deltaX == 0) && (deltaY == 0)) && view.isAlive(deltaX, deltaY)) {
                    count++;
                }
            }
        }

        boolean alive = view.isAlive(0, 0);
        if (alive && ((count < 2) || (count > 3))) {
            alive = false;
        } else if (!alive && (count == 3)) {
            alive = true;
        }

        return alive;
    }
}
