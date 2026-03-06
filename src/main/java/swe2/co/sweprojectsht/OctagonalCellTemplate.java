package swe2.co.sweprojectsht;

import javafx.scene.shape.Polygon;

class OctagonalCell extends Cell{
    private Player owner = Player.NONE;

    public OctagonalCell(int r, int c) {
        row = r;
        col = c;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player player) {
        owner = player;
    }
}
