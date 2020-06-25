package it.polimi.ingsw.client;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.*;

import java.util.List;

/** This interface represents view methods callable by client manager */

public interface View {
    /* service methods */
    void setBoard(ClientBoard board);
    void addObserver(ViewObserver observer);

    /* representation methods */
    /**This method is called when the game has just been started, the initial scene has to be shown*/
    void displayStartup();
    /**This method is called when name and number of player have already been given and the waiting
     * room scene before match creation has to be shown*/
    void displayWaitingRoom();
    /**This method is called when the match has been created and the god powers scene has to be shown
     * @param names object*/
    void displayPlayerNames(SerializableUpdateInitializeNames names);
    /**This method is called when a player has just chosen a god power
    * @param playerId player ID*/
    void displayGodPower(int playerId);
    /**This method is called when all god powers have already been chosen and workers initial positions
     * start to be asked*/
    void displayBoardScreen();
    /**This method is called when the first turn is starting*/
    void displayGameStart();
    /**This method is called when changes are made to the board and need to be displayed
     * @param update object*/
    void displayBoard(SerializableUpdateActions update);
    /**This method is called when workers have just been added to the board
     * @param update object*/
    void displayBoard(SerializableUpdateInitializeWorkerPositions update);
    /**This method is called when a new turn has just been started
     * @param playerId player ID*/
    void displayTurn(int playerId);
    /**This method is called when a player has just won
     * @param playerId player ID*/
    void displayWinner(int playerId);
    /**This method is called when a player has just lost
     * @param playerId player ID*/
    void displayLoser(int playerId);
    /**This method is called when an error message has to be shown
     * @param errorId error ID*/
    void displayError(int errorId);
    /**This method is called when a player has just disconnected
     * @param playerId player ID*/
    void displayDisconnection (int playerId);

    /* input methods */
    /**This method is called when player has to choose an action
     * @param object object*/
    void askForAction(SerializableRequestAction object);
    /**This method is called when player has to choose a god power
     * @param godCards list of god cards*/
    void askForInitialGodPower(List<GodCard> godCards);
    /**This method is called when player has to choose initial god powers
     * @param possiblePositions possible positions*/
    void askForInitialWorkerPositions(List <Position> possiblePositions);
    /**This method is called when player has to choose name and number of players
     *@param error error ID, -1 means no error */
    void askForStartupInfos(int error);
}
