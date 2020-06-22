package it.polimi.ingsw;

import java.util.Objects;

/**
 * This class sets the coordinates for the Positions on the board
 */

public class Position implements java.io.Serializable {
    private final int x;
    private final int y;
    private final int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Position mirrorYCoordinate() {
        return new Position(this.getX(),
                (this.getY()-4)<0 ? -(this.getY()-4) : (this.getY()-4),
                this.getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return getX() == position.getX() &&
                getY() == position.getY() &&
                getZ() == position.getZ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }
}
