package it.polimi.ingsw.effects.turn;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.consolidateMove.StandardConsolidateMove;
import it.polimi.ingsw.effects.move.NoMoveUp;
import it.polimi.ingsw.effects.move.StandardMove;
import java.util.List;
import java.util.stream.Collectors;


public class NewNoMoveUpTurn extends NewTurn {

    StandardMove originalMove;
    StandardMove modifiedMove;
    NewTurn originalNewTurn;
    NewTurn modifiedNewTurn;
    GodPower originalGodPower;

    /**
     * This method limits opponents' actions if at least one worker has leveled up during the player's turn.
     * <p>The method first saves a copy of the opponents' godPower, move effect and newTurn. Then the newTurn and move
     * are restored.
     * @param oldTurn The current turn that is about to end
     * @param godPowers The GodPowers of all players in the game
     * @param player Player of the next turn
     * @return a new Turn
     */

    @Override
    public Turn endTurn(Turn oldTurn, List<GodPower> godPowers, Player player){
        if (oldTurn.isMoveUp()) {
            for (int i = 0; i < godPowers.size(); i++) {
                if (i != godPowers.get(i).getPlayerId()) { //the change has effect only on the opponents' godPowers
                    originalGodPower = godPowers.get(i).copyGodPower(godPowers.get(i)); //Saving the original GodPower
                    originalMove = godPowers.get(i).getMove(); //Collecting the previous Move effect
                    originalNewTurn = godPowers.get(i).getNewTurn(); //Collecting the original NewTurn effect

                    //modifying the opponents' NewTurn method to restore, at the end of their turn, the previous conditions
                    modifiedNewTurn = new RestoreOriginalGodPower(originalGodPower);
                    modifiedMove = new NoMoveUp(originalMove);

                    //modifying the opponent GodPower
                    godPowers.get(i).setMove(modifiedMove);
                    godPowers.get(i).setNewTurn(modifiedNewTurn);
                }
            }
        }
        return new Turn(player);
    }
}
