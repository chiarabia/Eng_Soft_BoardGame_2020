package it.polimi.ingsw.model.effects.move;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Turn;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class implements a movement that cannot be a move up action
 * after the worker made a build action
 */


public class NoMoveUpAfterBuild extends StandardMove{
    public NoMoveUpAfterBuild(int moves) {
        super(moves);
    }


    /**Excludes positions placed at a higher height than the worker in the workerPosition, if the worker has already
     * built in this turn.
     * @param workerPosition the worker's Cell
     * @param board the board
     * @param turn the player's turn
     * @return a <code>Set&lt;Cell&gt;</code> collect that only has the cells where the player can move
     * without the initialPosition
     */
    @Override
    public Set<Position> move (Position workerPosition, Board board, Turn turn) {
        Set<Position> temp_positions = super.move(workerPosition, board, turn);
        if(turn.getBuildTimes()>0 & !turn.isBuildAfterMove()) // if the player has built before the first move...
           return temp_positions.stream()
                    .filter(a-> heightsDifference(workerPosition.getZ(),a.getZ())<=0)
                    .collect(Collectors.toSet());
        else
            return temp_positions;
    }

    @Override
    protected boolean checkMoveConditions(Cell workerCell, Turn turn) {
            return super.checkMoveConditions(workerCell, turn);
    }
}
