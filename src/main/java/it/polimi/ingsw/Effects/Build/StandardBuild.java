package it.polimi.ingsw.Effects.Build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class StandardBuild {
    protected final int builds; //parametrizzo il metodo

    public StandardBuild(int builds) {
        this.builds = builds;
    }

    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        if (!canIbuild(workerCell, turn))
            return new HashSet<>();
        else
        return board.getStream()
                .filter(a -> a.isFree())
                .filter(a -> a.getX()<=workerCell.getX()+1)
                .filter(a -> a.getX()>=workerCell.getX()-1)
                .filter(a -> a.getY()<=workerCell.getY()+1)
                .filter(a -> a.getY()>=workerCell.getY()-1)
                .collect(Collectors.toSet());
    }

    protected boolean canIbuild (Cell workercell, Turn turn) {
        if (!workercell.isWorker()) //robusto, devo invocare il metodo su un worker
            return false;

        if (workercell.getPlayerID() != turn.getPlayer_id()) //il player deve essere uguale
            return false;

        if (turn.getMove_times()==0) // se non mi sono mosso;
            return false;

        if (workercell.getWorkerId() != turn.getWorkerused()) //if the id doesn't match, false
            return false;

        else {
            if (turn.isMovebeforebuild()) //se mi sono mosso, allora posso costruire
                return (turn.getBuild_times() < builds);
            else
                return false;
        }
    }
}
