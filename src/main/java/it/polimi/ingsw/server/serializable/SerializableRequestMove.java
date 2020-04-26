package it.polimi.ingsw.server.serializable;

public class SerializableRequestMove implements SerializableRequest {
    private final int playerId;

    public SerializableRequestMove(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }
}
