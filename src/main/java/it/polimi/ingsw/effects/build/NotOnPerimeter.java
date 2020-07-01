package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * This class modifies the standard building rule
 * All subsequent constructions cannot be built on perimeteral cells.
 */

public class NotOnPerimeter extends StandardBuild {
    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        Set<Position> temp_cells = super.build(workerPosition, board, turn);

        /**
         * Calls the method build of father class if the worker has not yet built.
         * Otherwise, excludes all perimetral position.
         * @param workerPosition the worker's Position
         * @param board the board
         * @param turn the player's turn
         * @return a <code>Set&lt;Cell&gt;</code> collect that only has the cells where the player can build in
         */
        if(turn.getBuildTimes()>0)
            return temp_cells.stream()
                            .map(a-> board.getCell(a))
                            .filter(a->!a.isPerimetral())
                            .map(a->a.getPosition())
                            .collect(Collectors.toSet());
        else return temp_cells;
    }

    public NotOnPerimeter(int builds) {
        super(builds);
    }
}
