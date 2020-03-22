package it.polimi.ingsw.Effects;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Board;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StandardBuild {
    public Set<Cell> move (Cell workerCell, Board board) {
        return  board.getStream()
                .filter(Cell::isFree)
                .filter(a -> a.getCellPosition().getX()<=workerCell.getCellPosition().getX()+1)
                .filter(a -> a.getCellPosition().getX()>=workerCell.getCellPosition().getX()-1)
                .filter(a -> a.getCellPosition().getY()<=workerCell.getCellPosition().getX()+1)
                .filter(a -> a.getCellPosition().getY()>=workerCell.getCellPosition().getX()-1)
                .filter( a -> diffheight(workerCell.getZ(), a.getZ()) <= 1)
                .collect(Collectors.toSet());
    }

    int diffheight (int z_worker, int z_cell)  {
        final int i = z_cell - z_worker;
        return i;
    }
}

