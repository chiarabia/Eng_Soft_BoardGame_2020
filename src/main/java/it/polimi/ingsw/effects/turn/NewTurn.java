package it.polimi.ingsw.effects.turn;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.effects.GodPower;
import java.util.List;




public class NewTurn {

    /**
     * This method end the current turn and starts the turn of the next player
     * @param oldTurn The current turn that is about to end
     * @param godPowers The GodPowers of all players (still) in the game
     * @param nextTurnPlayer Player of the next turn
     * @return //TODO add descrption
     */
    public Turn endTurn(Turn oldTurn, List<GodPower> godPowers, Player nextTurnPlayer){
        return new Turn(nextTurnPlayer);
    }
}
