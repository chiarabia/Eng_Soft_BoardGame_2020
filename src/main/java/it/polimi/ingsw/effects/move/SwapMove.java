package it.polimi.ingsw.effects.move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SwapMove extends StandardMove {
    public SwapMove(int moves) {
        super(moves);
    }

    @Override
    public Set<Position> move (Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        if (!checkMoveConditions(workerCell, turn)) return new HashSet<Position>();

        else {
            final Set<Position> collect = board.getStream()
                    .filter(a -> a.isFree() || (a.isWorker()&&!(a.getPlayerId() == workerCell.getPlayerId())))
                            // toglie l'altro worker dall'insieme di caselle disponibili
                    .filter(a -> !a.equals(workerCell))
                    .filter(a -> a.getX() <= workerCell.getX() + 1)
                    .filter(a -> a.getX() >= workerCell.getX() - 1)
                    .filter(a -> a.getY() <= workerCell.getY() + 1)
                    .filter(a -> a.getY() >= workerCell.getY() - 1)
                    .filter(a -> heightsDifference(workerCell.getZ(), a.getZ()) <= 1)
                    .map(Cell::getPosition)
                    .collect(Collectors.toSet());
            return collect;
        }
    }
}
