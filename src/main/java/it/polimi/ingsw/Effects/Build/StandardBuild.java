package it.polimi.ingsw.Effects.Build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;
import java.util.Set;
import java.util.stream.Collectors;

public class StandardBuild {
    protected final int builds;

    public StandardBuild(int builds) {
        this.builds = builds;
    }

    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        return board.getStream()
                .filter(a -> a.isFree())
                .filter(a -> a.getX()<=workerCell.getX()+1)
                .filter(a -> a.getX()>=workerCell.getX()-1)
                .filter(a -> a.getY()<=workerCell.getY()+1)
                .filter(a -> a.getY()>=workerCell.getY()-1)
                .collect(Collectors.toSet());
    }


    protected boolean canIbuild (Cell workerCell, Turn turn) {

    }
}
