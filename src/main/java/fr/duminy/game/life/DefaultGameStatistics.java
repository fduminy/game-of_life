package fr.duminy.game.life;

import java.text.MessageFormat;
import java.time.Duration;

import static java.time.Duration.ZERO;
import static java.util.Locale.ENGLISH;

public class DefaultGameStatistics implements GameStatistics {
    private Duration totalDuration = ZERO;
    private long nbGenerations;

    @Override
    public void addGeneration(Duration duration) {
        totalDuration = totalDuration.plus(duration);
        nbGenerations++;
    }

    @Override
    public String getStringToView() {
        double average = totalDuration.equals(ZERO) ? 0 : (double) nbGenerations / totalDuration.getSeconds();
        MessageFormat messageFormat = new MessageFormat("generation {0} - {1} generations / second");
        messageFormat.setLocale(ENGLISH);
        return messageFormat.format(new Object[]{nbGenerations, average});
    }
}
