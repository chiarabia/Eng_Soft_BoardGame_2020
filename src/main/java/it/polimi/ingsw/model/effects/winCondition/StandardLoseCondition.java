package it.polimi.ingsw.model.effects.winCondition;


import it.polimi.ingsw.model.Position;


import java.util.Set;

/**
 * This class handles the standard lose condition.
 * <p>The player loses when they can no longer move and build
 */
public class StandardLoseCondition {

    /**This method determines if a player has lost.
     *
     * <p>When it's not possible to move and then to build the player has lost.
     *
     * @param collectMove the collect generated from the move method for the worker
     * @param collectBuild the collect generated from the build method for the worker
     * @return return true if the player has lost, false otherwise
     */

    public boolean lose (Set<Position> collectMove, Set<Position> collectBuild){
        if(!collectMove.isEmpty()) return false;
        return collectBuild.isEmpty();
    }
}
