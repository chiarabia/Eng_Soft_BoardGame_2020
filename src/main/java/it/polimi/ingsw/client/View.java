package it.polimi.ingsw.client;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.SerializableRequestAction;
import it.polimi.ingsw.server.serializable.SerializableUpdateInitializeNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Metodi che Client invoca su CLI e GUI

public interface View {
    // metodi di servizio
    void setBoard(ClientBoard board);
    void addObserver(ViewObserver observer);

    // metodi di rappresentazione
    void displayStartup(); // Il gioco è stato appena avviato, viene mostrata la schermata iniziale
    void displayWaitingRoom(); // Le informazioni di nome e numero giocatori sono state appena immesse, inizia l'attesa di altri giocatori per l'inizio della partita
    void displayPlayerNames(SerializableUpdateInitializeNames names); // La partita sta per iniziare, sono stati appena forniti i nomi di tutti i giocatori
    void displayRequestAction(SerializableRequestAction object); // Un'azione è stata appena richiesta al giocatore
    void displayBoard(); // Sono state effettuate modifiche alla board che devono ora essere riprodotte graficamente
    void displayTurn(); // Un nuovo turno è appena iniziato
    void displayWinner(int playerId); // Un giocatore ha appena vinto
    void displayLoser(int playerId); // Un giocatore ha appena perso
    void displayDisconnection (int playerId); // Un giocatore si è appena disconnesso

    // metodi di errore
    void displayError(String message); // Deve essere visualizzato un messaggio di errore
    void displayError(); // Deve essere visualizzato il messaggio di errore fatale

    // metodi di richiesta input tastiera/mouse
    void askForAction(SerializableRequestAction object);
    void askForInitialGodPower(List<GodCard> godCards);
    void askForWorkersInitialPositions(String chosenGodPower);
    void askForStartupInfos();
}
