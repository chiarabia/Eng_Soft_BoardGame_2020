package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

import java.util.List;

public class SerializableRequestInitializeWorkerPositions implements SerializableRequest {
    private final int playerId;
    private final List <Position> possiblePositions;

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public List<Position> getPossiblePositions() { return possiblePositions; }

    public SerializableRequestInitializeWorkerPositions(List<Position> possiblePositions, int playerId) {
        this.possiblePositions = possiblePositions;
        this.playerId = playerId;
    }
}
