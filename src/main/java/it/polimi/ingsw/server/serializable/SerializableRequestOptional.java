package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

import java.util.Set;

public class SerializableRequestOptional implements SerializableRequest{
    private final int playerId;
    private final boolean isMoveOptional; // se è false allora sono le builds a essere facoltative
    private final Set<Position> worker1Moves;
    private final Set<Position> worker2Moves;
    private final Set<Position> worker1Builds;
    private final Set<Position> worker2Builds;
    private final boolean canForceMove;

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public boolean isMoveOptional() {
        return isMoveOptional;
    }

    public Set<Position> getWorker1Moves() {
        return worker1Moves;
    }

    public Set<Position> getWorker2Moves() {
        return worker2Moves;
    }

    public Set<Position> getWorker1Builds() {
        return worker1Builds;
    }

    public Set<Position> getWorker2Builds() {
        return worker2Builds;
    }

    public boolean isCanForceMove() {
        return canForceMove;
    }

    public SerializableRequestOptional(int playerId, boolean isMoveOptional, Set<Position> worker1Moves, Set<Position> worker2Moves, Set<Position> worker1Builds, Set<Position> worker2Builds, boolean canForceMove) {
        this.playerId = playerId;
        this.isMoveOptional = isMoveOptional;
        this.worker1Moves = worker1Moves;
        this.worker2Moves = worker2Moves;
        this.worker1Builds = worker1Builds;
        this.worker2Builds = worker2Builds;
        this.canForceMove = canForceMove;
    }
}
