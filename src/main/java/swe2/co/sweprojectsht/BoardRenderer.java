package swe2.co.sweprojectsht;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import java.util.Random;


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
        double framePadding = boardSize * 0.20;
        double margin = framePadding;

        double playable = boardSize - margin * 2;

        double spacing = playable / (Board.SIZE - 1);

        double octRadius = spacing / (2 * Math.cos(Math.toRadians(22.5)));

        double diamondSize = (2 * octRadius * Math.sin(Math.toRadians(22.5))) / Math.sqrt(2);

        // outer rectangle
        double outerFrameOffset = spacing * 0.9;

        Rectangle frame = new Rectangle(
                margin - outerFrameOffset,
                margin - outerFrameOffset,
                playable + outerFrameOffset * 2,
                playable + outerFrameOffset * 2
        );

        frame.setFill(Color.BEIGE);
        frame.setStroke(Color.BLACK);
        frame.setStrokeWidth(3);

        getChildren().add(frame);

        // inner rectangle
        double innerFrameOffset = spacing * 0.6;

        Rectangle innerFrame = new Rectangle(
                margin - innerFrameOffset,
                margin - innerFrameOffset,
                playable + innerFrameOffset * 2,
                playable + innerFrameOffset * 2
        );

        innerFrame.setFill(Color.TRANSPARENT);
        innerFrame.setStroke(Color.BLACK);
        innerFrame.setStrokeWidth(3);

        getChildren().add(innerFrame);

        drawGoalBands(margin, playable, spacing);

        drawDiamonds(margin, spacing, diamondSize);

        //octagons drawing
        for (int r = 0; r < Board.SIZE; r++) {

            for (int c = 0; c < Board.SIZE; c++) {

                double cx = margin + c * spacing;
                double cy = margin + r * spacing;

                Polygon oct = createOctagon(cx, cy, octRadius);

                OctagonalCell cell = board.getOctagon(r, c);

                if (cell.getOwner() == Player.BLACK) {
                    oct.setFill(Color.BLACK);
                } else if (cell.getOwner() == Player.WHITE) {
                    oct.setFill(Color.WHITE);
                } else {
                    oct.setFill(Color.LIGHTGRAY);
                }

                int rr = r;
                int cc = c;


                oct.setOnMouseClicked(e -> {


                    if (engine.placePiece(rr, cc)) {
                        render();
                        engine.getQuaxGUI().updateTurnLabel();
                        engine.getQuaxGUI().checkSwapRule();
                    }

                    // After human moves, let bot move if game isn't over
                    if (!engine.checkWin(engine.getHumanPlayer()) ||!engine.checkWin(engine.getBotPlayer())) {
                        engine.getQuaxGUI().makeBotMove();
                    }
                });

                getChildren().add(oct);
            }
        }

        drawCoordinates(margin, spacing);
    }

    private void drawCoordinates(double margin, double spacing) {

        String[] letters = {"A","B","C","D","E","F","G","H","I","J","K"};

        // row numbers on the left
        for (int r = 0; r < Board.SIZE; r++) {

            double y = margin + r * spacing;

            Label rowLabel = new Label(String.valueOf(r + 1));
            rowLabel.setStyle("-fx-font-weight: bold;");
            rowLabel.setLayoutX(margin - spacing * 0.8);
            rowLabel.setLayoutY(y - 8);

            getChildren().add(rowLabel);
        }

        // column letters on the bottom
        for (int c = 0; c < Board.SIZE; c++) {

            double x = margin + c * spacing;

            Label colLabel = new Label(letters[c]);
            colLabel.setStyle("-fx-font-weight: bold;");
            colLabel.setLayoutX(x - 4);
            colLabel.setLayoutY(margin + spacing * (Board.SIZE - 1) + spacing * 0.6);

            getChildren().add(colLabel);
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

                if (cell.getOwner() == Player.BLACK) {
                    d.setFill(Color.BLACK);
                } else if (cell.getOwner() == Player.WHITE) {
                    d.setFill(Color.WHITE);
                } else {
                    d.setFill(Color.GRAY);
                }

                d.setStroke(Color.BLACK);

                int rr = r;
                int cc = c;

                d.setOnMouseClicked(e -> {
                    if (engine.placeBridge(rr, cc)) {
                        render();
                        engine.getQuaxGUI().updateTurnLabel();
                    }
                });

                getChildren().add(d);
            }
        }
    }

    private void drawGoalBands(double margin, double playable, double spacing) {

        double innerOffset = spacing * 0.6;

        double innerLeft = margin - innerOffset;
        double innerTop = margin - innerOffset;
        double innerRight = margin + playable + innerOffset;
        double innerBottom = margin + playable + innerOffset;

        double boardLeft = margin;
        double boardTop = margin;
        double boardRight = margin + playable;
        double boardBottom = margin + playable;

        // TOP band (Black)
        Rectangle topBand = new Rectangle(
                innerLeft,
                innerTop,
                innerRight - innerLeft,
                boardTop - innerTop
        );
        topBand.setFill(Color.BLACK);

        // BOTTOM band (Black)
        Rectangle bottomBand = new Rectangle(
                innerLeft,
                boardBottom,
                innerRight - innerLeft,
                innerBottom - boardBottom
        );
        bottomBand.setFill(Color.BLACK);

        // LEFT band (White)
        Rectangle leftBand = new Rectangle(
                innerLeft,
                innerTop,
                boardLeft - innerLeft,
                innerBottom - innerTop
        );
        leftBand.setFill(Color.WHITE);

        // RIGHT band (White)
        Rectangle rightBand = new Rectangle(
                boardRight,
                innerTop,
                innerRight - boardRight,
                innerBottom - innerTop
        );
        rightBand.setFill(Color.WHITE);

        getChildren().addAll(topBand, bottomBand, leftBand, rightBand);
    }
}

/*
                    if (cell.getOwner() == Player.WHITE)
                    {
                        Random random = new Random();
                        engine.placePiece(random.nextInt(Board.SIZE),random.nextInt(Board.SIZE));
                        render();
                        engine.getQuaxGUI().updateTurnLabel();
                        engine.getQuaxGUI().checkSwapRule();
                    }
                    else
 */