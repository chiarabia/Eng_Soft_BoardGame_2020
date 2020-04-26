package it.polimi.ingsw.server.serializable;

public class SerializableRequestBuild implements SerializableRequest{
    private final int playerId;

    public SerializableRequestBuild(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }
}
