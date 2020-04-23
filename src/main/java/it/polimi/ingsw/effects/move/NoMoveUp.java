package it.polimi.ingsw.effects.move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class defines a movement that cannot be a move up action
 */


public class NoMoveUp extends StandardMove {
    private StandardMove decoratedMove;

    public NoMoveUp (StandardMove decoratedMove){
        super(decoratedMove.moves);
        this.decoratedMove = decoratedMove;
    }

    @Override
    public Set<Position> move (Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        if (!checkMoveConditions(workerCell, turn)) return new HashSet<Position>();
        else {
            Set<Position> standardMove = decoratedMove.move(workerPosition, board, turn);
            return standardMove.stream()
                    .filter(a -> a.getZ() <= workerCell.getZ())
                    .collect(Collectors.toSet());
        }
    }

    @Override
    protected int heightsDifference(int z_worker, int z_cell) {
        return decoratedMove.heightsDifference(z_worker, z_cell);
    }
}

