package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class defines
 */


public class OnSamePositionBlockOnly extends StandardBuild {
    public OnSamePositionBlockOnly(int builds) {
        super(builds);
    }

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
