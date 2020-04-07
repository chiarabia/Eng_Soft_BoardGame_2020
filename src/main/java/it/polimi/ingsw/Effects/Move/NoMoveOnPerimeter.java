package it.polimi.ingsw.Effects.Move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.Set;

/**
 * This class defines a movement action that cannot end on a cell on the
 * perimeter of the board
 */


public class NoMoveOnPerimeter extends StandardMove {
    public NoMoveOnPerimeter(int moves) { //per convenzione metterei 1, tanto basta che possa muoversi almeno una volta il player per non perdere il turno
        super(moves);
    }

    @Override
    public Set<Cell> move(Cell workerCell, Board board, Turn turn) {
        return super.move(workerCell, board, turn);
    }

    @Override
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

        else {
            //the
            if (workerCell.isPerimetral())
                return true;
            else
                return (turn.getMoveTimes() < moves);
        }
    }
}
