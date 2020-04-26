package it.polimi.ingsw.server.serializable;

public class SerializableUpdateLoser implements SerializableUpdate {
    private final int playerId;
    private final int nextTurnPlyerId; // utile quando su tre giocatori rimasti uno perde

    public int getPlayerId() {
        return playerId;
    }

    public int getNextTurnPlyerId() {
        return nextTurnPlyerId;
    }

    public SerializableUpdateLoser(int playerId, int nextTurnPlyerId) {
        this.playerId = playerId;
        this.nextTurnPlyerId = nextTurnPlyerId;
    }
}
