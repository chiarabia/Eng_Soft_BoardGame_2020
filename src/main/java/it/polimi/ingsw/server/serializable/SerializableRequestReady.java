package it.polimi.ingsw.server.serializable;

public class SerializableRequestReady implements SerializableRequest{
    private final int playerId;

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public SerializableRequestReady(int playerId) {
        this.playerId = playerId;
    }
}
