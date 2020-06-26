package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.gui.controller.BoardSceneController;
import it.polimi.ingsw.client.gui.controller.LoginSceneController;
import it.polimi.ingsw.client.gui.controller.WaitingSceneController;
import it.polimi.ingsw.client.gui.runnable.BoardSceneRunnable;
import it.polimi.ingsw.client.gui.runnable.ChoosingGodSceneRunnable;
import it.polimi.ingsw.client.gui.runnable.LoginSceneRunnable;
import it.polimi.ingsw.client.gui.runnable.WaitingSceneRunnable;
import it.polimi.ingsw.server.serializable.*;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GUI implements View {
    private Textfields textfields = new Textfields();
    private BoardSceneController boardSceneController;
    private LoginSceneController loginSceneController;
    private WaitingSceneController waitingSceneController;

    private ClientBoard board;
    ChoosingGodSceneRunnable choosingGodSceneRunnable = new ChoosingGodSceneRunnable();
    BoardSceneRunnable boardSceneRunnable;
    LoginSceneRunnable loginSceneRunnable;
    WaitingSceneRunnable waitingSceneRunnable;

    public GUI() throws ParseException {
    }

    public void addObserver(ViewObserver observer){
        List<ViewObserver> observerList = MainStage.getObserverList();
        observerList.add(observer);}

    @Override
    public void displayStartup() {
    	//starts the MainStage javafx application
        new Thread(()-> MainStage.launch(MainStage.class)).start();
    }

    @Override
    public void displayGameStart(){}

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
        });
    }

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

    public void setBoard(ClientBoard board) {
        this.board = board;
    }

    @Override
    public void displayPlayerNames(SerializableUpdateInitializeNames names) {
        Platform.runLater(()->{
            waitingSceneController.updateWaitingLabel();
        });
    }

    @Override
    public void displayGodPower(int playerId){

    }


    @Override
    public void displayTurn(int currentPlayerId) {
        int myPlayerId = board.getMyPlayerId();
        boardSceneController.setWorkerSelected(false);
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

    @Override
    public void displayWinner(int playerId) {
        Platform.runLater(()->{
        	//displays a winner notification of the winner

            boardSceneController.displayEndGameImage(playerId == board.getMyPlayerId());
            /*String notification = board.getPlayer(playerId).getPlayerName() + " won!!";
            Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText(notification);*/
        });
    }

    @Override
    public void displayLoser(int playerId) {
        Platform.runLater(()->{
        	//displays a lose notification of the player that has lost
            String notification;
            if (playerId==board.getMyPlayerId()) notification = "You"+ textfields.getLost1()+ "\n";
            else notification = board.getPlayer(playerId).getPlayerName() + textfields.getLost2() +"\n";
            boardSceneController.displayNotificationsDuringTurn(notification);
        });

    }

    @Override
    public void displayDisconnection(int playerId) {
        String notification;
        notification = board.getPlayer(playerId).getPlayerName() + textfields.getDisconnected() + "\n";

        if (getWorkerPositions(board.getMyPlayerId(), 1) == null ||
                getWorkerPositions(board.getMyPlayerId(), 2) == null) {
            Platform.runLater(()->{
                //displays a disconnection message
                //boardSceneController.displayNotificationsDuringTurn(notification);
                Text oldText = BoardSceneController.getNotification();
                setTextFormat(oldText);
                oldText.setText(notification);
            });

        }
        else {
            Platform.runLater(()->{
                //displays a disconnection message
                boardSceneController.displayNotificationsDuringTurn(notification);
            });
        }


    }

    @Override
    public void displayError(int errorId) {
        String message = null;
        switch (errorId){
            case 0: message = textfields.getErr0(); break;
            case 1: message = textfields.getErr1(); break;
            case 2: message = textfields.getErr2(); break;
        }
        String finalMessage = message;
        Platform.runLater(()->{
        	//displays an error message
            boardSceneController.displayNotificationsDuringTurn(finalMessage);
            /*Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText(finalMessage);*/
        });
    }

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
                boardSceneController.displayNotificationsDuringTurn(textfields.getCanmove());
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
                boardSceneController.displayNotificationsDuringTurn(textfields.getCanbuild());
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
                boardSceneController.displayNotificationsDuringTurn(textfields.getCanendturn());
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

    @Override
    public void askForInitialGodPower(List<GodCard> godPowers) {
        //stores in GodCards the godPowers choosed by the server
        List<GodCard> godCards = MainStage.getGodPowers();
        godCards.addAll(godPowers);

        //displays the choosingGodScene
        Platform.runLater(choosingGodSceneRunnable);

        //alla fine deve chiamare onCompletedInitializeGodPower(String chosenGodPower)
    }

    @Override
    public void askForInitialWorkerPositions(List <Position> possiblePositions) {
        //adds the current notification for the player
        Platform.runLater(()->{
            /*String notification ="Choose your workers positions by clicking on the board";
            Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText("Choose your workers positions by clicking on the board");*/
            //sets the actionCode to 1 for the askForInitialiWorkerPosition phase
            BoardSceneController.updateActionCode(1);
        });

        //alla fine deve chiamare onCompletedInitializeWorkerPositions(List<Position> myWorkerPositions)
    }

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

    //sets the
    public void setTextFormat(Text notification){
        notification.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
    }

    public Position getWorkerPositions(int playerID, int workerID){
        int x = board.getPlayer(playerID).getWorker(workerID).getX();
        int y = board.getPlayer(playerID).getWorker(workerID).getY();
        return new Position(x,y,0);
    }

    private Set<Position> mirrorPositionYCoordinate(Set<Position> set) {
        Set<Position> tempSet = new HashSet<>();

        for (Position position : set) {
            tempSet.add(position.mirrorYCoordinate());
        }
         return tempSet;
    }
}

