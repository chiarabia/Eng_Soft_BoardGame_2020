package it.polimi.ingsw.Effects.Move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
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
    public Set<Cell> move(Cell workerCell, Board board, Turn turn) {
        Set<Cell> temp_cell = super.move(workerCell, board, turn);
        if(turn.getMove_times()>0 & !turn.isBuildAfterMove()) //in altri termini, se ho già costruito, ma non dopo essermi mosso, allora...
           return temp_cell.stream()
                    .filter(a-> heightsDifference(workerCell.getZ(),a.getZ())<=0)
                    .collect(Collectors.toSet());
        else
            return temp_cell;
    }

    @Override
    protected boolean checkMoveConditions(Cell workercell, Turn turn) {
        return super.checkMoveConditions(workercell, turn);
    }
}
