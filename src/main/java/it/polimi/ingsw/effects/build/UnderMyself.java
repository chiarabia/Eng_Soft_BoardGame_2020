package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
/**
*This class implements the possibility to build before moving
*/

public class UnderMyself extends StandardBuild {
    /**
     * Adds the cell where the worker is positioned to the set of possible constructions (if z &lt; 3).
     * @param workerPosition the worker's Position
     * @param board the board
     * @param turn the player's turn
     * @return a <code>Set&lt;Cell&gt;</code> collect that only has the cells where the player can build in
     */

    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        if (!checkBuildConditions(workerCell, turn)) return new HashSet<Position>();
        else {
            Set<Position> standardBuild = super.build(workerPosition, board, turn);
            if (workerCell.getZ() < 3) standardBuild.add(workerPosition);
            return standardBuild;
        }
    }

    public UnderMyself(int builds) {
        super(builds);
    }
}
