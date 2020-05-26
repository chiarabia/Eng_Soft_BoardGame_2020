package it.polimi.ingsw.client;

// CLIENTBOARD -----> CLIENTPLAYER [3] -----------> CLIENTWORKER [2]
//      Ͱ-----------> CLIENTBUILDING [5][5]

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.cli.Terminal;
import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.server.serializable.*;
import java.io.IOException;
import java.util.*;


public class Client implements ViewObserver {
    private ClientBoard board;
    private View view;
    private ClientCommunicator communicator;
    private int port;
    private String IP;

    public void startClient(int port, String IP) {
        try {
            this.port = port;
            this.IP = IP;
            view = new Terminal();
            view.addObserver(this);
            view.displayStartUp(); //Questo metodo fa partire la Cli e la Gui (nella cli fa partire l'ASCII Art)
            view.askForStartupInfos();
        } catch (Exception e) {
            onError();
            communicator.stopProcess();
        }
    }

    public void onError(){
        view.displayMessage(Terminal.Color.RED.set() + "Oops... something went wrong");
    }

    public void onUpdateInitializeGame(SerializableUpdateInitializeGame object){
        String godPower = object.getGodPower();
        int whichPlayerId = object.getPlayerId();
        Position positionWorker1 = object.getWorkerPositions().get(0);
        Position positionWorker2 = object.getWorkerPositions().get(1);
        board.getPlayer(whichPlayerId).setGodPowerName(godPower);
        //setto i worker dei vari giocatori
        board.getPlayer(whichPlayerId).getWorker(1).setX(positionWorker1.getX());
        board.getPlayer(whichPlayerId).getWorker(1).setY(positionWorker1.getY());
        board.getPlayer(whichPlayerId).getWorker(2).setX(positionWorker2.getX());
        board.getPlayer(whichPlayerId).getWorker(2).setY(positionWorker2.getY());
        view.displayBoard();
    }

    public void onRequestInitializeGame(SerializableRequestInitializeGame object) throws IOException {
        view.askForGodPowerAndWorkersInitialPositions(object.getGodPowers());
    }

    public void onUpdateDisconnection(SerializableUpdateDisconnection object) throws GameEndedException {
        view.displayMessage(board.getPlayer(object.getPlayerId()).getPlayerName() + " disconnected");
        throw new GameEndedException();
    }

    public void onRequestAction(SerializableRequestAction object) {
        view.askForAction(object);
    }

    public void onUpdateWinner(SerializableUpdateWinner object) throws GameEndedException {
        int playerId;
        playerId = object.getPlayerId();
        if (playerId == board.getMyPlayerId()) view.displayMessage("You have won!");
        else view.displayMessage(board.getPlayer(object.getPlayerId()).getPlayerName() + " has won");
        throw new GameEndedException();
    }

    public void onUpdateTurn (SerializableUpdateTurn object){
        board.setPlayerTurnId( object.getPlayerId());
        view.displayTurn(); //mostro a schermo che il gicoatore di turno è un altro
    }

    public void onUpdateLoser (SerializableUpdateLoser object){
        int playerId;
        playerId = object.getPlayerId();
        board.getPlayer(playerId).setLost(true); //setto a null le informazioni del giocatore
        view.displayBoard(); //mostro la board, senza i worker del giocatore che ha perso
        if (playerId == board.getMyPlayerId()) {
            view.displayMessage("You have lost!"); //se sono io quel gicoatore. ho perso :D
        } else view.displayMessage(board.getPlayer(playerId).getPlayerName() + " has lost");
        //se non sono io, mi arriva il messaggio che un altro giocatore ha perso
    }

    public void onUpdateBuild(SerializableUpdateBuild object){
        boolean isDome;
        int oldLevel, x, y;
        x = object.getNewPosition().getX(); //estraggo informazioni
        y = object.getNewPosition().getY();
        isDome = object.isDome(); //controllo se il player può costruire una cupola a ogni livello
        if (board.getCell(x, y) != null) oldLevel = board.getCell(x, y).getLevel(); //ricavo qual era la z prima di costruire
        else oldLevel = -1;
        board.setCell(new ClientBuilding(oldLevel + 1, isDome), x, y); // costruisco sopra l'ultima casella presente
        view.displayBoard(); //mostro le modifiche a schermo
    }

    public void onUpdateMove(SerializableUpdateMove object){
        int playerId, workerId, x, y;
        playerId = object.getPlayerId(); //estraggo le informazioni
        workerId = object.getWorkerId();
        x = object.getNewPosition().getX();
        y = object.getNewPosition().getY();
        board.getPlayer(playerId).getWorker(workerId).setX(x); //apporto modifiche alla board del client
        board.getPlayer(playerId).getWorker(workerId).setY(y);
        view.displayBoard(); //mostro le modifiche a schermo (credo proprio che per la GUI serva un metodo che dica quali sono le caselle di
        //partenza e arrivo
    }



    // metodi da lanciare una volta che l'utente ha fornito le informazioni necessarie (dopo che ha premuto OK)

    public void onCompletedStartup (String myName, int numOfPlayers) {
        try {
            board = new ClientBoard(numOfPlayers); //crea una board con 3 player, è copia di quella del model, ma si salva solo le informazioni della caselle con la Z maggiore, quindi al massimo mi pare 25 caselle
            view.setBoard(board); //passa il riferimento alla board creata alla View
            communicator = new ClientCommunicator(port, IP);
            communicator.addObserver(this);
            communicator.sendMessage(board.numOfPlayers() + " players"); //invio al server le scelte del nome del plauyer
            String message = "";
            while (message.equals("Player's name") || message.equals("")) {
                message = communicator.waitForMessage();
                if (message.equals("Player's name")) communicator.sendMessage(myName);
            }
            board.setMyPlayerId(Character.getNumericValue(message.charAt(15))); //creo il playerID
            SerializableUpdateInitializeNames names = (SerializableUpdateInitializeNames) communicator.waitForObject();
            //aspetto i nomi degli altri giocatori
            for (int id = 1; id <= board.numOfPlayers(); id++)
                board.setPlayer(new ClientPlayer(names.getPlayersNames().get(id - 1)), id);
            //aggiungo i nomi alla board
            view.displayPlayerNames(names); //mostro a schermo i nomi degli altri giocatori
            communicator.start();
        }catch (Exception e){
            onError();
            communicator.stopProcess();
        }
    }

    public void onCompletedBuild(Position position, int workerId, boolean isDome){
        communicator.sendObject(new SerializableConsolidateBuild(position, workerId, isDome));
    }

    public void onCompletedMove(Position position, int workerId){
        communicator.sendObject(new SerializableConsolidateMove(position, workerId));
    }

    public void onCompletedDecline(){
        communicator.sendObject(new SerializableDeclineLastAction());
    }

    public void onCompletedRequestInitializeGame(String chosenGodPower, List<Position> myWorkerPositions){
        communicator.sendObject(new SerializableInitializeGame(myWorkerPositions, chosenGodPower));
    }

}
