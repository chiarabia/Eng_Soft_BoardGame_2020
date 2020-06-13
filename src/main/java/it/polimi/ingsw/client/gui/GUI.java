package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientBoard;
import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.ViewObserver;

import it.polimi.ingsw.client.gui.controller.BoardSceneController;
import it.polimi.ingsw.client.gui.runnable.*;

import it.polimi.ingsw.server.serializable.*;
import javafx.application.Platform;
import javafx.scene.text.Text;

import java.util.List;

public class GUI implements View {

    private ClientBoard board;
    ChoosingGodSceneRunnable choosingGodSceneRunnable = new ChoosingGodSceneRunnable();
    List<String> notifications;
    List<Integer> actionsCodes;

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
        Platform.runLater(new BoardSceneRunnable());

        Platform.runLater(()->{
            BoardSceneController.notificationsTextFlow.getChildren().add(new Text("welcome"));
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

    }

    @Override
    public void displayWinner(int playerId) {
        Platform.runLater(new WinSceneRunnable());
    }

    @Override
    public void displayLoser(int playerId) {
        Platform.runLater(new LoseSceneRunnable());
    }

    @Override
    public void displayDisconnection(int playerId) {
        Platform.runLater(()->{
            Text notification = new Text (board.getPlayer(playerId).getPlayerName() + "has disconnected");
            BoardSceneController.notificationsTextFlow.getChildren().add(notification);
        });
    }

    @Override
    public void displayError(String message) {
        Platform.runLater(()->{
        Text notification = new Text (message);
            if (BoardSceneController.notificationsTextFlow.getChildren().size() != 0)
                BoardSceneController.notificationsTextFlow.getChildren().clear();
            BoardSceneController.notificationsTextFlow.getChildren().add(notification);
        });
    }

    @Override
    public void displayError() {
        Platform.runLater(()->{
            Text notification = new Text ("Oops... something went wrong");
            if (BoardSceneController.notificationsTextFlow.getChildren().size() != 0)
                BoardSceneController.notificationsTextFlow.getChildren().clear();
            BoardSceneController.notificationsTextFlow.getChildren().add(notification);
            });
    public void displayRequestAction(SerializableRequestAction object) {

    }

    @Override
    public void displayBoard(SerializableUpdateMove update) {
        //actionsCodes.clear();
        //actionsCodes.add(0);
    }

    @Override
    public void displayBoard(SerializableUpdateBuild update) {
    }

    @Override
    public void displayBoard(SerializableUpdateLoser update) {
    }

    @Override
    public void displayBoard(SerializableUpdateInitializeWorkerPositions update) {
    }

    @Override
    public void askForAction(SerializableRequestAction object) {

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
            Text notification = new Text ("Choose your workers positions by clicking on the board");
            if (BoardSceneController.notificationsTextFlow.getChildren().size() != 0)
                BoardSceneController.notificationsTextFlow.getChildren().clear();
            BoardSceneController.notificationsTextFlow.getChildren().add(notification);
        });

        //sets the actionCode to 1 for the askForInitialiWorkerPosition phase
        actionsCodes = MainStage.getActionsCodes();
        actionsCodes.clear();
        actionsCodes.add(1);

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


}
