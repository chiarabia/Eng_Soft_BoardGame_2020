package it.polimi.ingsw.effects.turn;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.effects.GodPower;

import java.util.List;

public class RestoreOriginalGodPower extends NewTurn {
    GodPower originalGodPower;


    /**
     * This method restores a player's original powers. Can be used at the end of a player's turn with an "Opponent'sTurn" power.
     * @param oldTurn The current turn that is about to end
     * @param godPowers The GodPowers of all players in the game
     * @param player Player of the next turn
     * @return
     */
    @Override
    public Turn newTurn(Turn oldTurn, List<GodPower> godPowers, Player player) {
        int j;
        for(j = 0; j <godPowers.size(); j++) {
            if (godPowers.get(j).getPlayerId() == originalGodPower.getPlayerId()) {
                godPowers.add(j, originalGodPower); //restore original GodPower effects

                return godPowers.get(j).getNewTurn().newTurn(oldTurn, godPowers, player); //calling the right NewTurn method of the player;
            }
        }
        return super.newTurn(oldTurn, godPowers, player); //The method should never exit the for loop and perform this return
    }

    public RestoreOriginalGodPower(GodPower originalGodPower) {
        originalGodPower = originalGodPower;
    }
}
