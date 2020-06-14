package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

import java.util.Set;

public class SerializableRequestAction implements SerializableRequest{
    private final int playerId;
    private final boolean isMoveOptional; // true move facoltative
    private final boolean isBuildOptional; //build facoltative
    private final boolean canDecline; //true, il player si è mosso prima di costruire e ha costruito dopo essersi mosso
    //quindi per le nostre regole, può rifiutare una move/build opzionale e passare il turno
    private final Set<Position> worker1Moves;
    private final Set<Position> worker2Moves;
    private final Set<Position> worker1Builds;
    private final Set<Position> worker2Builds;
    private final boolean canForceDome; //True se il player può costruire una cupola ad ogni livello

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public boolean isMoveOptional() {
        return isMoveOptional;
    }

    public boolean isBuildOptional() {
        return isBuildOptional;
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

    public boolean isCanForceDome() {
        return canForceDome;
    }

    public boolean canDecline() {return canDecline;}

    public SerializableRequestAction(int playerId, boolean isMoveOptional, boolean isBuildOptional, boolean canDecline, Set<Position> worker1Moves, Set<Position> worker2Moves, Set<Position> worker1Builds, Set<Position> worker2Builds, boolean canForceDome) {
        this.playerId = playerId;
        this.canDecline = canDecline;
        this.isMoveOptional = isMoveOptional;
        this.isBuildOptional = isBuildOptional;
        this.worker1Moves = worker1Moves;
        this.worker2Moves = worker2Moves;
        this.worker1Builds = worker1Builds;
        this.worker2Builds = worker2Builds;
        this.canForceDome = canForceDome;
    }

    public boolean areBuildsEmpty () {
        return this.worker1Builds.size()==0 && this.worker2Builds.size()==0;
    }

    public boolean areMovesEmpty () {
        return this.worker1Moves.size()==0 && this.worker2Moves.size()==0;
    }

    public boolean canWorkerDoAction (int workerId) {
        if (workerId == 1)
            return (this.worker1Moves.size()!=0 || this.worker1Builds.size()!=0);
        else if (workerId == 2)
            return (this.worker2Moves.size()!=0 || this.worker2Builds.size()!=0);
        else return false;
    }
}
