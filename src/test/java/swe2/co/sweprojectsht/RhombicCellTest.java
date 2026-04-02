package swe2.co.sweprojectsht;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class RhombicCellTest {

    private RhombicCell diamond;
    private OctagonalCell octagon1;
    private OctagonalCell octagon2;

    @BeforeEach
    void setUp() {
        diamond = new RhombicCell(1, 1);
        octagon1 = new OctagonalCell(1, 1);
        octagon2 = new OctagonalCell(2, 2);
    }

    @Test
    @DisplayName("RhombicCell should be created with correct row and column")
    void testCellCreation() {
        assertEquals(1, diamond.row);
        assertEquals(1, diamond.col);
    }

    @Test
    @DisplayName("New RhombicCell should have NONE owner")
    void testInitialOwner() {
        assertEquals(Player.NONE, diamond.getOwner());
    }

    @Test
    @DisplayName("Should set and get owner correctly")
    void testSetAndGetOwner() {
        diamond.setOwner(Player.BLACK);
        assertEquals(Player.BLACK, diamond.getOwner());
    }

    @Test
    @DisplayName("Connect method should create bidirectional connection")
    void testConnect() {
        diamond.connect(octagon1);

        // Diamond should have octagon as neighbour
        assertTrue(diamond.getNeighbours().contains(octagon1));

        // Octagon should have diamond as neighbour
        assertTrue(octagon1.getNeighbours().contains(diamond));
    }

    @Test
    @DisplayName("Connect method should work with multiple octagons")
    void testConnectMultiple() {
        diamond.connect(octagon1);
        diamond.connect(octagon2);

        assertEquals(2, diamond.getNeighbours().size());
        assertTrue(octagon1.getNeighbours().contains(diamond));
        assertTrue(octagon2.getNeighbours().contains(diamond));
    }
}