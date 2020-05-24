package it.polimi.ingsw.effects.move;

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
     * @param workerPosition the worker's Cell
     * @param board the board
     * @param turn the player's turn
     * @return a Set<Cell> collect that only has the cells where the player can move
     * without the initialPosition
     */

    @Override
    public Set<Position> move (Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);

        if (!checkMoveConditions(workerCell, turn)) return new HashSet<>();
        else {
            Position initialPosition = turn.getWorkerStartingPosition();

            if (initialPosition != null) {
                return super.move(workerPosition, board, turn)
                        .stream()
                        .filter(a -> !(a.getX()==initialPosition.getX()&& a.getY()==initialPosition.getY()))
                        .collect(Collectors.toSet());
            }
            //if the player didn't already move they can move normally
            else return super.move(workerPosition, board, turn);
        }
    }


    //The player needs to move two times in a row
    @Override
    protected boolean checkMoveConditions(Cell workerCell, Turn turn) {
        if (turn.isMoveBeforeBuild() && turn.isBuildAfterMove())
            return false;
        else
            return super.checkMoveConditions(workerCell, turn);
    }
}
