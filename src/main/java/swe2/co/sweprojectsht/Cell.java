package swe2.co.sweprojectsht;

import java.security.KeyStore;
import java.util.Objects;

public abstract class Cell {
    protected String owner = "NONE"; // Options: "BLACK" - "WHITE" - "NONE"

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isOcuupied() {

        if (Objects.equals(this.getOwner(), "NONE"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}