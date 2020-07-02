package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class modifies the standard building rule
 * All subsequent constructions must be built above the first construction.
 */


public class OnSamePositionBlockOnly extends StandardBuild {
    public OnSamePositionBlockOnly(int builds) {
        super(builds);
    }

    /**
     * Calls the method build of father class if the worker has not yet built.
     * Otherwise, filters all positions with the same X and Y as the first construction of the turn.
     * @param workerPosition the worker's Position
     * @param board the board
     * @param turn the player's turn
     * @return a <code>Set&lt;Cell&gt;</code> collect that only has the cells where the player can build in
     */
    @Override
    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        Position first_block = turn.getFirstBuildingPosition();
        Cell workerCell = board.getCell(workerPosition);

        if (turn.getBuildTimes()>0) {
            if (!checkBuildConditions(workerCell, turn))
                return new HashSet<>();
            else
                return board.getStream()
                        .filter(a -> a.isFree())
                        .filter(a-> a.getX() == (first_block.getX()))
                        .filter(a-> a.getY() == (first_block.getY()))
                        .filter(a->a.getZ() <= 2)
                        .map(a->a.getPosition())
                        .collect(Collectors.toSet());
        }
        else
            return super.build(workerPosition, board, turn);
    }
}
