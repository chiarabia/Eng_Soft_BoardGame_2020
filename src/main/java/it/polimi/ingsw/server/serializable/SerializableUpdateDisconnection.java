package it.polimi.ingsw.server.serializable;

public class SerializableUpdateDisconnection implements SerializableUpdate {
    private final int playerId;

    public int getPlayerId() { return playerId; }

    public SerializableUpdateDisconnection(int playerId) {
        this.playerId = playerId;
    }
}
