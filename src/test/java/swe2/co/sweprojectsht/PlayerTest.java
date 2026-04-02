package swe2.co.sweprojectsht;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void playerEnumShouldHaveThreeValues() {
        Player[] players = Player.values();
        assertEquals(3, players.length);
        assertArrayEquals(new Player[]{Player.NONE, Player.BLACK, Player.WHITE}, players);
    }

    @Test
    void playerNONEShouldBeFirst() {
        assertEquals(Player.NONE, Player.values()[0]);
    }
}