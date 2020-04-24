package it.polimi.ingsw.server.serializable;

public class SerializableBuild implements java.io.Serializable {
    private int workerId;

    public int getWorkerId() {
        return workerId;
    }

    public SerializableBuild(int workerId) {
        this.workerId = workerId;
    }
}
