package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

public class SerializableConsolidateMove implements Serializable {
    private final Position newPosition;
    private final int workerId;

    public Position getNewPosition() {
        return newPosition;
    }

    public int getWorkerId() {
        return workerId;
    }

    public SerializableConsolidateMove(Position newPosition, int workerId) {
        this.newPosition = newPosition;
        this.workerId = workerId;
    }
}
