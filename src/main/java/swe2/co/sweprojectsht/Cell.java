package swe2.co.sweprojectsht;

import java.util.ArrayList;
import java.util.List;

public abstract class Cell {
    protected int row;
    protected int col;

    protected List<Cell> neighbours = new ArrayList<>();

    public void addNeighbour(Cell cell) {
        neighbours.add(cell);
    }

    public List<Cell> getNeighbours() {
        return neighbours;
    }
}