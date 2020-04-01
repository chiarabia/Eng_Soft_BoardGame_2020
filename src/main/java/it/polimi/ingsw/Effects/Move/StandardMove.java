package it.polimi.ingsw.Effects.Move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This classe defines the general rules of movement
 */

public class StandardMove {
    protected final int moves;

    /**
     * The general move method
     * @param workerCell the worker's Cell
     * @param board the board
     * @param turn the player's turn
     * @return a Set<Cell> collect that only has the cells where the player can move to
     */
    public Set<Cell> move (Cell workerCell, Board board, Turn turn) {
        if (!canImove(workerCell, turn)) {
            return new HashSet<Cell>();
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

    protected boolean canImove (Cell workercell, Turn turn) {
        if (!workercell.isWorker()) //robusto, devo invocare il metodo su un worker
            return false;

        if (workercell.getPlayerID() != turn.getPlayer_id()) //il player deve essere uguale
            return false;

        if (turn.getWorkerused() == 0) //nessun worker è stato mosso, tutti i worker devorebbero potersi muovere
            return (turn.getMove_times() < moves);

        if (workercell.getWorkerId() != turn.getWorkerused()) //if the id doesn't match, false
            return false;

        else
            return (turn.getMove_times() < moves);
    }

    public StandardMove(int moves) {
        this.moves = moves;
    }
}

