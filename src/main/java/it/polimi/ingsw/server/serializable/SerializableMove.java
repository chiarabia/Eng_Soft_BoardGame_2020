package it.polimi.ingsw.server.serializable;

public class SerializableMove implements java.io.Serializable {
    private int workerId;

    public int getWorkerId() {
        return workerId;
    }

    public SerializableMove(int workerId) {
        this.workerId = workerId;
    }
}
