package swe2.co.sweprojectsht;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardRendererUITest {

    @BeforeAll
    static void initJavaFx() {
        FxTestUtils.initJavaFx();
    }

    @Test
    @DisplayName("BoardRenderer should create visible nodes when rendered")
    void testRenderCreatesNodes() throws Exception {
        Board board = new Board();
        GameEngine engine = new GameEngine(board, null);

        BoardRenderer renderer = FxTestUtils.callOnFxThreadAndWait(() -> {
            BoardRenderer r = new BoardRenderer(board, engine);
            r.resize(800, 800);
            r.render();
            return r;
        });

        assertFalse(renderer.getChildren().isEmpty());
    }

    @Test
    @DisplayName("BoardRenderer should create 221 polygons for cells")
    void testRenderCreatesAllPolygons() throws Exception {
        Board board = new Board();
        GameEngine engine = new GameEngine(board, null);

        BoardRenderer renderer = FxTestUtils.callOnFxThreadAndWait(() -> {
            BoardRenderer r = new BoardRenderer(board, engine);
            r.resize(800, 800);
            r.render();
            return r;
        });

        long polygonCount = renderer.getChildren().stream()
                .filter(node -> node instanceof Polygon)
                .count();

        assertEquals(221, polygonCount); // 121 octagons + 100 diamonds
    }

    @Test
    @DisplayName("BoardRenderer should create 22 coordinate labels")
    void testRenderCreatesCoordinateLabels() throws Exception {
        Board board = new Board();
        GameEngine engine = new GameEngine(board, null);

        BoardRenderer renderer = FxTestUtils.callOnFxThreadAndWait(() -> {
            BoardRenderer r = new BoardRenderer(board, engine);
            r.resize(800, 800);
            r.render();
            return r;
        });

        long labelCount = renderer.getChildren().stream()
                .filter(node -> node instanceof Label)
                .count();

        assertEquals(22, labelCount);
    }

    @Test
    @DisplayName("BoardRenderer should create 6 rectangles for frame and goal bands")
    void testRenderCreatesRectangles() throws Exception {
        Board board = new Board();
        GameEngine engine = new GameEngine(board, null);

        BoardRenderer renderer = FxTestUtils.callOnFxThreadAndWait(() -> {
            BoardRenderer r = new BoardRenderer(board, engine);
            r.resize(800, 800);
            r.render();
            return r;
        });

        long rectangleCount = renderer.getChildren().stream()
                .filter(node -> node instanceof Rectangle)
                .count();

        assertEquals(6, rectangleCount);
    }

    @Test
    @DisplayName("Rendered polygons should have mouse click handlers attached")
    void testRenderedPolygonsHaveClickHandlers() throws Exception {
        Board board = new Board();
        GameEngine engine = new GameEngine(board, null);

        BoardRenderer renderer = FxTestUtils.callOnFxThreadAndWait(() -> {
            BoardRenderer r = new BoardRenderer(board, engine);
            r.resize(800, 800);
            r.render();
            return r;
        });

        long handlerCount = renderer.getChildren().stream()
                .filter(node -> node instanceof Polygon)
                .filter(node -> ((Node) node).getOnMouseClicked() != null)
                .count();

        assertEquals(221, handlerCount);
    }
}
