package swe2.co.sweprojectsht;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private Board board;
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        board = new Board();
        engine = new GameEngine(board, null);
    }

    @Test
    @DisplayName("Game should start with human as BLACK and bot as WHITE")
    void testInitialPlayers() {
        assertEquals(Player.BLACK, engine.getHumanPlayer());
        assertEquals(Player.WHITE, engine.getBotPlayer());
    }

    @Test
    @DisplayName("Swap should not be offered before the first move")
    void testSwapNotOfferedInitially() {
        assertFalse(engine.swapOffer());
    }

    @Test
    @DisplayName("Human placing a piece on an empty octagon should succeed")
    void testPlacePieceOnEmptyCell() {
        boolean placed = engine.placePiece(3, 4);

        assertTrue(placed);
        assertEquals(Player.BLACK, board.getOctagon(3, 4).getOwner());
    }

    @Test
    @DisplayName("Human player colour should stay BLACK after placing a piece")
    void testPlacePieceKeepsHumanColour() {
        engine.placePiece(2, 2);

        assertEquals(Player.BLACK, engine.getHumanPlayer());
    }

    @Test
    @DisplayName("First move should enable swap offer")
    void testFirstMoveEnablesSwapOffer() {
        engine.placePiece(1, 1);

        assertTrue(engine.swapOffer());
    }

    @Test
    @DisplayName("Placing a piece on an occupied octagon should fail")
    void testPlacePieceOnOccupiedCellFails() {
        engine.placePiece(4, 4);
        boolean placedAgain = engine.placePiece(4, 4);

        assertFalse(placedAgain);
    }

    @Test
    @DisplayName("Failed octagon placement should not change human player colour")
    void testFailedPiecePlacementDoesNotChangeHumanColour() {
        engine.placePiece(4, 4);
        Player humanBeforeFailedMove = engine.getHumanPlayer();

        boolean placedAgain = engine.placePiece(4, 4);

        assertFalse(placedAgain);
        assertEquals(humanBeforeFailedMove, engine.getHumanPlayer());
    }

    @Test
    @DisplayName("Human placing a bridge on an empty diamond should succeed")
    void testPlaceBridgeOnEmptyDiamond() {
        boolean placed = engine.placeBridge(2, 2);

        assertTrue(placed);
        assertEquals(Player.BLACK, board.getDiamond(2, 2).getOwner());
    }

    @Test
    @DisplayName("Human player colour should stay BLACK after placing a bridge")
    void testPlaceBridgeKeepsHumanColour() {
        engine.placeBridge(3, 3);

        assertEquals(Player.BLACK, engine.getHumanPlayer());
    }

    @Test
    @DisplayName("Placing a bridge on an occupied diamond should fail")
    void testPlaceBridgeOnOccupiedDiamondFails() {
        engine.placeBridge(5, 5);
        boolean placedAgain = engine.placeBridge(5, 5);

        assertFalse(placedAgain);
    }

    @Test
    @DisplayName("Failed bridge placement should not change human player colour")
    void testFailedBridgePlacementDoesNotChangeHumanColour() {
        engine.placeBridge(5, 5);
        Player humanBeforeFailedMove = engine.getHumanPlayer();

        boolean placedAgain = engine.placeBridge(5, 5);

        assertFalse(placedAgain);
        assertEquals(humanBeforeFailedMove, engine.getHumanPlayer());
    }

    @Test
    @DisplayName("Marking swap as offered should disable future swap offer")
    void testMarkSwapOfferedDisablesOffer() {
        engine.placePiece(0, 0);
        assertTrue(engine.swapOffer());

        engine.markSwapOffered();

        assertFalse(engine.swapOffer());
    }

    @Test
    @DisplayName("Performing swap should flip the first move owner and swap human/bot colours")
    void testPerformSwap() {
        engine.placePiece(0, 0);

        engine.performSwap();

        assertEquals(Player.WHITE, board.getOctagon(0, 0).getOwner());
        assertEquals(Player.WHITE, engine.getHumanPlayer());
        assertEquals(Player.BLACK, engine.getBotPlayer());
        assertFalse(engine.swapOffer());
    }
}