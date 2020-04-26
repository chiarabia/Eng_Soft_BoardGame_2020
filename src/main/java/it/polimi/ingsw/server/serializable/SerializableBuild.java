package it.polimi.ingsw.server.serializable;

public class SerializableBuild implements Serializable {
    private final int workerId;

    public int getWorkerId() {
        return workerId;
    }

    public SerializableBuild(int workerId) {
        this.workerId = workerId;
    }
}
