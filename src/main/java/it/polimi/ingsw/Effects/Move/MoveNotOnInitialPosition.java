package it.polimi.ingsw.Effects.Move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * This class defines a move where the worker can't move on the original
 * position
 */

public class MoveNotOnInitialPosition extends StandardMove{
    public MoveNotOnInitialPosition(int moves) {
        super(moves);
    }

    /**
     * @param workerCell the worker's Cell
     * @param board the board
     * @param turn the player's turn
     * @return a Set<Cell> collect that only has the cells where the player can move
     * without the initialPosition
     */

    @Override
    public Set<Cell> move(Cell workerCell, Board board, Turn turn) {
        if (!checkMoveConditions(workerCell, turn)) return new HashSet<>();
        else {
            Position initialPosition = turn.getWorkerStartingPosition();
            if (initialPosition != null) {
                return super.move(workerCell, board, turn)
                        .stream()
                        .filter(a -> !a.getCellPosition().equals(initialPosition))
                        .collect(Collectors.toSet());
            }
            //if the player didn't already move they can move normally
            else return super.move(workerCell, board, turn);
        }
    }


    //The player needs to move two times in a row
    @Override
    protected boolean checkMoveConditions(Cell workerCell, Turn turn) {
        if (turn.getMoveTimes() != 0) return false;
        else {
            return super.checkMoveConditions(workerCell, turn);
        }
    }
}
