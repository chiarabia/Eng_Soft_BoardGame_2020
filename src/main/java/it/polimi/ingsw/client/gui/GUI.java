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
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

public class GUI implements View {
    private FXMLLoader boardSceneLoader;
    private ClientBoard board;
    ChoosingGodSceneRunnable choosingGodSceneRunnable = new ChoosingGodSceneRunnable();

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
        BoardSceneRunnable boardSceneRunnable = new BoardSceneRunnable();
        boardSceneLoader = boardSceneRunnable.getLoader();
        Platform.runLater(boardSceneRunnable);

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
    public void displayBoard(SerializableUpdateActions update) {
        List<SerializableUpdateMove> updateMove = update.getUpdateMove();
        List<SerializableUpdateBuild> updateBuild = update.getUpdateBuild();

        if(updateBuild.isEmpty() == false){
            for (int i = 0; i < updateBuild.size(); i++){
                Position newPosition = updateBuild.get(0).getNewPosition();
                boolean dome = updateBuild.get(0).isDome();
                Platform.runLater(()->{
                    BoardSceneController.updateBuilding(newPosition, dome);
                });
            }
        }

        if(updateMove.isEmpty() == false){
           for (int i = 0; i < updateMove.size(); i++){
               Position newPosition = updateMove.get(i).getNewPosition();
               Position oldPosition = updateMove.get(i).getStartingPosition();
               int playerID = updateMove.get(i).getPlayerId();
               Platform.runLater(()->{
                   BoardSceneController.updateWorker(newPosition,oldPosition,playerID);
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
        Platform.runLater(()->{
            for (int i=0; i < workerPositions.size(); i++){
                BoardSceneController.updateWorkerInitialPosition(workerPositions.get(i),update.getPlayerId());}
        });
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

}
