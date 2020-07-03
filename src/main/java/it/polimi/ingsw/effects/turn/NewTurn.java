package it.polimi.ingsw.effects.turn;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.effects.GodPower;
import java.util.List;

/**
 * This class implements the standard method for passing the turn
 */

public class NewTurn {

    /**
     * Ends the current turn and starts the turn of the next player
     * @param oldTurn The current turn that is about to end
     * @param godPowers The GodPowers of all players (still) in the game
     * @param nextTurnPlayer Player of the next turn
     * @return next player's Turn object
     */
    public Turn endTurn(Turn oldTurn, List<GodPower> godPowers, Player nextTurnPlayer){
        return new Turn(nextTurnPlayer);
    }
}
