package it.polimi.ingsw.server.serializable;

public class SerializableRequestOptionalBuild implements SerializableRequest{
    private final int playerId;

    public SerializableRequestOptionalBuild(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }
}
