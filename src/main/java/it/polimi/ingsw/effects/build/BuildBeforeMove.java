package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.Set;

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

        if (workercell.getPlayerId() != turn.getPlayerId()) //il player deve essere uguale
            return false;

        //Posso costruire prima di muovermi

        if (turn.getWorkerUsed() != 0 && workercell.getWorkerId() != turn.getWorkerUsed()) //if the id doesn't match, false
            return false;

        else {
            if (turn.isMoveBeforeBuild()||turn.getMoveTimes()==0) //se mi sono mosso, allora posso costruire OPPURE, posso costruire se non mi sono ancora mosso)
                return (turn.getBuildTimes() < builds);
            else
                return false;
        }
    }
}
