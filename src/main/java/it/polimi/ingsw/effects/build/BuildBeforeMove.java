package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.Set;

/**
 * This class defines the possibility to build before moving
 */

public class BuildBeforeMove extends StandardBuild {

    public BuildBeforeMove(int builds) {
        super(builds);
    }

    @Override
    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        return super.build(workerPosition, board, turn);
    }

    @Override
    protected boolean checkBuildConditions(Cell workerCell, Turn turn) {
        if (!workerCell.isWorker()) //robusto, devo invocare il metodo su un worker
            return false;

        if (workerCell.getPlayerId() != turn.getPlayerId()) //the player must be the same
            return false;

        if (turn.getWorkerUsed() != 0 && workerCell.getWorkerId() != turn.getWorkerUsed()) //if the id doesn't match, false
            return false;

        if (!turn.isMoveBeforeBuild() && turn.getMoveTimes() == 0) //The player can build before move
            return true;

        //the number "builds" refers to the number of normal builds the player can do after move.
        //The build before move is not counted in builds
        if (turn.isBuildBeforeMove()) //this is the case where the build before move has been done
            return (turn.getBuildTimes() < (builds + 1));
        else
            return (turn.getBuildTimes() < builds); //the player did not build before move
    }
}
