package it.polimi.ingsw.client;

import it.polimi.ingsw.Position;

import java.util.List;

/** This interface represents client methods callable by view when information is received by the user */

public interface ViewObserver {
    /**This method is called when player has just chosen name and number of players
     *@param myName name
     *@param numOfPlayers number of players*/
    void onCompletedStartup (String myName, int numOfPlayers);
    /**This method is called when player has just chosen a god power
     *@param chosenGodPower god power's name*/
    void onCompletedInitializeGodPower(String chosenGodPower);
    /**This method is called when player has just chosen workers initial positions
     *@param myWorkerPositions list of positions ordered by worker ID*/
    void onCompletedInitializeWorkerPositions(List<Position> myWorkerPositions);
    /**This method is called when player has just chosen to build
     * @param position position
     * @param workerId worker ID
     * @param isDome true if the building is a dome*/
    void onCompletedBuild(Position position, int workerId, boolean isDome);
    /**This method is called when player has just chosen to move
     * @param position position
     * @param workerId worker ID*/
    void onCompletedMove(Position position, int workerId);
    /**This method is called when player has just chosen to end the turn*/
    void onCompletedDecline();
}
