package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

public class SerializableConsolidateMove implements java.io.Serializable {
    private Position newPosition;
    private int workerId;

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
