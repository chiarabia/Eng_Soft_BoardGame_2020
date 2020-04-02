package it.polimi.ingsw.Effects.Build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class defines the possibility to build before moving
 */

public class BuildBeforeMove extends StandardBuild {

    public BuildBeforeMove(int builds) {
        super(builds);
    }

    //TODO: Non sono sicuro che chiamare la classe padre, mi permetta di usare il metodo canIbuild con Override, bisognerebbe fare testing
    @Override
    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        return super.build(workerCell, board, turn);
    }


    /**
     *
     * @param workercell
     * @param turn
     * @return
     */

    @Override
    protected boolean checkBuildConditions(Cell workercell, Turn turn) {
        if (!workercell.isWorker()) //robusto, devo invocare il metodo su un worker
            return false;

        if (workercell.getPlayerID() != turn.getPlayer_id()) //il player deve essere uguale
            return false;

        //Posso costruire prima di muovermi

        if (workercell.getWorkerId() != turn.getWorkerUsed()) //if the id doesn't match, false
            return false;

        else {
            if (turn.isMoveBeforeBuild()||turn.getMove_times()==0) //se mi sono mosso, allora posso costruire OPPURE, posso costruire se non mi sono ancora mosso)
                return (turn.getBuild_times() < builds);
            else
                return false;
        }
    }
}
