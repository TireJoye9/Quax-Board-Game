package swe2.co.sweprojectsht;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class OctagonalCellTest {

    private OctagonalCell cell;

    @BeforeEach
    void setUp() {
        cell = new OctagonalCell(2, 3);
    }

    @Test
    @DisplayName("OctagonalCell should be created with correct row and column")
    void testCellCreation() {
        assertEquals(2, cell.row);
        assertEquals(3, cell.col);
    }

    @Test
    @DisplayName("New OctagonalCell should have NONE owner")
    void testInitialOwner() {
        assertEquals(Player.NONE, cell.getOwner());
    }

    @Test
    @DisplayName("Should set and get owner correctly")
    void testSetAndGetOwner() {
        cell.setOwner(Player.BLACK);
        assertEquals(Player.BLACK, cell.getOwner());

        cell.setOwner(Player.WHITE);
        assertEquals(Player.WHITE, cell.getOwner());
    }

    @Test
    @DisplayName("Should allow setting owner back to NONE")
    void testSetOwnerToNone() {
        cell.setOwner(Player.BLACK);
        cell.setOwner(Player.NONE);
        assertEquals(Player.NONE, cell.getOwner());
    }
}