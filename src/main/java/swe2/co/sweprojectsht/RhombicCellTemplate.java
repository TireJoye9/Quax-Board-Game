package swe2.co.sweprojectsht;

class RhombicCell extends Cell {
    private Player owner = Player.NONE;

    public RhombicCell(int r, int c) {
        row = r;
        col = c;
    }

    public void connect(OctagonalCell octagon) {
        addNeighbour(octagon);
        octagon.addNeighbour(this);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player p) {
        owner = p;
    }
}
