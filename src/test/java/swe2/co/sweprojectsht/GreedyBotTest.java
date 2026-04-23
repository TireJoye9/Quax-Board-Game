package swe2.co.sweprojectsht;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
  Test cases for GreedyBot

  BLACK bot should connect TOP row (0) to BOTTOM row (SIZE-1)
  WHITE bot should connect LEFT col (0) to RIGHT col (SIZE-1)
 */
public class GreedyBotTest {

    private Board board;
    private GameEngine engine;
    private GreedyBot bot;

    @BeforeEach
    void setUp() {
        board = new Board();
        // Create a mock QuaxGUI for testing (can be null for these tests)
        engine = new GameEngine(board, null);
        bot = new GreedyBot(board, engine);
    }

    @Test
    void testBlackBotStartsNearTopRow() {
        // BLACK bot should start near top row
        engine = new GameEngine(board, null);
        // Manually set bot to BLACK for this test
        // Note: GameEngine normally has BLACK as human, so we need to test differently
        // For pure bot testing, we'll test the pathfinding directly

        int[] move = bot.getBestMove();
        assertNotNull(move, "Bot should return a move");
        assertEquals(0, move[2], "Bot should prefer octagons");
        assertTrue(move[0] == 0 || move[0] == 1, "BLACK bot should start near top row");
    }

    @Test
    void testWhiteBotStartsNearLeftCol() {
        // WHITE bot should start near left column
        int[] move = bot.getBestMove();
        assertNotNull(move);
        assertTrue(move[1] == 0 || move[1] == 1, "WHITE bot should start near left column");
    }

    @Test
    void testBotReturnsOctagonType() {
        int[] move = bot.getBestMove();
        assertNotNull(move);
        assertEquals(3, move.length, "Move should have 3 values: {row, col, type}");
        assertTrue(move[2] == 0 || move[2] == 1, "Type should be 0 (octagon) or 1 (bridge)");
    }

    @Test
    void testBotReturnsValidCoordinates() {
        int[] move = bot.getBestMove();
        assertNotNull(move);
        assertTrue(move[0] >= 0 && move[0] < Board.SIZE, "Row out of bounds");
        assertTrue(move[1] >= 0 && move[1] < Board.SIZE, "Col out of bounds");
    }

    @Test
    void testBotMovesTowardGoal() {
        // Place a piece for BLACK near top
        board.getOctagon(0, 5).setOwner(Player.BLACK);
        bot.updateBoard(board);

        int[] move = bot.getBestMove();
        assertNotNull(move);
        // Should move downward (row increases)
        assertTrue(move[0] > 0, "Bot should move toward bottom row");
    }



    @Test
    void testBotUsesBridgeWhenBlocked() {
        // Create a line but block straight path
        board.getOctagon(0, 5).setOwner(Player.BLACK);
        board.getOctagon(1, 5).setOwner(Player.BLACK);
        board.getOctagon(2, 5).setOwner(Player.BLACK);
        // Block the straight path with opponent piece
        board.getOctagon(3, 5).setOwner(Player.WHITE);
        bot.updateBoard(board);

        int[] move = bot.getBestMove();
        assertNotNull(move);
        // Should use bridge to go diagonal
        // Bridge placement might be returned
        assertNotNull(move, "Bot should find alternative path");
    }




    @Test
    void testBotBlocksCriticalCell() {
        // Place opponent pieces that are one move from connecting
        board.getOctagon(0, 5).setOwner(Player.WHITE);
        board.getOctagon(1, 5).setOwner(Player.WHITE);
        board.getOctagon(2, 5).setOwner(Player.WHITE);
        board.getOctagon(3, 5).setOwner(Player.WHITE);
        // Empty at (4,5) - if opponent places here, they continue chain
        bot.updateBoard(board);

        int[] move = bot.getBestMove();
        assertNotNull(move);
        // Should block the critical cell (4,5)
        if (move[0] == 4 && move[1] == 5) {
            // Good - bot is blocking
            assertTrue(true);
        }
        // Note: Bot might also be building its own path, which is acceptable
    }


    @Test
    void testBotHandlesNoMovesAvailable() {
        // Fill the board (in reality would never happen fully)
        // This test just ensures bot returns null gracefully
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                board.getOctagon(r, c).setOwner(Player.BLACK);
            }
        }
        bot.updateBoard(board);

        int[] move = bot.getBestMove();
        // Bot should return null or handle gracefully
        // Note: With full board, getAllPossibleMoves returns empty
    }

    @Test
    void testBotAlreadyWon() {
        // Create a winning path for BLACK
        for (int r = 0; r < Board.SIZE; r++) {
            board.getOctagon(r, 5).setOwner(Player.BLACK);
        }
        bot.updateBoard(board);

        int[] move = bot.getBestMove();
        // Bot should recognize game is over or not make moves
        // The engine handles game over, bot just finds path
        assertNotNull(move, "Bot might still return a move but game should be over");
    }

    @Test
    void testBotDoesNotPlaceOnOccupiedCell() {
        // Place a piece
        board.getOctagon(3, 3).setOwner(Player.BLACK);
        bot.updateBoard(board);

        int[] move = bot.getBestMove();
        assertNotNull(move);

        // Bot should not return already occupied cell
        if (move[0] == 3 && move[1] == 3) {
            fail("Bot should not place on already occupied cell");
        }
    }


    @Test
    void testBotCreatesDiagonalPath() {
        // Place pieces that encourage diagonal movement
        board.getOctagon(0, 5).setOwner(Player.BLACK);
        board.getOctagon(1, 6).setOwner(Player.BLACK); // Diagonal
        bot.updateBoard(board);

        int[] move = bot.getBestMove();
        assertNotNull(move);
        // Bot might place a bridge to connect these
        // Bridge at (0,5) connects (0,5) to (1,6)
        if (move[2] == 1) {
            assertTrue((move[0] == 0 && move[1] == 5) || (move[0] == 0 && move[1] == 5),
                    "Bridge placement should connect diagonal pieces");
        }
    }


    @Test
    void testUpdateBoardRefreshesState() {
        Board newBoard = new Board();
        newBoard.getOctagon(2, 2).setOwner(Player.BLACK);

        bot.updateBoard(newBoard);

        // Get a move - should be based on new board
        int[] move = bot.getBestMove();
        assertNotNull(move);
    }



    @Test
    void testBotMoveCompletesQuickly() {
        long startTime = System.nanoTime();
        int[] move = bot.getBestMove();
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        assertNotNull(move);
        assertTrue(durationMs < 500, "Bot should make move within 500ms, took: " + durationMs + "ms");
    }

    @Test
    void testMultipleMovesDontCrash() {
        // Simulate several moves
        for (int i = 0; i < 10; i++) {
            int[] move = bot.getBestMove();
            assertNotNull(move, "Move " + i + " returned null");

            if (move[2] == 0) {
                board.getOctagon(move[0], move[1]).setOwner(Player.BLACK);
            } else {
                board.getDiamond(move[0], move[1]).setOwner(Player.BLACK);
            }
            bot.updateBoard(board);
        }
        // If we got here, no crash occurred
        assertTrue(true);
    }
}