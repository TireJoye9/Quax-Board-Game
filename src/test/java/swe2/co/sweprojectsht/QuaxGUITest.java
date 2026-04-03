package swe2.co.sweprojectsht;

import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class QuaxGUITest {

    @BeforeAll
    static void initJavaFx() {
        FxTestUtils.initJavaFx();
    }

    @Test
    @DisplayName("QuaxGUI should start with BLACK to play label")
    void testInitialTurnLabel() throws Exception {
        QuaxGUI gui = FxTestUtils.callOnFxThreadAndWait(QuaxGUI::new);
        Label turnLabel = (Label) getPrivateField(gui, "turn");

        assertNotNull(turnLabel);
        assertEquals(" --------> BLACK to play:", turnLabel.getText());
        assertNotNull(turnLabel.getGraphic());
    }

    @Test
    @DisplayName("QuaxGUI should have a BoardRenderer in the centre")
    void testBoardRendererInCenter() throws Exception {
        QuaxGUI gui = FxTestUtils.callOnFxThreadAndWait(QuaxGUI::new);

        assertNotNull(gui.getCenter());
        assertTrue(gui.getCenter() instanceof BoardRenderer);
    }

    @Test
    @DisplayName("Turn label should update to WHITE after BLACK places a piece")
    void testTurnLabelUpdatesToWhiteAfterMove() throws Exception {
        QuaxGUI gui = FxTestUtils.callOnFxThreadAndWait(QuaxGUI::new);
        GameEngine engine = (GameEngine) getPrivateField(gui, "engine");
        Label turnLabel = (Label) getPrivateField(gui, "turn");

        FxTestUtils.runOnFxThreadAndWait(() -> {
            engine.placePiece(0, 0);
            gui.updateTurnLabel();
        });

        assertEquals(" --------> White to play:", turnLabel.getText());
    }

    @Test
    @DisplayName("Reset game should restore BLACK turn label")
    void testResetGameRestoresBlackTurnLabel() throws Exception {
        QuaxGUI gui = FxTestUtils.callOnFxThreadAndWait(QuaxGUI::new);
        GameEngine engine = (GameEngine) getPrivateField(gui, "engine");
        Label turnLabel = (Label) getPrivateField(gui, "turn");

        FxTestUtils.runOnFxThreadAndWait(() -> {
            engine.placePiece(0, 0);
            gui.updateTurnLabel();
            gui.resetGame();
        });

        assertEquals(" --------> BLACK to play:", turnLabel.getText());
        assertTrue(gui.getCenter() instanceof BoardRenderer);
    }

    @Test
    @DisplayName("Reset game should replace the BoardRenderer instance")
    void testResetGameCreatesNewRenderer() throws Exception {
        QuaxGUI gui = FxTestUtils.callOnFxThreadAndWait(QuaxGUI::new);

        Object firstRenderer = gui.getCenter();

        FxTestUtils.runOnFxThreadAndWait(gui::resetGame);

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
}