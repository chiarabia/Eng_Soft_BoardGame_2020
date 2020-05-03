package it.polimi.ingsw.server.serializable;

public class SerializableMessage implements Serializable{
    private final String message;

    public String getMessage() {
        return message;
    }

    public SerializableMessage(String message) {
        this.message = message;
    }
}
