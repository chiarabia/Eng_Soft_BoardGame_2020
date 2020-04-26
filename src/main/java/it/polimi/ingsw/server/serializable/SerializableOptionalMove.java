package it.polimi.ingsw.server.serializable;

public class SerializableOptionalMove implements Serializable {
    private final boolean wantToMove;

    public boolean isWantToMove() {
        return wantToMove;
    }

    public SerializableOptionalMove(boolean wantToMove) {
        this.wantToMove = wantToMove;
    }
}
