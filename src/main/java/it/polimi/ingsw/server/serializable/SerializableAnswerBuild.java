package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;
import java.util.Set;

public class SerializableAnswerBuild implements SerializableAnswer {
    private final int playerId;
    private final Set<Position> builds;
    private final boolean canForceDome;

    public int getPlayerId() {
        return playerId;
    }

    public Set<Position> getBuilds() {
        return builds;
    }

    public boolean isCanForceDome() { return canForceDome; }

    public SerializableAnswerBuild(int playerId, Set<Position> builds, boolean canForceDome) {
        this.playerId = playerId;
        this.builds = builds;
        this.canForceDome = canForceDome;
    }
}
