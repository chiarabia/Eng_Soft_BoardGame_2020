package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.server.serializable.SerializableRequestAction;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdateActions;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdateInitializeNames;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdateInitializeWorkerPositions;
import it.polimi.ingsw.model.Position;

import java.util.List;

/**
 * This interface implements methods used by the client to interact with the View
 */

public interface View {
    /* service methods */
    void setBoard(ClientBoard board);
    void addObserver(ViewObserver observer);

    /* representation methods */
    /**This method is called when the game has started and the initial scene is shown*/
    void displayStartup();
    /**This method is called when the WaitingScene needs to be shown*/
    void displayWaitingRoom();
    /**This method is called when the match has been created and either the names of the players are shown or
     * a WaitingScene that notifies the player that the match has started and they need to wait for other players
     * to chose their godPowers
     * @param names holds the information with the players names*/
    void displayPlayerNames(SerializableUpdateInitializeNames names);
    /**This method is called when a player has chosen a god power
    * @param playerId player ID*/
    void displayGodPower(int playerId);
    /**This method is called when godPowers have been chosen and the BoardScene can be shown*/
    void displayBoardScreen();
    /**This method is called when the first turn is starting*/
    void displayGameStart();
    /**This method is called when changes were made to the board and need to be displayed
     * @param update holds the information for possible moves or builds and proprieties of the workers*/
    void displayBoard(SerializableUpdateActions update);
    /**This method is called when workers have been added to the board in the first phase of the game
     * @param update holds the information for possible moves and proprieties of the workers */
    void displayBoard(SerializableUpdateInitializeWorkerPositions update);
    /**This method is called when a new turn has started
     * @param playerId the ID of the player playing the current turn*/
    void displayTurn(int playerId);
    /**This method is called when a player has won
     * @param playerId the ID of the player that has won*/
    void displayWinner(int playerId);
    /**This method is called when a player has lost
     * @param playerId the ID of the player that has lost*/
    void displayLoser(int playerId);
    /**This method is called when an error message has to be shown
     * @param errorId error ID
     * @param isFatalError true if it's fatal error */
    void displayError(int errorId, boolean isFatalError);
    /**This method is called when a player has disconnected
     * @param playerId the ID of the player that has disconnected*/
    void displayDisconnection (int playerId);

    /* input methods */
    /**This method is called when the player has to make an action
     * @param object holds the information for possible moves or builds and proprieties of the workers*/
    void askForAction(SerializableRequestAction object);
    /**This method is called when the player has to choose a god power
     * @param godCards the <code>List&lt;GodCard&gt;</code> with all possibles godCards to choose from for the player*/
    void askForInitialGodPower(List<GodCard> godCards);
    /**This method is called when the player has to choose the initial workers <code>Positions</code>
     * @param possiblePositions <code>List&lt;Position&gt;</code> with the possible <code>Positions</code> for the worker*/
    void askForInitialWorkerPositions(List <Position> possiblePositions);
    /**This method is called when the player has to choose their name and the number of players they want to play with
     *@param error error ID, -1 means no error */
    void askForStartupInfos(int error);
}
