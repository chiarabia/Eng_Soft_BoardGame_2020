package it.polimi.ingsw.model.effects.turn;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.effects.GodPower;

import java.util.List;

public class RestoreOriginalGodPower extends NewTurn {
    GodPower originalGodPower;

    /**
     * Restores a player's original powers. Can be used at the end of a player's turn with an "Opponent'sTurn" power.
     * @param oldTurn The current turn that is about to end
     * @param godPowers The GodPowers of all players in the game
     * @param nextTurnPlayer Player of the next turn
     * @return next player's Turn object
     */
    @Override
    public Turn endTurn(Turn oldTurn, List<GodPower> godPowers, Player nextTurnPlayer) {

        for(int j = 0; j < godPowers.size(); j++) {
            if (godPowers.get(j)!= null && godPowers.get(j).getPlayerId() == originalGodPower.getPlayerId()) {
                godPowers.set(j, originalGodPower); //restore original GodPower effects

               return godPowers.get(j).getNewTurn().endTurn(oldTurn, godPowers, nextTurnPlayer); //calling the right NewTurn method of the player;
            }
        }
        return super.endTurn(oldTurn, godPowers, nextTurnPlayer); //The method should never exit the for loop and perform this return
    }

    public RestoreOriginalGodPower(GodPower originalGodPower) {
        this.originalGodPower = originalGodPower;
    }
}
