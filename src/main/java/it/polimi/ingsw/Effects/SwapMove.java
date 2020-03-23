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
                //TODO: togliere l'altro worker dall'insieme di caselle disponibili
                .filter(a -> !a.equals(workerCell))
                .filter(a -> a.getX()<=workerCell.getX()+1)
                .filter(a -> a.getX()>=workerCell.getX()-1)
                .filter(a -> a.getY()<=workerCell.getX()+1)
                .filter(a -> a.getY()>=workerCell.getX()-1)
                .filter(a -> heightsdifference(workerCell.getZ(), a.getZ()) <= 1)
                .collect(Collectors.toSet());
        return collect;
    }
}
