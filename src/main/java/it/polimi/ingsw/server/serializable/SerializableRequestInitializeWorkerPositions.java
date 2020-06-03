package it.polimi.ingsw.server.serializable;

import java.util.List;

public class SerializableRequestInitializeWorkerPositions implements SerializableRequest {
    private final int playerId;

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public SerializableRequestInitializeWorkerPositions(int playerId) {
        this.playerId = playerId;
    }
}
