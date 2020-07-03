package it.polimi.ingsw.model.effects.build;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class defines the general rules of building.
 * Builds defines the number of buildings the player can make during his turn.
 */

public class StandardBuild {
    protected final int builds;

    public StandardBuild(int builds) {
        this.builds = builds;
    }

    /**
     * The general build method
     * @param workerPosition the worker's Position
     * @param board the board
     * @param turn the player's turn
     * @return a <code>Set&lt;Cell&gt;</code> collect that only has the cells where the player can build in
     */

    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        if (!checkBuildConditions(workerCell, turn))
            return new HashSet<>();
        else
        return board.getStream()
                .filter(a -> a.isFree())
                .filter(a -> a.getX()<=workerCell.getX()+1)
                .filter(a -> a.getX()>=workerCell.getX()-1)
                .filter(a -> a.getY()<=workerCell.getY()+1)
                .filter(a -> a.getY()>=workerCell.getY()-1)
                .map(a->a.getPosition())
                .collect(Collectors.toSet());
    }

    /**
     * Determines whether the chosen worker can (or still can) build this turn.
     *
     * @param workerCell the worker's Cell
     * @param turn the player's turn
     * @return returns true only and only if the worker considered: is a worker of the current player,
     * has already moved and has not already built <Code>"builds"</Code> times this turn.
     */

    protected boolean checkBuildConditions(Cell workerCell, Turn turn) {
        //the Cell needs to have a worker
        if (!workerCell.isWorker())
            return false;

        //the player id of this turn must be the same of the worker
        if (workerCell.getPlayerId() != turn.getPlayerId())
            return false;

        //if the player has moved a worker yet
        if (!turn.isMoveBeforeBuild())
            return false;

        //if the id of the worker is not the same of the id of the worker used in the turn
        if (workerCell.getWorkerId() != turn.getWorkerUsed())
            return false;

        else
            return (turn.getBuildTimes() < builds);
    }
}
