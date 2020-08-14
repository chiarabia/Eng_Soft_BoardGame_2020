package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Position;

import java.util.List;
/**
 * This interface implements methods used by the view when the user has to send information to the client
 */

public interface ViewObserver {
    /**This method is called when the player has chosen their name and number of players
     *@param myName the name of the player
     *@param numOfPlayers number of players*/
    void onCompletedStartup (String myName, int numOfPlayers);
    /**This method is called when the player has chosen a god power
     *@param chosenGodPower god power's name*/
    void onCompletedInitializeGodPower(String chosenGodPower);
    /**This method is called when the player has chosen their workers initial <code>Position</code>
     *@param myWorkerPositions list of positions sorted by worker ID*/
    void onCompletedInitializeWorkerPositions(List<Position> myWorkerPositions);
    /**This method is called when the player has chosen to build
     * @param position the <code>Position</code> of the new building
     * @param workerId the worker ID of the worker tha built
     * @param isDome true if the building is a dome*/
    void onCompletedBuild(Position position, int workerId, boolean isDome);
    /**This method is called when the player has chosen to move
     * @param position the new <code>Position</code> of the worker
     * @param workerId worker ID of the worker that moved*/
    void onCompletedMove(Position position, int workerId);
    /**This method is called when the player has chosen to end the turn*/
    void onCompletedDecline();
}
