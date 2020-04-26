package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

public class SerializableUpdateBuild implements SerializableUpdate {
    private final boolean dome;
    private final Position newPosition;

    public boolean isDome() {return dome;}

    public Position getNewPosition () {return newPosition;}

    public SerializableUpdateBuild(Position newPosition, boolean dome) {
        this.dome = dome;
        this.newPosition = newPosition;
    }
}
