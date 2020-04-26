package it.polimi.ingsw.server.serializable;

public class SerializableUpdateTurn implements SerializableUpdate {
private final int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public SerializableUpdateTurn(int playerId) {
        this.playerId = playerId;
    }
}
