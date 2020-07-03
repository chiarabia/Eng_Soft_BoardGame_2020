package it.polimi.ingsw.server.serializable;

public class SerializableUpdateTurn implements SerializableUpdate {
private final int playerId;
private final boolean isFirstTurn;

    public int getPlayerId() {
        return playerId;
    }

    public boolean getIsFirstTurn(){return isFirstTurn;}

    public SerializableUpdateTurn(int playerId, boolean isFirstTurn) {
        this.isFirstTurn = isFirstTurn;
        this.playerId = playerId;
    }
}
