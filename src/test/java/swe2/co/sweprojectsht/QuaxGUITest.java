package swe2.co.sweprojectsht;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class QuaxGUITest {

    @BeforeAll
    static void initJavaFX() throws Exception {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // JavaFX already started
        }
    }

    @Test
    @DisplayName("QuaxGUI should start with BLACK turn label")
    void testInitialTurnLabel() throws Exception {
        QuaxGUI gui = runOnFxThreadAndWait(QuaxGUI::new);

        Label turnLabel = (Label) getPrivateField(gui, "turn");

        assertNotNull(turnLabel);
        assertEquals(" --------> BLACK to play:", turnLabel.getText());
        assertNotNull(turnLabel.getGraphic());
    }

    @Test
    @DisplayName("QuaxGUI should place BoardRenderer in the center")
    void testBoardRendererPlacedInCenter() throws Exception {
        QuaxGUI gui = runOnFxThreadAndWait(QuaxGUI::new);

        assertNotNull(gui.getCenter());
        assertTrue(gui.getCenter() instanceof BoardRenderer);
    }

    @Test
    @DisplayName("Turn label should stay BLACK after human places first piece")
    void testTurnLabelAfterMove() throws Exception {
        QuaxGUI gui = runOnFxThreadAndWait(QuaxGUI::new);

        GameEngine engine = (GameEngine) getPrivateField(gui, "engine");
        Label turnLabel = (Label) getPrivateField(gui, "turn");

        runOnFxThreadAndWait(() -> {
            engine.placePiece(0, 0);
            gui.updateTurnLabel();
            return null;
        });

        assertEquals(" --------> BLACK to play:", turnLabel.getText());
        assertNotNull(turnLabel.getGraphic());
    }

    @Test
    @DisplayName("Reset game should restore BLACK turn label")
    void testResetGameRestoresInitialState() throws Exception {
        QuaxGUI gui = runOnFxThreadAndWait(QuaxGUI::new);

        GameEngine engine = (GameEngine) getPrivateField(gui, "engine");
        Label turnLabel = (Label) getPrivateField(gui, "turn");

        runOnFxThreadAndWait(() -> {
            engine.placePiece(0, 0);
            gui.updateTurnLabel();
            gui.resetGame();
            return null;
        });

        assertEquals(" --------> BLACK to play:", turnLabel.getText());
        assertTrue(gui.getCenter() instanceof BoardRenderer);
    }

    @Test
    @DisplayName("Reset game should replace the board renderer instance")
    void testResetGameCreatesNewRenderer() throws Exception {
        QuaxGUI gui = runOnFxThreadAndWait(QuaxGUI::new);

        Object firstRenderer = gui.getCenter();

        runOnFxThreadAndWait(() -> {
            gui.resetGame();
            return null;
        });

        Object secondRenderer = gui.getCenter();

        assertNotNull(firstRenderer);
        assertNotNull(secondRenderer);
        assertTrue(firstRenderer instanceof BoardRenderer);
        assertTrue(secondRenderer instanceof BoardRenderer);
        assertNotSame(firstRenderer, secondRenderer);
    }

    private static Object getPrivateField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private static <T> T runOnFxThreadAndWait(FxSupplier<T> supplier) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Holder<T> holder = new Holder<>();
        Holder<Throwable> errorHolder = new Holder<>();

        Platform.runLater(() -> {
            try {
                holder.value = supplier.get();
            } catch (Throwable t) {
                errorHolder.value = t;
            } finally {
                latch.countDown();
            }
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        if (!completed) {
            fail("Timed out waiting for JavaFX task");
        }

        if (errorHolder.value != null) {
            throw new RuntimeException(errorHolder.value);
        }

        return holder.value;
    }

    @FunctionalInterface
    interface FxSupplier<T> {
        T get() throws Exception;
    }

    static class Holder<T> {
        T value;
    }
}
