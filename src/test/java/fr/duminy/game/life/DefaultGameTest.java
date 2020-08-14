package fr.duminy.game.life;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SoftAssertionsExtension.class)
class DefaultGameTest {
    @Property
    void getSize() {
        assertThat(new DefaultGame(GAME_SIZE).getSize()).isEqualTo(GAME_SIZE);
    }

    @Property
    void isAlive_returns_false_with_default_constructor(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y) {
        assertThat(new DefaultGame(GAME_SIZE).isAlive(x, y)).isFalse();
    }

    @Property
    void isAlive_returns_value_given_to_setAlive(@ForAll @IntRange(max = GAME_SIZE - 1) int x, @ForAll @IntRange(max = GAME_SIZE - 1) int y,
                                                 @ForAll boolean alive) {
        DefaultGame game = new DefaultGame(GAME_SIZE);
        game.setAlive(x, y, alive);
        assertThat(game.isAlive(x, y)).isEqualTo(alive);
    }

    @Test
    void iterator(SoftAssertions softly) {
        DefaultGame game = new DefaultGame(GAME_SIZE);

        CellIterator cellIterator1 = game.iterator();
        CellIterator cellIterator2 = game.iterator();

        assertIterator(softly, cellIterator1);
        assertIterator(softly, cellIterator2);
        softly.assertThat(cellIterator2).isNotSameAs(cellIterator1);
    }

    private void assertIterator(SoftAssertions softly, CellIterator cellIterator) {
        softly.assertThat(cellIterator).isExactlyInstanceOf(DefaultCellIterator.class);
        if (softly.wasSuccess()) {
            softly.assertThat(cellIterator.getX()).isZero();
            softly.assertThat(cellIterator.getY()).isZero();
            softly.assertThat(cellIterator.getIndex()).isZero();
        }
    }
}