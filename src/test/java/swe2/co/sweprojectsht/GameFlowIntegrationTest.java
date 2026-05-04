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
    @DisplayName("Human and bot octagon moves should update board correctly")
    void testHumanAndBotPieceFlow() {
        assertTrue(engine.placePiece(0, 0));
        assertEquals(Player.BLACK, board.getOctagon(0, 0).getOwner());

        assertTrue(engine.botPlacePiece(0, 1));
        assertEquals(Player.WHITE, board.getOctagon(0, 1).getOwner());
    }

    @Test
    @DisplayName("Human piece followed by bot bridge should update board correctly")
    void testPieceThenBotBridgeFlow() {
        assertTrue(engine.placePiece(1, 1));
        assertTrue(engine.botPlaceBridge(1, 1));

        assertEquals(Player.BLACK, board.getOctagon(1, 1).getOwner());
        assertEquals(Player.WHITE, board.getDiamond(1, 1).getOwner());
    }

    @Test
    @DisplayName("Invalid repeated octagon move should leave board state unchanged")
    void testRepeatedPieceMoveDoesNotOverwriteCell() {
        assertTrue(engine.placePiece(2, 2));
        Player ownerAfterFirstMove = board.getOctagon(2, 2).getOwner();

        assertFalse(engine.placePiece(2, 2));

        assertEquals(Player.BLACK, ownerAfterFirstMove);
        assertEquals(ownerAfterFirstMove, board.getOctagon(2, 2).getOwner());
        assertEquals(Player.BLACK, engine.getHumanPlayer());
    }

    @Test
    @DisplayName("Invalid repeated diamond move should leave bridge owner unchanged")
    void testRepeatedBridgeMoveDoesNotOverwriteDiamond() {
        assertTrue(engine.placeBridge(3, 3));
        Player ownerAfterFirstMove = board.getDiamond(3, 3).getOwner();

        assertFalse(engine.placeBridge(3, 3));

        assertEquals(ownerAfterFirstMove, board.getDiamond(3, 3).getOwner());
        assertEquals(Player.BLACK, engine.getHumanPlayer());
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
        assertEquals(Player.WHITE, engine.getHumanPlayer());
        assertEquals(Player.BLACK, engine.getBotPlayer());
    }
}
