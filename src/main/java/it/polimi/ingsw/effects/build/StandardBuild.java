package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class defines the general rules of building
 */

public class StandardBuild {
    protected final int builds; //parametrizzo il metodo

    public StandardBuild(int builds) {
        this.builds = builds;
    }

    /**
     * The general build method
     * @param workerCell the worker's Cell
     * @param board the board
     * @param turn the player's turn
     * @return a Set<Cell> collect that only has the cells where the player can build in
     */

    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        if (!checkBuildConditions(workerCell, turn))
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

    /**
     * This method determines if the worker can build
     *
     * @param workerCell the worker's Cell
     * @param turn the player's turn
     * @return
     */

    protected boolean checkBuildConditions(Cell workerCell, Turn turn) {
        //the Cell needs to have a worker
        if (!workerCell.isWorker())
            return false;

        //the player id of this turn must be the same of the worker
        if (workerCell.getPlayerId() != turn.getPlayerId())
            return false;

        //if the player has moved a worker yet
        if (turn.getMoveTimes()==0)
            return false;

        //if the id of the worker is not the same of the id of the worker used in the turn
        if (workerCell.getWorkerId() != turn.getWorkerUsed())
            return false;

        else {
            if (turn.isMoveBeforeBuild()) //se mi sono mosso, allora posso costruire
                return (turn.getBuildTimes() < builds);
            else
                return false;
        }
    }
}
