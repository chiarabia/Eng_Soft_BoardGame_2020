package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;
import java.util.Set;
import java.util.stream.Collectors;

public class StandardBuild {
    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        final Set<Cell> collect = board.getStream()
                .filter(a -> a.isFree())
                .filter(a -> a.getX()<=workerCell.getX()+1)
                .filter(a -> a.getX()>=workerCell.getX()-1)
                .filter(a -> a.getY()<=workerCell.getY()+1)
                .filter(a -> a.getY()>=workerCell.getY()-1)
                .collect(Collectors.toSet());
        return collect;
    }
}
