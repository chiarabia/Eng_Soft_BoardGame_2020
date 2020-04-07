package it.polimi.ingsw.Effects.Move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class defines the general rules of movement
 */

public class StandardMove {
    protected final int moves;

    /**
     * The general move method
     * @param workerCell the worker's Cell
     * @param board the board
     * @param turn the player's turn
     * @return a Set<Cell> collect that only has the cells where the player can move to or
     * a HashSet<Cell> if the worker can't move
     *
     */
    public Set<Cell> move (Cell workerCell, Board board, Turn turn) {
        if (!checkMoveConditions(workerCell, turn)) {
            return new HashSet<>();
        }
        final Set<Cell> collect = board.getStream()
                //only free Cells
                .filter(Cell::isFree)
                //only the Cells near the worker
                .filter(a -> a.getX() <= workerCell.getX() + 1)
                .filter(a -> a.getX() >= workerCell.getX() - 1)
                .filter(a -> a.getY() <= workerCell.getY() + 1)
                .filter(a -> a.getY() >= workerCell.getY() - 1)
                //the worker can move up only by one level, but can move down without limitations
                .filter(a -> heightsDifference(workerCell.getZ(), a.getZ()) <= 1)
                .collect(Collectors.toSet());
        return collect;
    }

    /**
     * It's a protected method so that it can be used in classes that
     * extend this one.
     *
     * @param z_worker the position of the worker in the z axis
     * @param z_cell the position of the cell in the z axis
     * @returnthe difference in the Z axis between the possible destination Cells and the worker
     */
    protected int heightsDifference (int z_worker, int z_cell)  {
        final int i = z_cell - z_worker;
        return i;
    }

    /**
     * This method determines if the worker can move
     *
     * @param workerCell the worker's Cell
     * @param turn the player's turn
     * @return false if the the player cannot move its workers,
     */

    protected boolean checkMoveConditions(Cell workerCell, Turn turn) {
        //the Cell needs to have a worker
        if (!workerCell.isWorker())
            return false;

        //the player id of this turn must be the same of the worker
        if (workerCell.getPlayerID() != turn.getPlayerId())
            return false;

        //if no worker has been moved yet, all workers can be moved
        if (turn.getWorkerUsed() == 0)
            return (turn.getMoveTimes() < moves);

        if (workerCell.getWorkerId() != turn.getWorkerUsed()) //if the id doesn't match, false
            return false;

        else
            return (turn.getMoveTimes() < moves);
    }

    public StandardMove(int moves) {
        this.moves = moves;
    }
}

