package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

public class SerializableConsolidateBuild implements Serializable {
    private final Position newPosition;
    private final int workerId;
    private final boolean forceDome;

    public Position getNewPosition() {
            return newPosition;
        }
    public int getWorkerId() { return workerId; }
    public boolean isForceDome() { return forceDome; }

    public SerializableConsolidateBuild(Position newPosition, int workerId, boolean forceDome) {
        this.newPosition = newPosition;
        this.workerId = workerId;
        this.forceDome = forceDome;
    }
}
