package swe2.co.sweprojectsht;

//Board class
public class Board {
    private final int SIZE = 11;
    //Stone Grid is a 2d array that holds octagonal cells
    private final OctagonalCellTemplate[][] stoneGrid;
    private final RhombicCellTemplate[][] tileGrid;
    int integer;

    public Board() {
        stoneGrid = new OctagonalCellTemplate[SIZE][SIZE];
        //-1 as there is only 10 squares
        tileGrid = new RhombicCellTemplate[SIZE - 1][SIZE - 1];
        initialise();
    }

    //Creates the layout
    private void initialise() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                //initialised stoneGrid
                stoneGrid[i][j] = new OctagonalCellTemplate();
                if (i < SIZE - 1 && j < SIZE - 1) {
                    tileGrid[i][j] = new RhombicCellTemplate();
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
