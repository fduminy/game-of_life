package fr.duminy.game.life;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;

import static fr.duminy.game.life.DefaultCellViewTest.GAME_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

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
}