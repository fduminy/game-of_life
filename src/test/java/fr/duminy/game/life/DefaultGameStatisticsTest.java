package fr.duminy.game.life;

import org.junit.jupiter.api.Test;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultGameStatisticsTest {
    @Test
    void getStringToView_no_generation() {
        assertThat(new DefaultGameStatistics().getStringToView()).isEqualTo("generation 0 - 0 generations / second");
    }

    @Test
    void getStringToView_one_generation() {
        DefaultGameStatistics statistics = new DefaultGameStatistics();
        statistics.addGeneration(ofSeconds(2));

        assertThat(statistics.getStringToView()).isEqualTo("generation 1 - 0.5 generations / second");
    }

    @Test
    void getStringToView_two_generations() {
        DefaultGameStatistics statistics = new DefaultGameStatistics();
        statistics.addGeneration(ofMillis(500));
        statistics.addGeneration(ofMillis(1500));

        assertThat(statistics.getStringToView()).isEqualTo("generation 2 - 1 generations / second");
    }

    @Test
    void getStringToView_three_generations() {
        DefaultGameStatistics statistics = new DefaultGameStatistics();
        statistics.addGeneration(ofSeconds(1));
        statistics.addGeneration(ofSeconds(3));
        statistics.addGeneration(ofSeconds(95));

        assertThat(statistics.getStringToView()).isEqualTo("generation 3 - 0.03 generations / second");
    }
}