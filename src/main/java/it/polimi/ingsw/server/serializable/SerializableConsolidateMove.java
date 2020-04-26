package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

public class SerializableConsolidateMove implements Serializable {
    private final Position newPosition;
    private final int workerId;

    public Position getNewPosition() {
        return newPosition;
    }

    public int getNumOfWorker() {
        return workerId;
    }

    public SerializableConsolidateMove(Position newPosition, int numOfWorker) {
        this.newPosition = newPosition;
        this.workerId = numOfWorker;
    }
}
