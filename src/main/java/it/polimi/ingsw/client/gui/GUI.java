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

import java.util.List;

public class GUI implements View {

    private ClientBoard board;
    ChoosingGodSceneRunnable choosingGodSceneRunnable = new ChoosingGodSceneRunnable();
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
            oldText.setText(message);
        });
    }

    @Override
    public void displayRequestAction(SerializableRequestAction object) {

    }

    @Override
    public void displayBoard(SerializableUpdateInfos update) {

    }
    /*
    @Override
    public void displayBoard(SerializableUpdateMove update) {


    }

    @Override
    public void displayBoard(SerializableUpdateBuild update) {
        Position newPosition = update.getNewPosition();
        boolean dome = update.isDome();
        Platform.runLater(()->{
            BoardSceneController.updateBuilding(newPosition, dome);
        });
    }*/

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
            String notification ="Choose your workers positions by clicking on the board";
            Text oldText = BoardSceneController.getNotification();
            setTextFormat(oldText);
            oldText.setText(notification);
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


    public void setTextFormat(Text notification){
        notification.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
    }
}
