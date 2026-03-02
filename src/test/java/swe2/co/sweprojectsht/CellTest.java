package swe2.co.sweprojectsht;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Created test due to bug assuming every tile is occupied
class CellTest {

    @Test
    void setOwner() {
        OctagonalCellTemplate octagonalCellTemplate = new OctagonalCellTemplate();
        octagonalCellTemplate.setOwner("NONE");
        assertEquals("NONE",octagonalCellTemplate.getOwner());
    }

    @Test
    void isUnoccupiedIfNoTileWasPlaced() {
        OctagonalCellTemplate octagonalCellTemplate = new OctagonalCellTemplate();
        octagonalCellTemplate.setOwner("NONE");
        assertFalse(octagonalCellTemplate.isOcuupied());
    }

    @Test
    void isUnoccupiedIfNoTileWasNotPlaced() {
        OctagonalCellTemplate octagonalCellTemplate = new OctagonalCellTemplate();
        octagonalCellTemplate.setOwner("BLACK");
        assertTrue(octagonalCellTemplate.isOcuupied());
    }
}