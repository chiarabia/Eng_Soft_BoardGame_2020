package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;
import java.util.List;

public class SerializableUpdateInitializeWorkers implements SerializableUpdate {
    private final List<Position> workerPositions;
    private final int playerId;

    public List<Position> getWorkerPositions() {
        return workerPositions;
    }

    public int getPlayerId() {
        return playerId;
    }

    public SerializableUpdateInitializeWorkers(List<Position> workerPositions, int playerId) {
        this.workerPositions = workerPositions;
        this.playerId = playerId;
    }
}
