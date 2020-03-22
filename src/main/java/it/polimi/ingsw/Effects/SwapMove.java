package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;

import java.util.Set;
import java.util.stream.Collectors;

public class SwapMove extends StandardBuild {
    @Override
    public Set<Cell> move(Cell workerCell, Board board) {
        final Set<Cell> collect = board.getStream()
                .filter(a -> a.isFree() || a.isWorker())
                .filter(a -> !a.equals(workerCell))
                .filter(a -> a.getCellPosition().getX() <= workerCell.getCellPosition().getX() + 1)
                .filter(a -> a.getCellPosition().getX() >= workerCell.getCellPosition().getX() - 1)
                .filter(a -> a.getCellPosition().getY() <= workerCell.getCellPosition().getX() + 1)
                .filter(a -> a.getCellPosition().getY() >= workerCell.getCellPosition().getX() - 1)
                .filter(a -> diffheight(workerCell.getZ(), a.getZ()) <= 1)
                .collect(Collectors.toSet());
        return collect;
    }
}
