package it.polimi.ingsw.effects.move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class defines a movement that cannot be a move up action
 * after the worker made a build action
 */


public class NoMoveUpAfterBuild extends StandardMove{
    public NoMoveUpAfterBuild(int moves) {
        super(moves);
    }

    @Override
    public Set<Position> move (Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        Set<Position> temp_positions = super.move(workerPosition, board, turn);
        if(turn.getMoveTimes()>0 & !turn.isBuildAfterMove()) //in altri termini, se ho già costruito, ma non dopo essermi mosso, allora...
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
