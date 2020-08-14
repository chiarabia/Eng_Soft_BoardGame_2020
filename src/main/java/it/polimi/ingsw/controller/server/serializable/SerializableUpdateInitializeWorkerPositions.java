package it.polimi.ingsw.controller.server.serializable;

import it.polimi.ingsw.model.Position;
import java.util.List;

public class SerializableUpdateInitializeWorkerPositions implements SerializableUpdate {
    private final List<Position> workerPositions;
    private final int playerId;

    public List<Position> getWorkerPositions() {
        return workerPositions;
    }

    public int getPlayerId() {
        return playerId;
    }

    public SerializableUpdateInitializeWorkerPositions(List<Position> workerPositions, int playerId) {
        this.workerPositions = workerPositions;
        this.playerId = playerId;
    }
}
