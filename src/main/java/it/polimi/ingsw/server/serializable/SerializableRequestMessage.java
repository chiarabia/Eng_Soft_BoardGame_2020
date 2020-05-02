package it.polimi.ingsw.server.serializable;

public class SerializableRequestMessage implements SerializableRequest{
    private final int playerId;
    private final String message;
    @Override
    public int getPlayerId() {
        return playerId;
    }

    public String getMessage() {
        return message;
    }

    public SerializableRequestMessage(int playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }
}
