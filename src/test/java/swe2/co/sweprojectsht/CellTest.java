// File: src/test/java/swe2/co/sweprojectsht/CellTest.java
package swe2.co.sweprojectsht;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    private TestCell cell;

    @BeforeEach
    void setUp() {
        cell = new TestCell(3, 5);
    }

    @Test
    @DisplayName("Cell should be created with correct row and column")
    void testCellCreation() {
        assertEquals(3, cell.row);
        assertEquals(5, cell.col);
    }

    @Test
    @DisplayName("Cell should initially have no neighbours")
    void testInitialNeighbours() {
        assertTrue(cell.getNeighbours().isEmpty());
    }

    @Test
    @DisplayName("Cell should add neighbours correctly")
    void testAddNeighbour() {
        TestCell neighbour1 = new TestCell(1, 1);
        TestCell neighbour2 = new TestCell(2, 2);

        cell.addNeighbour(neighbour1);
        cell.addNeighbour(neighbour2);

        assertEquals(2, cell.getNeighbours().size());
        assertTrue(cell.getNeighbours().contains(neighbour1));
        assertTrue(cell.getNeighbours().contains(neighbour2));
    }

    @Test
    @DisplayName("Adding same neighbour twice should duplicate it")
    void testAddDuplicateNeighbour() {
        TestCell neighbour = new TestCell(1, 1);
        cell.addNeighbour(neighbour);
        cell.addNeighbour(neighbour);

        assertEquals(2, cell.getNeighbours().size());
    }

    // Test implementation of abstract Cell
    private static class TestCell extends Cell {
        public TestCell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}