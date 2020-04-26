package it.polimi.ingsw.server.serializable;

public class SerializableUpdateWinner implements SerializableUpdate {
    private final int playerId;

    public int getPlayerId() { return playerId; }

    public SerializableUpdateWinner(int playerId) {
        this.playerId = playerId;
    }
}
