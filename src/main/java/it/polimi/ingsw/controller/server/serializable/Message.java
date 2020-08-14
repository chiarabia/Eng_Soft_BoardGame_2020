package it.polimi.ingsw.controller.server.serializable;

public class Message implements java.io.Serializable{
    private final String message;

    public String getMessage() {
        return message;
    }

    public Message(String message) {
        this.message = message;
    }
}
