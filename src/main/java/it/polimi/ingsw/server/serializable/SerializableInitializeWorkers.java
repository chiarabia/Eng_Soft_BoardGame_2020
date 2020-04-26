package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

import java.util.List;

public class SerializableInitializeWorkers implements Serializable {
    private final List<Position> workerPositions;

    public List<Position> getWorkerPositions() {
        return workerPositions;
    }

    public SerializableInitializeWorkers(List<Position> workerPositions) {
        this.workerPositions = workerPositions;
    }
}
