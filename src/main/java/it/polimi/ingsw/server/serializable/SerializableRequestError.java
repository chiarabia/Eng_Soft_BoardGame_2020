package it.polimi.ingsw.server.serializable;

public class SerializableRequestError implements SerializableRequest {
    private final int playerId;
    private final String message;

    public int getPlayerId() {
        return playerId;
    }

    public String getMessage(){
        return message;
    }

    public SerializableRequestError(int playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }
}
