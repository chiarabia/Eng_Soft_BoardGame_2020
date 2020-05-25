package it.polimi.ingsw.client;

// CLIENTBOARD -----> CLIENTPLAYER [3] -----------> CLIENTWORKER [2]
//      Ͱ-----------> CLIENTBUILDING [5][5]

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.CLI.Terminal;
import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.server.serializable.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Client {
private static ClientBoard board;
private static Socket serverSocket;
private static View view = new Terminal();

public static void startClient(int port, String IP) {
    view.displayStartUp(); //Questo metodo fa partire la Cli e la Gui (nella cli fa partire l'ASCII Art)
    String myName = view.askForName(); //Chiede il nome del player
    int numOfPlayers = view.askForNumOfPlayers(); //Chiede quanti giocatori
    //Nella Gui, direi di metterci in attesa (nel metodo ASKforName) che il player risponda, (sono due chiamate per il client, quindi quando facciamo
    // OK nella Gui, ci salviamo anche il numero di player e li restituiamo alla seconda chiamata

    board = new ClientBoard(numOfPlayers); //crea una board con 3 player, è copia di quella del model, ma si salva solo le informazioni della caselle con la Z maggiore
    //quindi al massimo mi pare 25 caselle
    view.setBoard(board); //passa il riferimento alla board creata alla View
    try {
        setup(myName, IP, port); //instaura la connessione
        while (true) reactToServer(waitForObject()); //si mette in attessa di messagi dal server
    } catch (GameEndedException e) {
    } catch (Exception e) {
        view.displayMessage(Terminal.Color.RED.set() + "Oops... something went wrong");
        e.printStackTrace();
    }
}

//metodo principale, gestisce tutti messaggi col server e invia le risposte al server
private static void reactToServer(Object object) throws Exception {
    boolean isDome;
    Position position;
    int playerId, workerId = 0, oldLevel, x, y;

    //messaggio che ricevo dopo aver consolidato una move
    if (object instanceof SerializableUpdateMove) {
        playerId = ((SerializableUpdateMove) object).getPlayerId(); //estraggo le informazioni
        workerId = ((SerializableUpdateMove) object).getWorkerId();
        x = ((SerializableUpdateMove) object).getNewPosition().getX();
        y = ((SerializableUpdateMove) object).getNewPosition().getY();
        board.getPlayer(playerId).getWorker(workerId).setX(x); //apporto modifiche alla board del client
        board.getPlayer(playerId).getWorker(workerId).setY(y);
        view.displayBoard(); //mostro le modifiche a schermo (credo proprio che per la GUI serva un metodo che dica quali sono le caselle di
        //partenza e arrivo

    }

    //messaggio che ricevo dopo aver consolidato una build
    if (object instanceof SerializableUpdateBuild) {
        x = ((SerializableUpdateBuild) object).getNewPosition().getX(); //estraggo informazioni
        y = ((SerializableUpdateBuild) object).getNewPosition().getY();
        isDome = ((SerializableUpdateBuild) object).isDome(); //controllo se il player può costruire una cupola a ogni livello
        if (board.getCell(x, y) != null) oldLevel = board.getCell(x, y).getLevel(); //ricavo qual era la z prima di costruire
        else oldLevel = -1;
        board.setCell(new ClientBuilding(oldLevel + 1, isDome), x, y); // costruisco sopra l'ultima casella presente
        view.displayBoard(); //mostro le modifiche a schermo
    }

    //in questo messaggio sono contenute le informazioni sulle mosse disponibili per entrambi i workers
    //le informazioni riguardo all'opzionalità delle suddette azioni, se il player è in condizioni di passare il turno oppure no
    if (object instanceof SerializableRequestAction) {
        SerializableRequestAction receivedObject = (SerializableRequestAction) object;
        boolean move = false; //questi boolean contengono le intenzioni del player
        //se il player decide di muoversi, move diventa true.
        //se il payer non ha scelta, non gli viene permesso di scegliere.
        boolean build = false;

        //mostra al player tutte le informazioni sulle mosse che i suoi worker possono eseguire
        view.displayRequestAction(receivedObject);

        if (receivedObject.canDecline()) { //Se il player può terminare il turno
            if (receivedObject.areBuildsEmpty() && receivedObject.areMovesEmpty()) {
                view.displayEndTurn("There are no more moves available. The turn is over.");
                sendObject(new SerializableDeclineLastAction()); //questo oggetto passa il turno
                return;
            }
            //chiedo al player se vuole terminare il turno
            else if (view.askForDecline("Do you want to decline(y/n)? ")) {
                sendObject(new SerializableDeclineLastAction());
                return;
            }
        }

        //chiedo quale lavoratore il player voglia usare solo se entrambi possono muoversi
        if (receivedObject.canWorkerDoAction(1)&&receivedObject.canWorkerDoAction(2)) {
            workerId = view.askForWorker(); //se entrambi i lavoratori possono fare qualche azione chiedo al player;
        }
        else { //se no capisco io al posto del player qual è l'unico worker che può compiere un'azione
            int i;
            for(i = 1; i<=2; i++) {
                if (receivedObject.canWorkerDoAction(i)) {//in caso contrario non ho bisogno di chiedere al player
                    workerId = i;
                }
            }
        }


        if (!receivedObject.areMovesEmpty()&&!receivedObject.areBuildsEmpty()) { //Se il lavoratore può sia muoversi che costruire chiedo al player
            while (!move && !build) {
                if (view.askForDecision("move")) move = true;
                if (view.askForDecision("build")) build = true;
            }
        }
        else { //se no scelgo per lui
            if (receivedObject.areMovesEmpty()) build = true;
            else move = true;
        }

        if (move) { //se posso solo muovermi  o il player ha scleto di muovermi
            if (workerId==1) position = view.askForRightPosition(receivedObject.getWorker1Moves()); //chiedo al player la posizione
            else position = view.askForRightPosition(receivedObject.getWorker2Moves());
            sendObject(new SerializableConsolidateMove(position, workerId));
        } else if (build) {
            if (((SerializableRequestAction) object).isCanForceDome()) isDome = view.askForDome(); //chiedo al player se vuole costruire una cupola
            //a qualsiasi livello, solo se può farlo con il potere della sua divinità
            else isDome = false;
            if (workerId==1) position = view.askForRightPosition(receivedObject.getWorker1Builds()); //chiedo dove il player voglia costruire
            else position = view.askForRightPosition(receivedObject.getWorker2Builds());
            sendObject(new SerializableConsolidateBuild(position, workerId, isDome));
        }
    }
        //messaggio che ricevo quando cambio il turno
        if (object instanceof SerializableUpdateTurn) {
            board.setPlayerTurnId(((SerializableUpdateTurn) object).getPlayerId());
            view.displayTurn(); //mostro a schermo che il gicoatore di turno è un altro
        }

        if (object instanceof SerializableUpdateLoser) { //messaggio che mosra che un gicoatore ah perso
            playerId = ((SerializableUpdateLoser) object).getPlayerId();
            board.getPlayer(playerId).setLost(true); //setto a null le informazioni del giocatore
            view.displayBoard(); //mostro la board, senza i worker del giocatore che ha perso
            if (playerId == board.getMyPlayerId()) {
                view.displayMessage("You have lost!"); //se sono io quel gicoatore. ho perso :D
            } else view.displayMessage(board.getPlayer(playerId).getPlayerName() + " has lost");
            //se non sono io, mi arriva il messaggio che un altro giocatore ha perso
        }

        if (object instanceof SerializableUpdateWinner) { //messagio dopo vincita
            playerId = ((SerializableUpdateWinner) object).getPlayerId();
            if (playerId == board.getMyPlayerId()) view.displayMessage("You have won!");
            else
                view.displayMessage(board.getPlayer(((SerializableUpdateWinner) object).getPlayerId()).getPlayerName() + " has won");
            throw new GameEndedException();
        }
        //messaggio se salta la connessione
        if (object instanceof SerializableUpdateDisconnection) {
            view.displayMessage(board.getPlayer(((SerializableUpdateDisconnection) object).getPlayerId()).getPlayerName() + " disconnected");
            throw new GameEndedException();
        }
    }

    //invita al server le informazioni
    private static void setup (String myName, String serverIP,int serverPort) throws Exception {
        serverSocket = new Socket(serverIP, serverPort);
        sendMessage(board.numOfPlayers() + " players"); //invio al server le scelte del nome del plauyer
        String message = "";
        while (message.equals("Player's name") || message.equals("")) {
            message = waitForMessage();
            if (message.equals("Player's name")) sendMessage(myName);
        }
        board.setMyPlayerId(Character.getNumericValue(message.charAt(15))); //creo il playerID
        SerializableUpdateInitializeNames names = (SerializableUpdateInitializeNames) waitForObject();
        //aspetto i nomi degli altri giocatori
        for (int id = 1; id <= board.numOfPlayers(); id++)
            board.setPlayer(new ClientPlayer(names.getPlayersNames().get(id - 1)), id);
        //aggiungo i nomi alla board
        view.displayPlayerNames(names); //mostro a schermo i nomi degli altri giocatori
        while (true) {
            Object object = waitForObject();
            if (object instanceof SerializableUpdateTurn) { //ricevo il primo SerialiableTUrn, che comunica chi è il primo plauer ha scegliere le divinità
                board.setPlayerTurnId(((SerializableUpdateTurn) object).getPlayerId());
                view.displayTurn();
                return;
            }
            if (object instanceof SerializableUpdateInitializeGame) { //ricevo un oggetto contente i GodPower dei vari giocatori che vengono aggiunti alla board
                String godPower = ((SerializableUpdateInitializeGame) object).getGodPower();
                int whichPlayerId = ((SerializableUpdateInitializeGame) object).getPlayerId();
                Position positionWorker1 = ((SerializableUpdateInitializeGame) object).getWorkerPositions().get(0);
                Position positionWorker2 = ((SerializableUpdateInitializeGame) object).getWorkerPositions().get(1);
                board.getPlayer(whichPlayerId).setGodPowerName(godPower);
                //setto i worker dei vari giocatori
                board.getPlayer(whichPlayerId).getWorker(1).setX(positionWorker1.getX());
                board.getPlayer(whichPlayerId).getWorker(1).setY(positionWorker1.getY());
                board.getPlayer(whichPlayerId).getWorker(2).setX(positionWorker2.getX());
                board.getPlayer(whichPlayerId).getWorker(2).setY(positionWorker2.getY());
                view.displayBoard();
            }
            if (object instanceof SerializableRequestInitializeGame) { // chiedo quale GodPower il plauer voglia scegleire
                String chosenGodPower = view.askForGodPower(((SerializableRequestInitializeGame) object).getGodPowers());
                List<Position> myWorkerPositions = view.askForWorkersInitialPositions();
                sendObject(new SerializableInitializeGame(myWorkerPositions, chosenGodPower));
            }
            if (object instanceof SerializableUpdateDisconnection) { //disconnessione
                view.displayMessage(board.getPlayer(((SerializableUpdateDisconnection) object).getPlayerId()).getPlayerName() + " disconnected");
                throw new GameEndedException();
            }
        }
    }

    private static Object waitForObject () throws IOException, ClassNotFoundException {
        ObjectInputStream fileObjectIn = new ObjectInputStream(serverSocket.getInputStream());
        return fileObjectIn.readObject();
    }

    private static void sendObject (Object object) throws IOException {
        ObjectOutputStream fileObjectOut = new ObjectOutputStream(serverSocket.getOutputStream());
        fileObjectOut.writeObject(object);
        fileObjectOut.flush();
    }

    private static void sendMessage (String message) throws IOException {
        sendObject(new Message(message));
    }

    private static String waitForMessage () throws IOException, ClassNotFoundException {
        return ((Message) waitForObject()).getMessage();
    }
}
