package it.polimi.ingsw.Effects.WinCondition;


import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;


import java.util.Set;

public class StandardLoseCondition {

    /**This method determines if a player has lost.
     *
     * <p>When it's not possible to move and then to build the player has lost.
     *
     * @param collectMove the collect generated from the move method for the worker
     * @param collectBuild the collect generated from the build method for the worker
     * @return return true if the player has lost, false otherwise
     */

    public boolean lose (Set<Cell> collectMove, Set<Cell> collectBuild){
        if(collectMove.isEmpty()) return true;
        return collectBuild.isEmpty();
    }
}
