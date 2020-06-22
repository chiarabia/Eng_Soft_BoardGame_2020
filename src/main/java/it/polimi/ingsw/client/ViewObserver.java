package it.polimi.ingsw.client;

import it.polimi.ingsw.Position;

import java.util.List;

/** This interface represents client methods callable by view when information is received by the user */

public interface ViewObserver {
    void onCompletedStartup (String myName, int numOfPlayers); //Il giocatore ha appena scelto nome e numero giocatori
    void onCompletedInitializeGodPower(String chosenGodPower); // Il giocatore ha appena scelto la divinità
    void onCompletedInitializeWorkerPositions(List<Position> myWorkerPositions); // Il giocatore ha appena scelto anche le posizioni iniziali
    void onCompletedBuild(Position position, int workerId, boolean isDome); // Il giocatore ha appena scelto di costruire in una posizione
    void onCompletedMove(Position position, int workerId); // Il giocatore ha appena scelto di muovere in una posizione
    void onCompletedDecline(); // Il giocatore ha appena scelto di rifiutare un'azione opzionale
}
