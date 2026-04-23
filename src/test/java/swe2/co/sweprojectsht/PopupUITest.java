package swe2.co.sweprojectsht;

import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class PopupUITest {

    @BeforeAll
    static void initJavaFx() {
        FxTestUtils.initJavaFx();
    }

    @Test
    @DisplayName("Swap Rule popup should be visible after the first move")
    void testSwapRulePopupVisible() throws Exception {
        QuaxGUI gui = FxTestUtils.callOnFxThreadAndWait(QuaxGUI::new);
        GameEngine engine = (GameEngine) getPrivateField(gui, "engine");

        FxTestUtils.runOnFxThreadAndWait(() -> {
            engine.placePiece(0, 0);
            gui.updateTurnLabel();
        });

        CountDownLatch popupSeen = new CountDownLatch(1);
        CountDownLatch popupClosed = new CountDownLatch(1);

        javafx.application.Platform.runLater(() -> {
            try {
                gui.checkSwapRule();
            } finally {
                popupClosed.countDown();
            }
        });

        Thread.sleep(300);

        javafx.application.Platform.runLater(() -> {
            DialogPane dialog = FxTestUtils.findShowingDialogPaneByHeader("Player 2");
            assertNotNull(dialog);
            assertEquals("Do you want to swap colours?", dialog.getContentText());
            popupSeen.countDown();
            FxTestUtils.clickDialogButton(dialog, "No");
        });

        assertTrue(popupSeen.await(3, TimeUnit.SECONDS));
        assertTrue(popupClosed.await(3, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("BLACK win popup should appear and Play Again should reset the game")
    void testBlackWinnerPopupVisibleAndResetWorks() throws Exception {
        QuaxGUI gui = FxTestUtils.callOnFxThreadAndWait(QuaxGUI::new);
        Board board = (Board) getPrivateField(gui, "board");
        GameEngine engine = (GameEngine) getPrivateField(gui, "engine");
        Label turnLabel = (Label) getPrivateField(gui, "turn");

        FxTestUtils.runOnFxThreadAndWait(() -> {
            for (int r = 0; r < 10; r++) {
                board.getOctagon(r, 0).setOwner(Player.BLACK);
            }
        });

        CountDownLatch popupSeen = new CountDownLatch(1);
        CountDownLatch popupClosed = new CountDownLatch(1);

        javafx.application.Platform.runLater(() -> {
            try {
                engine.placePiece(10, 0);
            } finally {
                popupClosed.countDown();
            }
        });

        Thread.sleep(300);

        javafx.application.Platform.runLater(() -> {
            DialogPane dialog = FxTestUtils.findShowingDialogPaneByHeader("Winner: BLACK");
            assertNotNull(dialog);
            assertEquals("Play again?", dialog.getContentText());
            popupSeen.countDown();
            FxTestUtils.clickDialogButton(dialog, "Play Again");
        });

        assertTrue(popupSeen.await(3, TimeUnit.SECONDS));
        assertTrue(popupClosed.await(3, TimeUnit.SECONDS));

        FxTestUtils.runOnFxThreadAndWait(() -> {});

        assertEquals(" --------> BLACK to play:", turnLabel.getText());
        assertTrue(gui.getCenter() instanceof BoardRenderer);
    }

    @Test
    @DisplayName("WHITE win popup should appear and Play Again should reset the game")
    void testWhiteWinnerPopupVisibleAndResetWorks() throws Exception {
        QuaxGUI gui = FxTestUtils.callOnFxThreadAndWait(QuaxGUI::new);
        Board board = (Board) getPrivateField(gui, "board");
        GameEngine engine = (GameEngine) getPrivateField(gui, "engine");
        Label turnLabel = (Label) getPrivateField(gui, "turn");

        FxTestUtils.runOnFxThreadAndWait(() -> {
            engine.placePiece(10, 10); // BLACK dummy move so WHITE becomes current player
            for (int c = 0; c < 10; c++) {
                board.getOctagon(0, c).setOwner(Player.WHITE);
            }
        });

        CountDownLatch popupSeen = new CountDownLatch(1);
        CountDownLatch popupClosed = new CountDownLatch(1);

        javafx.application.Platform.runLater(() -> {
            try {
                engine.placePiece(0, 10);
            } finally {
                popupClosed.countDown();
            }
        });

        Thread.sleep(300);

        javafx.application.Platform.runLater(() -> {
            DialogPane dialog = FxTestUtils.findShowingDialogPaneByHeader("Winner: WHITE");
            assertNotNull(dialog);
            assertEquals("Play again?", dialog.getContentText());
            popupSeen.countDown();
            FxTestUtils.clickDialogButton(dialog, "Play Again");
        });

        assertTrue(popupSeen.await(3, TimeUnit.SECONDS));
        assertTrue(popupClosed.await(3, TimeUnit.SECONDS));

        FxTestUtils.runOnFxThreadAndWait(() -> {});

        assertEquals(" --------> BLACK to play:", turnLabel.getText());
        assertTrue(gui.getCenter() instanceof BoardRenderer);
    }

    private static Object getPrivateField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}
