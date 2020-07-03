package it.polimi.ingsw.model.effects.move;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * this class implements a movement into cells occupied by an enemy worker.
 */
public class SwapMove extends StandardMove {
    public SwapMove(int moves) {
        super(moves);
    }


    /**
     *  Adds to the possible cells to move into the  cells with an enemy workers.
     * @param workerPosition the worker's Position
     * @param board the board
     * @param turn the player's turn
     * @return a <code>Set&lt;Cell&gt;</code> collect that only has the cells where the player can move to or
     * a <code>HashSet&lt;Cell&gt;</code> if the worker can't move
     */
    @Override
    public Set<Position> move (Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        if (!checkMoveConditions(workerCell, turn)) return new HashSet<>();
        else {
            return board.getStream()
                    .filter(a -> a.isFree() || (a.isWorker()&&!(a.getPlayerId() == workerCell.getPlayerId())))
                    .filter(a -> !a.equals(workerCell))
                    .filter(a -> a.getX() <= workerCell.getX() + 1)
                    .filter(a -> a.getX() >= workerCell.getX() - 1)
                    .filter(a -> a.getY() <= workerCell.getY() + 1)
                    .filter(a -> a.getY() >= workerCell.getY() - 1)
                    .filter(a -> heightsDifference(workerCell.getZ(), a.getZ()) <= 1)
                    .map(Cell::getPosition)
                    .collect(Collectors.toSet());
        }
    }
}
