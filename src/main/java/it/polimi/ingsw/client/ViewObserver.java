package it.polimi.ingsw.client;

import it.polimi.ingsw.Position;
import java.util.List;

// Contiene i metodi da lanciare una volta che l'utente ha fornito le informazioni necessarie (dopo che ha premuto OK)
// Client è ViewObserver

public interface ViewObserver {
    void onCompletedStartup (String myName, int numOfPlayers);
    void onCompletedRequestInitializeGame(String chosenGodPower, List<Position> myWorkerPositions);
    void onCompletedBuild(Position position, int workerId, boolean isDome);
    void onCompletedMove(Position position, int workerId);
    void onCompletedDecline();
}
