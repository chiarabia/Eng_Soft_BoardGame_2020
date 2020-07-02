package it.polimi.ingsw.effects.move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * this class defines a movement into cells occupied by enemy worker.
 */

public class PushForward extends StandardMove{
    public PushForward(int moves) {
        super(moves);
    }

    /**
     *  Adds to the free cells to move in, cells with workers who have free cells behind them.
     * @param workerPosition the worker's Position
     * @param board the board
     * @param turn the player's turn
     * @return a <code>Set&lt;Cell&gt;</code> collect that only has the cells where the player can move to or
     * a <code>HashSet&lt;Cell&gt;</code> if the worker can't move
     *
     */

    @Override
    public Set<Position> move (Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        if (!checkMoveConditions(workerCell, turn))
            return new HashSet<Position>();
        else{
            return board
                .getStream()
                .filter(a -> a.isFree() || a.isWorker())
                .filter(a -> a.getX() <= workerCell.getX() + 1)
                .filter(a -> a.getX() >= workerCell.getX() - 1)
                .filter(a -> a.getY() <= workerCell.getY() + 1)
                .filter(a -> a.getY() >= workerCell.getY() - 1)
                .filter(a -> heightsDifference(workerCell.getZ(), a.getZ()) <= 1)
                .filter(a -> a.isFree()
                        || (a.isWorker()
                        && a.getPlayerId() != workerCell.getPlayerId()
                        && board.isFreeZone
                        (behindWorker(workerCell.getX(), a.getX()),
                                behindWorker(workerCell.getY(), a.getY()))))
                    .map(Cell::getPosition)
                .collect(Collectors.toSet());
        }
    }

    /**
     * Calculates the coordinates of the new Position of the worker.
     * @param myWorkerCoordinate x or y coordinate of my worker
     * @param opponentsWorkerCoordinate x or y coordinate of enemy worker.
     * @return x or y coordinate of the position behind enemy worker.
     */

    protected int behindWorker(int myWorkerCoordinate, int opponentsWorkerCoordinate) {
         if (myWorkerCoordinate == opponentsWorkerCoordinate) {
             return opponentsWorkerCoordinate;
         } else if (myWorkerCoordinate > opponentsWorkerCoordinate) {
             return opponentsWorkerCoordinate - 1;
         } else
             return opponentsWorkerCoordinate + 1;
     }
}
