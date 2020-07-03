package it.polimi.ingsw.controller.server.serializable;

/** This interface defines a Serializable object from server to one client*/
public interface SerializableRequest extends java.io.Serializable {
    int getPlayerId();
}
