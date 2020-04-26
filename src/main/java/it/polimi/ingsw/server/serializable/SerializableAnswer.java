package it.polimi.ingsw.server.serializable;

public interface SerializableAnswer extends java.io.Serializable {
    // message from server to one client
    int getPlayerId();
}
