package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

public class SerializableUpdateMove implements SerializableUpdate {
    private final int playerId;
    private final int workerId;
    private final Position newPosition;

    public int getPlayerId() {return playerId;}
    public int getWorkerId () {return workerId;}
    public Position getNewPosition () {return newPosition;}

    public SerializableUpdateMove(Position newPosition, int playerId, int workerId){
            this.playerId = playerId;
            this.workerId = workerId;
            this.newPosition = newPosition;
    }
}
