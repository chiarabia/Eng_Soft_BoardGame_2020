package it.polimi.ingsw.server.serializable;

public class SerializableRequestInitializeWorkers implements SerializableRequest {
    private final int playerId;

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public SerializableRequestInitializeWorkers(int playerId) {
        this.playerId = playerId;
    }
}
