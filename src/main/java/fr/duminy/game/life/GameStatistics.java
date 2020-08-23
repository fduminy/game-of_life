package fr.duminy.game.life;

import java.time.Duration;

public interface GameStatistics {
    void addGeneration(Duration duration);

    String getStringToView();
}
