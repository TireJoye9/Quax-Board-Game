package swe2.co.sweprojectsht;

public class Board {
    public static final int SIZE = 11;

    private OctagonalCell[][] octagons;
    private RhombicCell[][] diamonds; //renamed to make it easier to understand

    public Board() {
        octagons = new OctagonalCell[SIZE][SIZE];
        diamonds = new RhombicCell[SIZE - 1][SIZE - 1];

        createCells(); //much simpler logic, make new classes for each action
        connectCells();
    }

    private void createCells() {
        for(int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                octagons[r][c] = new OctagonalCell(r, c);
            }
        }

        for(int r = 0; r < SIZE - 1; r++) {
            for (int c = 0; c < SIZE - 1; c++) {
                diamonds[r][c] = new RhombicCell(r, c);
            }
        }
    }

    private void connectCells() {
        int[][] dirs = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {

                OctagonalCell cell = octagons[r][c];

                for (int[] d : dirs) {
                    int nr = r + d[0];
                    int nc = c + d[1];

                    if (inBounds(nr, nc)) {
                        cell.addNeighbour(octagons[nr][nc]);
                    }
                }
            }
        }

        for (int r = 0; r < SIZE - 1; r++) {
            for (int c = 0; c < SIZE - 1; c++) {

                RhombicCell d = diamonds[r][c];

                d.connect(octagons[r][c]);
                d.connect(octagons[r + 1][c]);
                d.connect(octagons[r][c + 1]);
                d.connect(octagons[r + 1][c + 1]);
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    public OctagonalCell getOctagon(int r, int c) {
        return octagons[r][c];
    }

    public RhombicCell getDiamond(int r, int c) {
        return diamonds[r][c];
    }

    public void reset() {
        // clears all octagons and diamonds
    }
}
