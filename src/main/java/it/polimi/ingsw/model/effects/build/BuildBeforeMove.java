package it.polimi.ingsw.model.effects.build;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Turn;

import java.util.Set;

/**
 * This class implements whether a player can build before moving.
 */

public class BuildBeforeMove extends StandardBuild {

    public BuildBeforeMove(int builds) {
        super(builds);
    }

    @Override
    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        return super.build(workerPosition, board, turn);
    }


    /**
     * Determines whether the chosen worker can (or still can) build this turn.
     * Unlike the parent class, this method also considers the possibility that a build may occur before a move.
     * The number of builds refers exclusively to the number of standard builds made (ie after moving).
     * So a player with builds = 1 can build before moving and after moving.
     * @param workerCell the worker's Cell
     * @param turn the player's turn
     * @return returns true only and only if the worker considered: is a worker of the current player,
     * has already moved and has not already built <Code>"builds"</Code> times this turn.
     */

    @Override
    protected boolean checkBuildConditions(Cell workerCell, Turn turn) {
        if (!workerCell.isWorker())
            return false;

        if (workerCell.getPlayerId() != turn.getPlayerId()) //the player must be the same
            return false;

        if (turn.getWorkerUsed() != 0 && workerCell.getWorkerId() != turn.getWorkerUsed()) //if the id doesn't match, false
            return false;

        if (!turn.isMoveBeforeBuild() && turn.getBuildTimes() == 0) //The player can build before move
            return true;

        if(!turn.isMoveBeforeBuild() && turn.isBuildBeforeMove())
            return false;

        //the number "builds" refers to the number of normal builds the player can do after move.
        //The build before move is not counted in builds
        if (turn.isBuildBeforeMove()) //this is the case where the build before move has been done
            return (turn.getBuildTimes()< (builds+1));
        else
            return (turn.getBuildTimes() < builds); //the player did not build before move
    }


}
