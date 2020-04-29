package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

import java.util.Set;

public class SerializableRequestMove implements SerializableRequest {
    private final int playerId;
    private final Set<Position> worker1Moves;
    private final Set<Position> worker2Moves;

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public Set<Position> getWorker1Moves() {
        return worker1Moves;
    }

    public Set<Position> getWorker2Moves() {
        return worker2Moves;
    }

    public SerializableRequestMove(int playerId, Set<Position> worker1Moves, Set<Position> worker2Moves) {
        this.playerId = playerId;
        this.worker1Moves = worker1Moves;
        this.worker2Moves = worker2Moves;
    }
}
