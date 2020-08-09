package fr.duminy.game.life;

import java.util.ArrayList;
import java.util.List;

public class DefaultGameEvolution implements GameEvolution {
    private final List<Update> updates = new ArrayList<>();

    @Override
    public void setAlive(int x, int y, boolean alive) {
        updates.add(new Update(x, y, alive));
    }

    @Override
    public void update(Game game) {
        for (Update update : updates) {
            game.setAlive(update.x, update.y, update.alive);
        }
        updates.clear();
    }

    private static class Update {
        private final int x;
        private final int y;
        private final boolean alive;

        private Update(int x, int y, boolean alive) {
            this.x = x;
            this.y = y;
            this.alive = alive;
        }
    }
}
