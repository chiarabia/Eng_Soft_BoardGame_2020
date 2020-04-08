package it.polimi.ingsw.effects.turn;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.move.NoMoveUp;
import it.polimi.ingsw.effects.move.StandardMove;
import java.util.List;
import java.util.stream.Collectors;


public class NewNoMoveUpTurn extends NewTurn {
    private int powerOwnerId;
    private List <StandardMove> originalMoves;
    private boolean areMovesDecorated(){return originalMoves!=null;}

    public Turn newTurn(Turn oldTurn, List<GodPower> godPowers, Player player){
        if (oldTurn.getPlayerId()==powerOwnerId && oldTurn.isMoveUp()){
            // ha appena giocato Athena e Athena è salita: mette i decorator NoMoveUp
            originalMoves = godPowers.stream().map(x->x.getMove()).collect(Collectors.toList());
            godPowers.stream().forEach(x->x.setMove(new NoMoveUp(x.getMove())));
        }
        if (player.getId()==powerOwnerId && areMovesDecorated()) {
            // sta per giocare Athena e ci sono i decorator NoMoveUp: ripristina le Moves originali
            for (int i = 0; i < originalMoves.size(); i++)
                godPowers.get(i).setMove(originalMoves.get(i));
            originalMoves = null;
        }
        return new Turn(player);
    }


    public NewNoMoveUpTurn(int powerOwnerId) {
        this.powerOwnerId = powerOwnerId;
        this.originalMoves = null;
    }
}
