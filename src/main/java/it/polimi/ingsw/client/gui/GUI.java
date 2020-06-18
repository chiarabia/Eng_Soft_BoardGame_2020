package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.ClientBoard;
import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.ViewObserver;

import it.polimi.ingsw.client.gui.controller.BoardSceneController;
import it.polimi.ingsw.client.gui.runnable.*;

import it.polimi.ingsw.server.serializable.*;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class GUI implements View {
    private static BoardSceneController boardSceneController;
    private ClientBoard board;
    ChoosingGodSceneRunnable choosingGodSceneRunnable = new ChoosingGodSceneRunnable();
    BoardSceneRunnable boardSceneRunnable;

    public void addObserver(ViewObserver observer){
        List<ViewObserver> observerList = MainStage.getObserverList();
        observerList.add(observer);}

    @Override
    public void displayStartup() {
        new Thread(()-> {
            MainStage.launch(MainStage.class);
        }).start();
    }

    @Override
    public void displayGameStart(){}

    @Override
    public void displayBoardScreen(){
        //displays the BoardScene
        boardSceneRunnable = new BoardSceneRunnable();
        boardSceneController = boardSceneRunnable.getBoardSceneController();
        Platform.runLater(boardSceneRunnable);
        ArrayList<Object> playerData = MainStage.getPlayerData();
        playerData.add(board.getMyPlayerId());
        Platform.runLater(()->{
            Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText("Welcome!");
        });
    }

    @Override
    public void displayWaitingRoom() {
        //displays the WaitingRoomScene
        Platform.runLater(new WaitingSceneRunnable());

    }

    public void setBoard(ClientBoard board) {
        this.board = board;
    }

    @Override
    public void displayPlayerNames(SerializableUpdateInitializeNames names) {
    }

    @Override
    public void displayGodPower(int playerId){

    }


    @Override
    public void displayTurn() {
        int currentPlayerID = board.getPlayerTurnId();
        int myPlayerID = board.getMyPlayerId();
        if (myPlayerID == currentPlayerID){
            Platform.runLater(()->{
                String notification = "It's your turn!";
                Text oldText = BoardSceneController.getNotification();
                setTextFormat(oldText);
                oldText.setText(notification);
            });
        }
        else {
            Platform.runLater(()->{
                String notification = "It's" + board.getPlayer(currentPlayerID).getPlayerName() + "turn!";
                Text oldText = BoardSceneController.getNotification();
                setTextFormat(oldText);
                oldText.setText(notification);
            });
        }
    }

    @Override
    public void displayWinner(int playerId) {
        Platform.runLater(()->{
            String notification = board.getPlayer(playerId).getPlayerName() + " won!!";
            Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText(notification);
        });
    }

    @Override
    public void displayLoser(int playerId) {
        Platform.runLater(()->{
            String notification = board.getPlayer(playerId).getPlayerName() + " lost";
            Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText(notification);
        });

    }

    @Override
    public void displayDisconnection(int playerId) {
        Platform.runLater(()->{
            String notification = board.getPlayer(playerId).getPlayerName() + " has disconnected";
            Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText(notification);
        });
    }

    @Override
    public void displayError(String message) {
        Platform.runLater(()->{
            Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText(message);;
        });
    }

    @Override
    public void displayRequestAction(SerializableRequestAction object) {

    }

    @Override
    public void displayBoard(SerializableUpdateActions update) {
        List<SerializableUpdateMove> updateMove = update.getUpdateMove();
        List<SerializableUpdateBuild> updateBuild = update.getUpdateBuild();


        if(!updateBuild.isEmpty()){
            for (int i = 0; i < updateBuild.size(); i++){
                Position newPosition = updateBuild.get(0).getNewPosition();
                boolean dome = updateBuild.get(0).isDome();
                Platform.runLater(()->{
                    boardSceneController.updateBuilding(newPosition, dome);
                });
            }
        }

        if(!updateMove.isEmpty()){
           for (int i = 0; i < updateMove.size(); i++){
               Position newPosition = updateMove.get(i).getNewPosition();
               Position oldPosition = updateMove.get(i).getStartingPosition();
               int playerID = updateMove.get(i).getPlayerId();
               Platform.runLater(()->{
                   boardSceneController.updateWorker(newPosition,oldPosition,playerID);
               });
           }
        }
    }

    @Override
    public void displayBoard(SerializableUpdateLoser update) {
    }

    @Override
    public void displayBoard(SerializableUpdateInitializeWorkerPositions update) {
        List<Position> workerPositions = update.getWorkerPositions();
        int playerID = update.getPlayerId();

        boardSceneController = boardSceneRunnable.getBoardSceneController();
        Platform.runLater(()->{
            if (playerID != board.getMyPlayerId()) {
                for (int i = 0; i < workerPositions.size(); i++) {
                    boardSceneController.updateWorkerInitialPosition(workerPositions.get(i), update.getPlayerId());
                }
            }
        });
    }

    @Override
    public void askForAction(SerializableRequestAction object) {
        BoardSceneController.updateActionCode(2);
        int currentPlayerID = board.getPlayerTurnId();
        Position firstWorkerPosition = getWorkerPostions(currentPlayerID,1);
        Position secondWorkerPosition = getWorkerPostions(currentPlayerID,2);

        Platform.runLater(()->{
            boardSceneController.setOldFirstWorkerPosition(firstWorkerPosition);
            boardSceneController.setOldSecondWorkerPosition(secondWorkerPosition);
            boardSceneController.setVisibleDeclineButton(false);
            boardSceneController.setVisibleDomeButton(false);
            boardSceneController.setWorker1MovesPosition(object.getWorker1Moves());
            boardSceneController.setWorker2MovesPosition(object.getWorker2Moves());
            boardSceneController.setWorker1BuildPosition(object.getWorker1Builds());
            boardSceneController.setWorker2BuildPosition(object.getWorker2Builds());
        });

        //move
        if (!object.areMovesEmpty()) {
            Platform.runLater(() -> {
                boardSceneController.displayNotificationsDuringTurn("You can move \n");
                boardSceneController.setMovePossible(true);
            });
        }
        if(object.areMovesEmpty()) {
            Platform.runLater(() -> {
                boardSceneController.setMovePossible(false);
            });
        }
        //build
        if(!object.areBuildsEmpty()){
            Platform.runLater(() -> {
                boardSceneController.displayNotificationsDuringTurn("You can build \n");
                boardSceneController.setBuildPossible(true);
            });
        }
        if(object.areBuildsEmpty()){
            Platform.runLater(() -> {
                boardSceneController.setBuildPossible(false);
            });
        }
        //build and dome building allowed
        if(!object.areBuildsEmpty() &&  object.isCanForceDome()){
            Platform.runLater(() -> {
                boardSceneController.setDomeAtAnyLevelPossible(true);
                boardSceneController.setVisibleDomeButton(true);
            });
        }

        if(!object.isCanForceDome()) {
            Platform.runLater(() -> {
                boardSceneController.setDomeAtAnyLevelPossible(false);
            });
        }
        //can decline allowed
        if(object.canDecline()){
            Platform.runLater(() -> {
                boardSceneController.displayNotificationsDuringTurn("You can decline the action, just press can decline \n");
                boardSceneController.setDeclinePossible(true);
                boardSceneController.setVisibleDeclineButton(true);
            });
        }
        if(!object.canDecline()){
            Platform.runLater(() -> {
                boardSceneController.setDeclinePossible(false);
            });
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
    public void askForInitialWorkerPositions() {
        //adds the current notification for the player
        Platform.runLater(()->{
            String notification ="Choose your workers positions by clicking on the board";
            Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText(notification);
            //sets the actionCode to 1 for the askForInitialiWorkerPosition phase
            BoardSceneController.updateActionCode(1);
        });

        //alla fine deve chiamare onCompletedInitializeWorkerPositions(List<Position> myWorkerPositions)
    }

    @Override
    public void askForStartupInfos() {
        // I can't call Platform.runLater until the JavaFX application has started.
        try {
            MainStage.getLock().take();
        } catch (Exception ignored) {};

        //displays the LoginScene
        Platform.runLater(new LoginSceneRunnable());

    }


    public void setTextFormat(Text notification){
        notification.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
    }

    public Position getWorkerPostions(int playerID, int workerID){
        int x = board.getPlayer(playerID).getWorker(workerID).getX();
        int y = board.getPlayer(playerID).getWorker(workerID).getY();
        Position workerPosition = new Position(x,y,0);
        return workerPosition;
    }

    }

