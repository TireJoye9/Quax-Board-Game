package swe2.co.sweprojectsht;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class BoardRenderer extends Pane {
    private Board board;
    private GameEngine engine;

    public BoardRenderer(Board board, GameEngine engine) {
        this.board = board;
        this.engine = engine;

        widthProperty().addListener(e -> render());
        heightProperty().addListener(e -> render());
    }

    public void render() {

        getChildren().clear();

        double width = getWidth();
        double height = getHeight();

        double boardSize = Math.min(width, height);
        double margin = boardSize * 0.05;

        double playable = boardSize - margin * 2;

        double spacing = playable / (Board.SIZE - 1);

        double octRadius = spacing / (2 * Math.cos(Math.toRadians(22.5)));

        double diamondSize = (2 * octRadius * Math.sin(Math.toRadians(22.5))) / Math.sqrt(2);

        drawDiamonds(margin, spacing, diamondSize);

        //diamonds drawing
//        for (int r = 0; r < Board.SIZE - 1; r++) {
//
//            for (int c = 0; c < Board.SIZE - 1; c++) {
//
//                double cx = margin + c * spacing + spacing / 2;
//                double cy = margin + r * spacing + spacing / 2;
//
//                Polygon d = new Polygon(
//                        cx, cy - diamondSize,
//                        cx + diamondSize, cy,
//                        cx, cy + diamondSize,
//                        cx - diamondSize, cy
//                );
//
//                d.setFill(Color.GRAY);
//                d.setStroke(Color.BLACK);
//
//                getChildren().add(d);
//            }
//        }

        //octagons drawing
        for (int r = 0; r < Board.SIZE; r++) {

            for (int c = 0; c < Board.SIZE; c++) {

                double cx = margin + c * spacing;
                double cy = margin + r * spacing;

                Polygon oct = createOctagon(cx, cy, octRadius);

                OctagonalCell cell = board.getOctagon(r, c);

                if (cell.getOwner() == Player.BLACK)
                    oct.setFill(Color.BLACK);

                else if (cell.getOwner() == Player.WHITE)
                    oct.setFill(Color.WHITE);

                else
                    oct.setFill(Color.LIGHTGRAY);

                int rr = r;
                int cc = c;

                oct.setOnMouseClicked(e -> {

                    if (engine.placePiece(rr, cc)) {

                        render();
                    }
                });

                getChildren().add(oct);
            }
        }
    }

    private Polygon createOctagon(double cx, double cy, double r) {

        Polygon p = new Polygon();

        for (int i = 0; i < 8; i++) {

            double angle = Math.toRadians(45 * i + 22.5);

            double x = cx + r * Math.cos(angle);
            double y = cy + r * Math.sin(angle);

            p.getPoints().addAll(x, y);
        }

        p.setStroke(Color.BLACK);

        return p;
    }

    private void drawDiamonds(double margin, double spacing, double size) {
        for (int r = 0; r < Board.SIZE - 1; r++) {
            for (int c = 0; c < Board.SIZE - 1; c++) {

                double cx = margin + c * spacing + spacing / 2;
                double cy = margin + r * spacing + spacing / 2;

                Polygon d = new Polygon(
                        cx, cy - size,
                        cx + size, cy,
                        cx, cy + size,
                        cx - size, cy
                );

                RhombicCell cell = board.getDiamond(r, c);

                if (cell.getOwner() == Player.BLACK)
                    d.setFill(Color.BLACK);
                else if (cell.getOwner() == Player.WHITE)
                    d.setFill(Color.WHITE);
                else
                    d.setFill(Color.GRAY);

                d.setStroke(Color.BLACK);

                int rr = r;
                int cc = c;

                d.setOnMouseClicked(e -> {
                    if (engine.placeBridge(rr, cc)) {
                        render();
                    }
                });

                getChildren().add(d);
            }
        }
    }
}