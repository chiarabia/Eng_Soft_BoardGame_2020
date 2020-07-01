package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.gui.controller.BoardSceneController;
import it.polimi.ingsw.client.gui.controller.ErrorSceneController;
import it.polimi.ingsw.client.gui.controller.LoginSceneController;
import it.polimi.ingsw.client.gui.controller.WaitingSceneController;
import it.polimi.ingsw.client.gui.runnable.*;
import it.polimi.ingsw.server.serializable.*;
import javafx.application.Platform;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class implements the View for the GUI
 */
public class GUI implements View {
    private Textfields textfields = new Textfields();
    private BoardSceneController boardSceneController;
    private LoginSceneController loginSceneController;
    private WaitingSceneController waitingSceneController;
    private ErrorSceneController errorSceneController;
    private ClientBoard board;
    ChoosingGodSceneRunnable choosingGodSceneRunnable = new ChoosingGodSceneRunnable();
    BoardSceneRunnable boardSceneRunnable;
    LoginSceneRunnable loginSceneRunnable;
    WaitingSceneRunnable waitingSceneRunnable;
    ErrorSceneRunnable errorSceneRunnable;

    public GUI() throws ParseException {
    }

    public void addObserver(ViewObserver observer){
        List<ViewObserver> observerList = MainStage.getObserverList();
        observerList.add(observer);}

    /**
     * Launches the JavaFx app
      */
    @Override
    public void displayStartup() {
    	//starts the MainStage javafx application
        new Thread(()-> MainStage.launch(MainStage.class)).start();
    }

    /**
     * Not implemented in GUI
     */
    @Override
    public void displayGameStart(){}

    /**
     * Displays the BoardScene and adds the Player ID in the GUI cache
     */
    @Override
    public void displayBoardScreen(){
        //displays the BoardScene
        boardSceneRunnable = new BoardSceneRunnable();
        //displays the boardScene
        Platform.runLater(boardSceneRunnable);
        //adds the PlayerId of the player to the playerData cache
        ArrayList<Object> playerData = MainStage.getPlayerData();
        playerData.add(board.getMyPlayerId());

        Platform.runLater(()->{
            //gets the boardSceneController
            boardSceneController = boardSceneRunnable.getBoardSceneController();
            boardSceneController.displayGameInfos(board);
        });

    }

    /**
     * Displays the WaitingScene
     */
    @Override
    public void displayWaitingRoom() {
        //displays the WaitingRoomScene
        waitingSceneRunnable = new WaitingSceneRunnable();
        Platform.runLater(waitingSceneRunnable);
        Platform.runLater(()->{
            //gets the waitingSceneController
            waitingSceneController = waitingSceneRunnable.getWaitingSceneController();
        });
    }

    /**
     * Sets the board
     * @param board the <code>ClientBoard</code> received from the Client
     */
    public void setBoard(ClientBoard board) {
        this.board = board;
    }

    /**
     * Changes the <code>Label</code> in WaitingScene to notify the players
     * that the match has started but they need to wait for other players
     * to choose their GodCard
     * @param names object it contains a <code>List</code> with the Players names. Not used for the GUI
     */
    @Override
    public void displayPlayerNames(SerializableUpdateInitializeNames names) {
        Platform.runLater(()->{
            waitingSceneController.updateWaitingLabel();
        });
    }

    /**
     * Not implemented in GUI
     * @param playerId player ID
     */
    @Override
    public void displayGodPower(int playerId){

    }

    /**
     * When a new turn starts the players are notified either that their turn has started
     * or which player's turn started.
     * @param currentPlayerId
     */
    @Override
    public void displayTurn(int currentPlayerId) {
        int myPlayerId = board.getMyPlayerId();
        Platform.runLater(()->{
            boardSceneController.setWorkerSelected(false);
        });

        if (myPlayerId == currentPlayerId){
            Platform.runLater(()->{
                String notification = "You" + textfields.getPlaying1() + "\n";
                boardSceneController.displayNotificationsDuringTurn(notification);
                boardSceneController.setMyTurn(true);
            });
        }
        else {
            Platform.runLater(()->{
                String notification = board.getPlayer(currentPlayerId).getPlayerName() + textfields.getPlaying2()+ "\n";
                boardSceneController.displayNotificationsDuringTurn(notification);
                boardSceneController.setMyTurn(false);
            });
        }
    }

    /**
     * Displays in the BoardScene an <code>Image</code> that is either a Win or Lose image
     * @param playerId player ID
     */
    @Override
    public void displayWinner(int playerId) {
        Platform.runLater(()->{
        	//displays a winner notification of the winner

            boardSceneController.displayEndGameImage(playerId == board.getMyPlayerId());
        });
    }

    /**
     * Displays in the BoardScene the name of the player that has lost
     * and removes its workers from the board.
     * @param playerId player ID
     */
    @Override
    public void displayLoser(int playerId) {
        Platform.runLater(()->{
            //removes the player workers frm the board
            for(int i = 1; i<3; i++) {
                boardSceneController
                        .removeWorker(getWorkerPositions(playerId, i)
                                .mirrorYCoordinate());
            }

        	//displays a lose notification of the player that has lost
            String notification;
            if (playerId==board.getMyPlayerId()) notification = "You"+ textfields.getLost1()+ "\n";
            else notification = board.getPlayer(playerId).getPlayerName() + textfields.getLost2() +"\n";
            boardSceneController.displayNotificationsDuringTurn(notification);
        });

    }

    /**
     * Displays the ErrorScene with a disconnection message.
     * @param playerId player ID
     */
    @Override
    public void displayDisconnection(int playerId) {
        String notification;
        notification = board.getPlayer(playerId).getPlayerName() + textfields.getDisconnected() + ". Game ended\n";
        errorSceneRunnable = new ErrorSceneRunnable();
        Platform.runLater(errorSceneRunnable);
        Platform.runLater(()->{
            //gets the boardSceneController
            errorSceneController = errorSceneRunnable.getErrorSceneController();
            errorSceneController.updateErrorLabel(notification);
        });

    }

    /**
     * Displays an error message.
     * <p>If the error is a fatal error it is displayed in the ErrorScene. Otherwise the error is visible
     * in the BoardScene as a notification.
     * @param errorId error ID
     * @param isFatalError true if it's fatal error
     */
    @Override
    public void displayError(int errorId, boolean isFatalError) {
        String message = null;
        switch (errorId){
            case 0: message = textfields.getErr0(); break;
            case 1: message = textfields.getErr1(); break;
            case 2: message = textfields.getErr2(); break;
        }
        String finalMessage = message;
        if (isFatalError){
            errorSceneRunnable = new ErrorSceneRunnable();
            Platform.runLater(errorSceneRunnable);
            Platform.runLater(()->{
                //gets the boardSceneController
                errorSceneController = errorSceneRunnable.getErrorSceneController();
                errorSceneController.updateErrorLabel(finalMessage);
            });
        } else {
            Platform.runLater(() -> {boardSceneController.displayNotificationsDuringTurn(finalMessage);});
        }
    }

    /**
     * Displays the changes of the board.
     * <p>Tells the BoardSceneController where to add a new building and where to add or remove workers.
     * @param update holds the information for possible moves and proprieties of the workers
     */
    @Override
    public void displayBoard(SerializableUpdateActions update) {
        List<SerializableUpdateMove> updateMove = update.getUpdateMove();
        List<SerializableUpdateBuild> updateBuild = update.getUpdateBuild();

		//if there is a building to update
        if(!updateBuild.isEmpty()){
            for (int i = 0; i < updateBuild.size(); i++){
                Position newPosition = updateBuild.get(i).getNewPosition().mirrorYCoordinate();
                boolean dome = updateBuild.get(i).isDome();
                Platform.runLater(()-> boardSceneController.updateBuilding(newPosition, dome));
            }
        }

        //if there is a worker position to update
        if(!updateMove.isEmpty()){
            for (int i = 0; i < updateMove.size(); i++) {
                int playerID = updateMove.get(i).getPlayerId();
                Position newPosition = updateMove.get(i).getNewPosition().mirrorYCoordinate();
                Position oldPosition = updateMove.get(i).getStartingPosition().mirrorYCoordinate();

                if(moveIsSwap(updateMove)) {
                    Platform.runLater(() -> boardSceneController.updateWorker(newPosition, newPosition, playerID));
                }

                else {
                    Platform.runLater(() -> boardSceneController.updateWorker(newPosition, oldPosition, playerID));
                }
            }
        }
    }

    /**
     * Displays the changes to the board in the initial phase of the game.
     * <p>It updates the board with the initial positions of the workers.
     * @param update holds the information for possible moves and proprieties of the workers
     */
    @Override
    public void displayBoard(SerializableUpdateInitializeWorkerPositions update) {
        List<Position> workerPositions = update.getWorkerPositions();
        ArrayList<Position> tempList = new ArrayList<>();

        for(int i = 0; i<update.getWorkerPositions().size(); i++) {
            tempList.add(i,  workerPositions.get(i).mirrorYCoordinate());
        }

        int playerID = update.getPlayerId();

        Platform.runLater(()->{
        	//displays the worker initial position on only enemy players
            if (playerID != board.getMyPlayerId()) {
                for (int i = 0; i < workerPositions.size(); i++) {
                    boardSceneController.updateWorkerInitialPosition(tempList.get(i), update.getPlayerId());
                }
            }
        });
    }

    //TODO: javadoc
    private boolean moveIsSwap(List<SerializableUpdateMove> updateMoveList) {
        if (updateMoveList.size()==2) {
            SerializableUpdateMove firstMove = updateMoveList.get(0);
            SerializableUpdateMove secondMove = updateMoveList.get(1);
            return  firstMove.getNewPosition()
                        .equals(secondMove.getStartingPosition()) &&
                    secondMove.getNewPosition()
                        .equals(firstMove.getStartingPosition());
        }
        else
            return false;
    }

    /**
     * Handles the information for the GUI about the possible action a player can make.
     * <p>It tells the BoardSceneController if and where a worker can move/build, if they can end the turn
     * or if they can build a dome
     * @param object holds the information for possible moves and proprieties of the workers
     */

    @Override
    public void askForAction(SerializableRequestAction object) {

        BoardSceneController.updateActionCode(2);
        int currentPlayerID = board.getPlayerTurnId();
        Position firstWorkerPosition = getWorkerPositions(currentPlayerID,1).mirrorYCoordinate();
        Position secondWorkerPosition = getWorkerPositions(currentPlayerID,2).mirrorYCoordinate();

        //updates all the Sets in the BoardSceneController and the Workers positions
        Platform.runLater(()->{
            boardSceneController.setOldFirstWorkerPosition(firstWorkerPosition);
            boardSceneController.setOldSecondWorkerPosition(secondWorkerPosition);
            boardSceneController.setWorker1MovesPosition(mirrorPositionYCoordinate(object.getWorker1Moves()));
            boardSceneController.setWorker2MovesPosition(mirrorPositionYCoordinate(object.getWorker2Moves()));
            boardSceneController.setWorker1BuildPosition(mirrorPositionYCoordinate(object.getWorker1Builds()));
            boardSceneController.setWorker2BuildPosition(mirrorPositionYCoordinate(object.getWorker2Builds()));
        });

        //move possible
        if (!object.areMovesEmpty()) {
            Platform.runLater(() -> {
                boardSceneController.setMovePossible(true);
            });
        }
        //move not possible
        if(object.areMovesEmpty()) {
            Platform.runLater(() -> boardSceneController.setMovePossible(false));
        }

        //build possible
        if(!object.areBuildsEmpty()){
            Platform.runLater(() -> {
                boardSceneController.setBuildPossible(true);
            });
        }
        //build not possible
        if(object.areBuildsEmpty()){
            Platform.runLater(() -> boardSceneController.setBuildPossible(false));
        }
        //build and dome building allowed
        if(!object.areBuildsEmpty() &&  object.isCanForceDome()){
            Platform.runLater(() -> {
                boardSceneController.setDomeAtAnyLevelPossible(true);
                boardSceneController.setVisibleDomeButton(true);
            });
        }
		//dome building everywhere not possible
        if(!object.isCanForceDome()) {
            Platform.runLater(() -> boardSceneController.setDomeAtAnyLevelPossible(false));
        }
        //can decline allowed
        if(object.canDecline()){
            Platform.runLater(() -> {
                boardSceneController.displayNotificationsDuringTurn(textfields.getCanendturn()+ "\n");
                boardSceneController.setDeclinePossible(true);
                boardSceneController.setVisibleDeclineButton(true);
                boardSceneController.disableDeclineButton(false);
            });
        }
        //can decline not allowed
        if(!object.canDecline()){
            Platform.runLater(() -> boardSceneController.setDeclinePossible(false));
        }
    }

    /**
     * Displays the ChoosingGodPowerScene and adds the GodCards to the GUI cache
     * @param godPowers the list of possible GodCards to choose from for the players
     */
    @Override
    public void askForInitialGodPower(List<GodCard> godPowers) {
        //stores in GodCards the godPowers chosen by the server
        List<GodCard> godCards = MainStage.getGodPowers();
        godCards.addAll(godPowers);

        //displays the choosingGodScene
        Platform.runLater(choosingGodSceneRunnable);
    }

    /**
     * Sets the BoardSceneController into the phase when the player need to choose their workers first positions
     * <p>The actionCode of BoardSceneController is set to 1
     * @param possiblePositions <code>List&lt;Position&gt;</code> of the possible positions for the workers
     */
    @Override
    public void askForInitialWorkerPositions(List <Position> possiblePositions) {
        //adds the current notification for the player
        Platform.runLater(()->{
            boardSceneController.setPossiblePosition(possiblePositions);
            //sets the actionCode to 1 for the askForInitialiWorkerPosition phase
            BoardSceneController.updateActionCode(1);
        });

    }

    /**
     * Displays the LoginSceneController when the main javafx application is ready.
     * The player can insert their name and choose the number of players of the match.
     * If the name of the player is invalid, the LoginScene is shown again, with an error notification
     * @param errorId
     */
    @Override
    public void askForStartupInfos(int errorId) {
        // I can't call Platform.runLater until the JavaFX application has started.
        // errorId: -1 <==> no error, 1 <==> name error

        try {
            MainStage.getLock().take();
        } catch (Exception ignored) {}

        loginSceneRunnable = new LoginSceneRunnable();

        //displays the LoginScene
        if(errorId == 1){
            ArrayList<Object> playerData = MainStage.getPlayerData();
            playerData.clear();
            Platform.runLater(loginSceneRunnable);
            Platform.runLater(()->{
                loginSceneController = loginSceneRunnable.getLoginSceneController();
                loginSceneController.updateErrorLabel();
            });
        }
        else Platform.runLater(loginSceneRunnable);

        try {
            MainStage.getLock().put(MainStage.getLock());
        } catch (Exception ignored) {}
    }

    /**
     * Gets the <code>Position</code> of a worker
     * @param playerID the PlayerID of the player
     * @param workerID the workerID of the worker
     * @return the <code>Position</code>
     */
    public Position getWorkerPositions(int playerID, int workerID){
        int x = board.getPlayer(playerID).getWorker(workerID).getX();
        int y = board.getPlayer(playerID).getWorker(workerID).getY();
        return new Position(x,y,0);
    }

    //TODO add javadoc
    private Set<Position> mirrorPositionYCoordinate(Set<Position> set) {
        Set<Position> tempSet = new HashSet<>();

        for (Position position : set) {
            tempSet.add(position.mirrorYCoordinate());
        }
         return tempSet;
    }
}

