package swe2.co.sweprojectsht;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class GameFlowIntegrationTest {

    private Board board;
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        board = new Board();
        engine = new GameEngine(board, null);
    }

    @Test
    @DisplayName("Two valid octagon moves should alternate BLACK then WHITE")
    void testTwoTurnFlowWithPieces() {
        assertTrue(engine.placePiece(0, 0));
        assertEquals(Player.BLACK, board.getOctagon(0, 0).getOwner());
        assertEquals(Player.WHITE, engine.getHumanPlayer());

        assertTrue(engine.placePiece(0, 1));
        assertEquals(Player.WHITE, board.getOctagon(0, 1).getOwner());
        assertEquals(Player.BLACK, engine.getHumanPlayer());
    }

    @Test
    @DisplayName("Piece move followed by bridge move should update board correctly")
    void testPieceThenBridgeFlow() {
        assertTrue(engine.placePiece(1, 1));   // BLACK
        assertTrue(engine.placeBridge(1, 1));  // WHITE

        assertEquals(Player.BLACK, board.getOctagon(1, 1).getOwner());
        assertEquals(Player.WHITE, board.getDiamond(1, 1).getOwner());
        assertEquals(Player.BLACK, engine.getHumanPlayer());
    }

    @Test
    @DisplayName("Invalid repeated octagon move should leave board state unchanged")
    void testRepeatedPieceMoveDoesNotOverwriteCell() {
        assertTrue(engine.placePiece(2, 2)); // BLACK
        Player ownerAfterFirstMove = board.getOctagon(2, 2).getOwner();

        assertFalse(engine.placePiece(2, 2)); // WHITE tries same cell

        assertEquals(Player.BLACK, ownerAfterFirstMove);
        assertEquals(ownerAfterFirstMove, board.getOctagon(2, 2).getOwner());
        assertEquals(Player.WHITE, engine.getHumanPlayer());
    }

    @Test
    @DisplayName("Invalid repeated diamond move should leave bridge owner unchanged")
    void testRepeatedBridgeMoveDoesNotOverwriteDiamond() {
        assertTrue(engine.placeBridge(3, 3)); // BLACK
        Player ownerAfterFirstMove = board.getDiamond(3, 3).getOwner();

        assertFalse(engine.placeBridge(3, 3)); // WHITE tries same diamond

        assertEquals(ownerAfterFirstMove, board.getDiamond(3, 3).getOwner());
    }

    @Test
    @DisplayName("Swap rule should only become available after first move")
    void testSwapOfferLifecycle() {
        assertFalse(engine.swapOffer());

        engine.placePiece(4, 4);

        assertTrue(engine.swapOffer());

        engine.markSwapOffered();

        assertFalse(engine.swapOffer());
    }

    @Test
    @DisplayName("Performing swap should change first placed piece from BLACK to WHITE")
    void testSwapChangesFirstMoveOwner() {
        engine.placePiece(6, 6);

        assertEquals(Player.BLACK, board.getOctagon(6, 6).getOwner());

        engine.performSwap();

        assertEquals(Player.WHITE, board.getOctagon(6, 6).getOwner());
    }
}
