package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

import java.util.Set;

public class SerializableRequestBuild implements SerializableRequest {
    private final int playerId;
    private final Set<Position> worker1Builds;
    private final Set<Position> worker2Builds;
    private final boolean canForceMove;

    @Override
    public int getPlayerId() {
        return playerId;
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

    public SerializableRequestBuild(int playerId, Set<Position> worker1Builds, Set<Position> worker2Builds, boolean canForceMove) {
        this.playerId = playerId;
        this.worker1Builds = worker1Builds;
        this.worker2Builds = worker2Builds;
        this.canForceMove = canForceMove;
    }
}
