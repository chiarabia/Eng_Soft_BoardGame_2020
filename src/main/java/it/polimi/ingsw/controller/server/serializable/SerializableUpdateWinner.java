package it.polimi.ingsw.controller.server.serializable;

public class SerializableUpdateWinner implements SerializableUpdate {
    private final int playerId;

    public int getPlayerId() { return playerId; }

    public SerializableUpdateWinner(int playerId) {
        this.playerId = playerId;
    }
}
