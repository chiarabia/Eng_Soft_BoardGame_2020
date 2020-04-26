package it.polimi.ingsw.server.serializable;

public class SerializableMove implements Serializable {
    private final int workerId;

    public int getWorkerId() {
        return workerId;
    }

    public SerializableMove(int workerId) {
        this.workerId = workerId;
    }
}
