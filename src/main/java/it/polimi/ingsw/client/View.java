package it.polimi.ingsw.client;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.SerializableRequestAction;
import it.polimi.ingsw.server.serializable.SerializableUpdateInitializeNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface View {

    void setBoard(ClientBoard board);

    void displayPlayerNames(SerializableUpdateInitializeNames names);
    void displayMessage(String string);
    void displayStartUp ();
    void displayCells (Set< Position > positions);
    void displayTurn();
    void displayRequestAction(SerializableRequestAction object);
    void displayBoard();
    void displayEndTurn(String request);

    boolean askForDome();
    String askForGodPower (List<String> godPowers);
    List <Position> askForWorkersInitialPositions();
    Position askForPosition();
    Position askForRightPosition (Set<Position> positions);
    int askForWorker();
    boolean askForDecision(String action); //se il player ha più di una opzione, quale sceglie?
    String askForName();
    int askForNumOfPlayers();
    boolean askForDecline(String request);

    enum Color{
        RESET("\u001B[0m"),
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m");
        private String set;

        Color(String set) {
            this.set = set;
        }

        public String set() {
            return set;
        }
    }

    /*public void updateAfterBuild (); // il worker ha costrito etc...

    public void updateAfterMove (); //il player si è mosso

    public void communicateWhatActionIsOptional();*/
}
