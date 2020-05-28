package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.ClientBoard;
import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.ViewObserver;
import it.polimi.ingsw.server.serializable.SerializableRequestAction;
import it.polimi.ingsw.server.serializable.SerializableUpdateInitializeNames;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GUI implements View {

    GUICache cache = new GUICache();
    private ClientBoard board;
    private List<ViewObserver> observerList = new ArrayList<>();
    ChoosingGodSceneRunnable ChoosingGodSceneRunnable = new ChoosingGodSceneRunnable();

    public void addObserver(ViewObserver observer){observerList.add(observer);}
    public void setBoard(ClientBoard board) {
        this.board = board;
    }

    @Override
    public void displayPlayerNames(SerializableUpdateInitializeNames names) {

    }

    @Override
    public void displayMessage(String message) {

    }

    @Override
    public void displayErrorMessage() {

    }

    @Override
    public void displayStartUp() {
        MainStage.launch();
    }

    @Override
    public void displayCells(Set<Position> positions) {

    }

    @Override
    public void displayTurn() {

    }

    @Override
    public void displayRequestAction(SerializableRequestAction object) {

    }

    @Override
    public void displayBoard() {

    }

    @Override
    public void displayEndTurn(String request) {

    }

    @Override
    public void askForAction(SerializableRequestAction object) {

    }

    @Override
    public void askForGodPowerAndWorkersInitialPositions(List<GodCard> godPowers) {
        cache.setGodPowers(godPowers);
        Platform.runLater(ChoosingGodSceneRunnable);

    }

    @Override
    public void askForStartupInfos() {
        int numberOfPlayers = cache.getNumberOfPlayers();
        String playersName = cache.getPlayerName();
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedStartup(playersName, numberOfPlayers);
    }
}
