package swe2.co.sweprojectsht;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class BoardRenderer extends Pane {
    private Board board;        // Reference to the games state
    private GameEngine engine;  // Reference to the games logic

    public BoardRenderer(Board board, GameEngine engine) {
        this.board = board;
        this.engine = engine;

        // Render again every time the window is resized, so the game fills the new window
        widthProperty().addListener(e -> render());
        heightProperty().addListener(e -> render());
    }

    // Clears and redraws the board based on the current game state
    public void render() {

        getChildren().clear(); // Clears the previous drawing

        double width = getWidth(); // Gets size of the window
        double height = getHeight();

        double boardSize = Math.min(width, height); // Keeps the board as a square
        double framePadding = boardSize * 0.08;     // Adds margin
        double margin = framePadding;

        // Playable area inside the margins
        double playable = boardSize - margin * 2;

        // Distance between octagon centres
        double spacing = playable / (Board.SIZE - 1);

        // Radius of an octagon to make sure they touch perfectly
        double octRadius = spacing / (2 * Math.cos(Math.toRadians(22.5)));

        // Size of the diamonds between the octagons
        double diamondSize = (2 * octRadius * Math.sin(Math.toRadians(22.5))) / Math.sqrt(2);

        // OUTER FRAME SIZES
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

        // INNER FRAME SIZES
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

        // Draw 'goals'
        drawGoalBands(margin, playable, spacing);

        // Draw diamonds
        drawDiamonds(margin, spacing, diamondSize);

        // Draw octagons
        for (int r = 0; r < Board.SIZE; r++) {

            for (int c = 0; c < Board.SIZE; c++) {

                double cx = margin + c * spacing;
                double cy = margin + r * spacing;

                Polygon oct = createOctagon(cx, cy, octRadius);

                OctagonalCell cell = board.getOctagon(r, c);

                // Set cell colour based on ownership
                if (cell.getOwner() == Player.BLACK) {
                    oct.setFill(Color.BLACK);
                } else if (cell.getOwner() == Player.WHITE) {
                    oct.setFill(Color.WHITE);
                } else {
                    oct.setFill(Color.LIGHTGRAY);
                }

                int rr = r;
                int cc = c;

                // Handle clicks to place a piece
                oct.setOnMouseClicked(e -> {

                    if (engine.placePiece(rr, cc)) {
                        render();                               // Redraw Board
                        engine.getQuaxGUI().updateTurnLabel();  // Update turn label
                        engine.getQuaxGUI().checkSwapRule();    // Check if there should be a swap rule
                    }
                });

                getChildren().add(oct);
            }
        }

        // Draw coordinate labels
        drawCoordinates(margin, spacing);
    }

    // Draws row numbers along the left and also column letters along the bottom
    private void drawCoordinates(double margin, double spacing) {

        String[] letters = {"A","B","C","D","E","F","G","H","I","J","K"};

        // Row numbers on the left
        for (int r = 0; r < Board.SIZE; r++) {

            double y = margin + r * spacing;

            Label rowLabel = new Label(String.valueOf(r + 1));
            rowLabel.setStyle("-fx-font-weight: bold;");
            rowLabel.setLayoutX(margin - spacing * 0.8);
            rowLabel.setLayoutY(y - 8);

            getChildren().add(rowLabel);
        }

        // Column letters on the bottom
        for (int c = 0; c < Board.SIZE; c++) {

            double x = margin + c * spacing;

            Label colLabel = new Label(letters[c]);
            colLabel.setStyle("-fx-font-weight: bold;");
            colLabel.setLayoutX(x - 4);
            colLabel.setLayoutY(margin + spacing * (Board.SIZE - 1) + spacing * 0.6);

            getChildren().add(colLabel);
        }
    }

    // Creates octagons at  specific coordinates
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

    // Draws diamonds
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

                // Sets colour based on opwnership
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

                // Handles clicks to place piece
                d.setOnMouseClicked(e -> {
                    if (engine.placeBridge(rr, cc)) {
                        render();
                        engine.getQuaxGUI().updateTurnLabel(); // Update the turn after the placement
                    }
                });

                getChildren().add(d);
            }
        }
    }

    // Draws the 'goals' at each end of the board
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