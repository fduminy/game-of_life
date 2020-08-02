package fr.duminy.game.life;

import static fr.duminy.game.life.CellView.Position.CENTER;
import static fr.duminy.game.life.CellView.Position.values;

public class DefaultRule implements Rule {
    @Override
    public boolean evolve(CellView view) {
        int count = 0;
        for (CellView.Position position : values()) {
            if ((position != CENTER) && view.isAlive(position)) {
                count++;
            }
        }

        boolean alive = view.isAlive(CENTER);
        if (alive && ((count < 2) || (count > 3))) {
            alive = false;
        } else if (!alive && (count == 3)) {
            alive = true;
        }

        return alive;
    }
}
