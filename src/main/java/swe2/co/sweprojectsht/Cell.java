package swe2.co.sweprojectsht;

public abstract class Cell {
    protected String owner = "NONE"; // Options: "BLACK" - "WHITE" - "NONE"

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isUnoccupied() {
        return "NONE".equals(this.owner);
    }
}