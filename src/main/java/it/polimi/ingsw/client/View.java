package it.polimi.ingsw.client;

import it.polimi.ingsw.server.serializable.*;

import java.util.List;

// Metodi che Client invoca su CLI e GUI

public interface View {
    // metodi di servizio
    void setBoard(ClientBoard board);
    void addObserver(ViewObserver observer);

    // metodi di rappresentazione
    void displayStartup(); // Il gioco è stato appena avviato, viene mostrata la schermata iniziale
    void displayWaitingRoom(); // Le informazioni di nome e numero giocatori sono state appena immesse, inizia l'attesa di altri giocatori per l'inizio della partita
    void displayPlayerNames(SerializableUpdateInitializeNames names); // La partita sta per iniziare, sono stati appena forniti i nomi di tutti i giocatori. Qui si attiva la schermata dei GodPowers
    void displayGodPower(int playerId); // Un player ha appena scelto il proprio god power
    void displayBoardScreen(); // Sono stati appena scelti tutti i god powers, ora partono le richieste di posizioni iniziali dei workers ai players
    void displayGameStart(); // La partita ha ora inizio
    void displayRequestAction(SerializableRequestAction object); // Un'azione è stata appena richiesta al giocatore
    void displayBoard(SerializableUpdateActions update); // Sono state effettuate modifiche alla board che devono ora essere riprodotte graficamente
    void displayBoard(SerializableUpdateInitializeWorkerPositions update); // Sono stati aggiunti dei workers alla board
    void displayTurn(int playerId); // Un nuovo turno è appena iniziato
    void displayWinner(int playerId); // Un giocatore ha appena vinto
    void displayLoser(int playerId); // Un giocatore ha appena perso
    void displayError(int errorId); // Deve essere visualizzato un messaggio di errore
    void displayDisconnection (int playerId); // Un giocatore si è appena disconnesso

    // metodi di richiesta input tastiera/mouse
    void askForAction(SerializableRequestAction object);
    void askForInitialGodPower(List<GodCard> godCards);
    void askForInitialWorkerPositions();
    void askForStartupInfos();
}
