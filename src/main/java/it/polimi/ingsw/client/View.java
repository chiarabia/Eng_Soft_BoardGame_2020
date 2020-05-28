package it.polimi.ingsw.client;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.SerializableRequestAction;
import it.polimi.ingsw.server.serializable.SerializableUpdateInitializeNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface View {
    // metodi di servizio
    void setBoard(ClientBoard board);
    void addObserver(ViewObserver observer);

    // metodi di rappresentazione
    void displayPlayerNames(SerializableUpdateInitializeNames names);
    void displayMessage(String message);
    void displayErrorMessage();
    void displayStartUp ();
    void displayCells (Set< Position > positions);
    void displayTurn();
    void displayRequestAction(SerializableRequestAction object);
    void displayBoard();
    void displayEndTurn(String request);

    // metodi di richiesta input tastiera/mouse
    void askForAction(SerializableRequestAction object);
    void askForGodPowerAndWorkersInitialPositions(List<GodCard> godCards);
    void askForStartupInfos();
}
