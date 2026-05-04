package swe2.co.sweprojectsht;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    @DisplayName("Board should have correct dimensions")
    void testBoardDimensions() {
        assertEquals(11, Board.SIZE);
    }

    @Test
    @DisplayName("Board should create all octagonal cells")
    void testOctagonCreation() {
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                OctagonalCell cell = board.getOctagon(r, c);
                assertNotNull(cell);
                assertEquals(r, cell.row);
                assertEquals(c, cell.col);
                assertEquals(Player.NONE, cell.getOwner());
            }
        }
    }

    @Test
    @DisplayName("Board should create all rhombic cells")
    void testDiamondCreation() {
        for (int r = 0; r < Board.SIZE - 1; r++) {
            for (int c = 0; c < Board.SIZE - 1; c++) {
                RhombicCell cell = board.getDiamond(r, c);
                assertNotNull(cell);
                assertEquals(r, cell.row);
                assertEquals(c, cell.col);
                assertEquals(Player.NONE, cell.getOwner());
            }
        }
    }

    @Test
    @DisplayName("Octagonal cells should be connected to octagons and diamonds")
    void testOctagonConnections() {
        // Interior octagon: 4 orthogonal octagons + 4 diamonds
        OctagonalCell interior = board.getOctagon(5, 5);
        assertEquals(8, interior.getNeighbours().size());

        // Corner octagons: 2 orthogonal octagons + 1 diamond
        OctagonalCell topLeft = board.getOctagon(0, 0);
        assertEquals(3, topLeft.getNeighbours().size());

        OctagonalCell topRight = board.getOctagon(0, 10);
        assertEquals(3, topRight.getNeighbours().size());

        OctagonalCell bottomLeft = board.getOctagon(10, 0);
        assertEquals(3, bottomLeft.getNeighbours().size());

        OctagonalCell bottomRight = board.getOctagon(10, 10);
        assertEquals(3, bottomRight.getNeighbours().size());

        // Edge non-corner octagons: 3 orthogonal octagons + 2 diamonds
        OctagonalCell topEdge = board.getOctagon(0, 5);
        assertEquals(5, topEdge.getNeighbours().size());

        OctagonalCell leftEdge = board.getOctagon(5, 0);
        assertEquals(5, leftEdge.getNeighbours().size());
    }

    @Test
    @DisplayName("Diamonds should be connected to four octagons")
    void testDiamondConnections() {
        RhombicCell diamond = board.getDiamond(5, 5);

        // Diamond should have 4 octagon neighbours
        assertEquals(4, diamond.getNeighbours().size());

        // Check the specific octagons it connects to
        assertTrue(diamond.getNeighbours().contains(board.getOctagon(5, 5)));
        assertTrue(diamond.getNeighbours().contains(board.getOctagon(5, 6)));
        assertTrue(diamond.getNeighbours().contains(board.getOctagon(6, 5)));
        assertTrue(diamond.getNeighbours().contains(board.getOctagon(6, 6)));
    }

    @Test
    @DisplayName("Diamond connections should be bidirectional")
    void testDiamondBidirectionalConnections() {
        RhombicCell diamond = board.getDiamond(3, 3);
        OctagonalCell octagon = board.getOctagon(3, 3);

        assertTrue(diamond.getNeighbours().contains(octagon));
        assertTrue(octagon.getNeighbours().contains(diamond));
    }

    @Test
    @DisplayName("Getting out of bounds octagon should throw exception")
    void testGetOctagonOutOfBounds() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.getOctagon(-1, 0);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.getOctagon(11, 0);
        });
    }

    @Test
    @DisplayName("Getting out of bounds diamond should throw exception")
    void testGetDiamondOutOfBounds() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.getDiamond(-1, 0);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.getDiamond(10, 5); // SIZE-1 = 10 is out of bounds for diamonds
        });
    }
}