package it.polimi.ingsw.controller.server.serializable;

import it.polimi.ingsw.model.Position;

public class SerializableUpdateMove implements SerializableUpdate {
    private final int playerId;
    private final int workerId;
    private final Position startingPosition;
    private final Position newPosition;

    public int getPlayerId() {return playerId;}
    public int getWorkerId () {return workerId;}
    public Position getNewPosition () {return newPosition;}
    public Position getStartingPosition() {
        return startingPosition;
    }

    public SerializableUpdateMove(Position startingPosition, Position newPosition, int playerId, int workerId){
            this.playerId = playerId;
            this.workerId = workerId;
            this.startingPosition = startingPosition;
            this.newPosition = newPosition;
    }
}
