package swe2.co.sweprojectsht;

public class Board {
    private final int SIZE = 11;
    private OctagonalCell[][] stoneGrid;
    private RhombicCell[][] tileGrid;

    public Board() {
        stoneGrid = new OctagonalCell[SIZE][SIZE];
        tileGrid = new RhombicCell[SIZE - 1][SIZE - 1];
        initialise();
    }

    private void initialise() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                stoneGrid[i][j] = new OctagonalCell();

                if (i < SIZE - 1 && j < SIZE - 1) {
                    tileGrid[i][j] = new RhombicCell();
                }
            }
        }
    }

    public boolean canPlaceStone(int r, int c) {
        return stoneGrid[r][c].getOwner().equals("NONE");
    }

    public void placeStone(int r, int c, String colour) {
        stoneGrid[r][c].setOwner(colour);
    }
}
