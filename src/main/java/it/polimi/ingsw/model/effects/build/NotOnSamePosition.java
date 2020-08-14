package it.polimi.ingsw.model.effects.build;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class modifies the standard building rule
 * All subsequent constructions cannot be built above the first construction.
 */

public class NotOnSamePosition extends StandardBuild{
    public NotOnSamePosition(int builds) {
        super(builds);
    }

    @Override
    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);

        /**
         * Calls the method build of the father class if the worker has not yet built.
         * Otherwise, excludes all positions with the same X and Y as the first construction of the turn.
         * @param workerPosition the worker's Position
         * @param board the board
         * @param turn the player's turn
         * @return a <code>Set&lt;Cell&gt;</code> collect that only has the cells where the player can build in
         */
        if (!checkBuildConditions(workerCell, turn))
            return new HashSet<>();
        else
            if(turn.getBuildTimes()>0) {
                return board.getStream()
                        .filter(a -> a.isFree())
                        .filter(a -> !a.getPosition().equals(turn.getFirstBuildingPosition()))
                        .filter(a -> !(a.getX()==turn.getFirstBuildingPosition().getX()&&
                                a.getY()==turn.getFirstBuildingPosition().getY()))
                        .filter(a -> a.getX() <= workerCell.getX() + 1)
                        .filter(a -> a.getX() >= workerCell.getX() - 1)
                        .filter(a -> a.getY() <= workerCell.getY() + 1)
                        .filter(a -> a.getY() >= workerCell.getY() - 1)
                        .map(a->a.getPosition())
                        .collect(Collectors.toSet());
            }
            else
                return super.build(workerPosition, board, turn);
            }
    }
