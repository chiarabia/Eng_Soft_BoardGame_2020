package it.polimi.ingsw.server.serializable;

public class SerializableRequestOptionalMove implements SerializableRequest {
    private final int playerId;

    public SerializableRequestOptionalMove(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }
}
