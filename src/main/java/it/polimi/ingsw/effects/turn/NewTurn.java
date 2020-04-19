package it.polimi.ingsw.effects.turn;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.effects.GodPower;
import java.util.List;


public class NewTurn {
    public Turn newTurn(Turn oldTurn, List<GodPower> godPowers, Player player){
        return new Turn(player);
    }
}
