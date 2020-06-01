package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientBoard;
import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.ViewObserver;
import it.polimi.ingsw.client.gui.runnable.BoardSceneRunnable;
import it.polimi.ingsw.client.gui.runnable.ChoosingGodSceneRunnable;
import it.polimi.ingsw.client.gui.runnable.LoginSceneRunnable;
import it.polimi.ingsw.client.gui.runnable.WaitingSceneRunnable;
import it.polimi.ingsw.server.serializable.SerializableRequestAction;
import it.polimi.ingsw.server.serializable.SerializableUpdateInitializeNames;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class GUI implements View {

    GUICache cache = new GUICache();
    private ClientBoard board;
    ChoosingGodSceneRunnable choosingGodSceneRunnable = new ChoosingGodSceneRunnable();

    public void addObserver(ViewObserver observer){
        List<ViewObserver> observerList = cache.getObserverList();
        observerList.add(observer);}

    @Override
    public void displayStartup() {
        new Thread(()-> {
            MainStage.launch(MainStage.class);
        }).start();
    }

    @Override
    public void displayWaitingRoom() {
        Platform.runLater(new WaitingSceneRunnable());
    }

    public void setBoard(ClientBoard board) {
        this.board = board;
    }

    @Override
    public void displayPlayerNames(SerializableUpdateInitializeNames names) {

    }


    @Override
    public void displayTurn() {

    }

    @Override
    public void displayWinner(int playerId) {

    }

    @Override
    public void displayLoser(int playerId) {

    }

    @Override
    public void displayDisconnection(int playerId) {

    }

    @Override
    public void displayBadNameError() {

    }

    @Override
    public void displayError() {

    }

    @Override
    public void displayRequestAction(SerializableRequestAction object) {

    }

    @Override
    public void displayBoard() {
        Platform.runLater(new BoardSceneRunnable());
    }


    @Override
    public void askForAction(SerializableRequestAction object) {

    }

    @Override
    public void askForGodPowerAndWorkersInitialPositions(List<GodCard> godPowers) {
        cache.setGodPowers(godPowers);
        Platform.runLater(choosingGodSceneRunnable);

    }

    @Override
    public void askForStartupInfos() {
        // I can't call Platform.runLater until the JavaFX application has started.
        try {
            MainStage.getLock().take();
        } catch (Exception ignored) {};

        Platform.runLater(new LoginSceneRunnable());

    }
}
