package it.polimi.ingsw.client;

// CLIENTBOARD -----> CLIENTPLAYER [3] -----------> CLIENTWORKER [2]
//      Ͱ-----------> CLIENTBUILDING [5][5]

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.cli.Terminal;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.server.serializable.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;


public class Client implements ViewObserver {
    private ClientBoard board;
    private View view;
    private ClientCommunicator communicator;
    private int port;
    private String IP;

    public void startClient(int port, String IP, boolean GUI) {
        try {
            this.port = port;
            this.IP = IP;
            if (GUI) view = new GUI(new Textfields());
            else view = new Terminal(new Textfields());
            view.addObserver(this);
            view.displayStartup(); //Questo metodo fa partire la Cli e la Gui (nella cli fa partire l'ASCII Art)
            view.askForStartupInfos();
        } catch (Exception e) {
            e.printStackTrace();
            onError();
        }
    }

    public void onError(){
        view.displayError(0);
    }

    public void onUpdateInitializeWorkerPositions(SerializableUpdateInitializeWorkerPositions object){
        int whichPlayerId = object.getPlayerId();
        Position positionWorker1 = object.getWorkerPositions().get(0);
        Position positionWorker2 = object.getWorkerPositions().get(1);
        //setto i worker dei vari giocatori
        board.getPlayer(whichPlayerId).getWorker(1).setX(positionWorker1.getX());
        board.getPlayer(whichPlayerId).getWorker(1).setY(positionWorker1.getY());
        board.getPlayer(whichPlayerId).getWorker(2).setX(positionWorker2.getX());
        board.getPlayer(whichPlayerId).getWorker(2).setY(positionWorker2.getY());
        view.displayBoard(object);
    }

    public void onUpdateInitializeGodPower(SerializableUpdateInitializeGodPower object){
        String godPower = object.getGodPower();
        int whichPlayerId = object.getPlayerId();
        board.getPlayer(whichPlayerId).setGodPowerName(godPower);
        view.displayGodPower(whichPlayerId);
        if (whichPlayerId==board.getNumOfPlayers()) view.displayBoardScreen();
    }

    public void onRequestInitializeGodPower(SerializableRequestInitializeGodPower object) throws IOException, ParseException {
        List<GodCard> godCards = new ArrayList<>();
        for(String s : object.getGodPowers())
            godCards.add(GodCardsManager.getCard(s));
        view.askForInitialGodPower(godCards);
    }

    public void onRequestInitializeWorkerPositions(){
        view.askForInitialWorkerPositions();
    }

    public void onUpdateDisconnection(SerializableUpdateDisconnection object) throws Exception {
        if ((object).getPlayerId()==board.getMyPlayerId()) throw new Exception();
        view.displayDisconnection((object.getPlayerId()));
        throw new GameEndedException();
    }

    public void onRequestAction(SerializableRequestAction object) {
        view.askForAction(object);
    }

    public void onUpdateWinner(SerializableUpdateWinner object) throws GameEndedException {
        int playerId = object.getPlayerId();
        view.displayWinner(playerId);
        throw new GameEndedException();
    }

    public void onUpdateTurn (SerializableUpdateTurn object){
        board.setPlayerTurnId( object.getPlayerId());
        if (object.getIsFirstTurn()) view.displayGameStart();
        view.displayTurn(object.getPlayerId()); //mostro a schermo che il gicoatore di turno è un altro
    }

    public void onUpdateLoser (SerializableUpdateLoser object){
        int playerId = object.getPlayerId();
        board.getPlayer(playerId).setLost(true);
        view.displayLoser(playerId);
    }


    public void onUpdateAction(SerializableUpdateActions object) {
        for(int i =0; i < object.getUpdateBuild().size(); i++) {
            onUpdateBuild(object.getUpdateBuild().get(i));
        }
        for(int i =0; i < object.getUpdateMove().size(); i++) {
            onUpdateMove(object.getUpdateMove().get(i));
        }
        view.displayBoard(object); //mostro le modifiche a schermo e passo alla GUI tutte le informazioni
    }

    private void onUpdateBuild(SerializableUpdateBuild object){
        boolean isDome;
        int oldLevel, x, y;
        x = object.getNewPosition().getX(); //estraggo informazioni
        y = object.getNewPosition().getY();
        isDome = object.isDome(); //controllo se il player può costruire una cupola a ogni livello
        if (board.getCell(x, y) != null) oldLevel = board.getCell(x, y).getLevel(); //ricavo qual era la z prima di costruire
        else oldLevel = -1;
        board.setCell(new ClientBuilding(oldLevel + 1, isDome), x, y); // costruisco sopra l'ultima casella presente
    }

    private void onUpdateMove(SerializableUpdateMove object){
        int playerId, workerId, x, y;
        playerId = object.getPlayerId(); //estraggo le informazioni
        workerId = object.getWorkerId();
        x = object.getNewPosition().getX();
        y = object.getNewPosition().getY();
        board.getPlayer(playerId).getWorker(workerId).setX(x); //apporto modifiche alla board del client
        board.getPlayer(playerId).getWorker(workerId).setY(y);
    }

    public void onUpdateInitializeNames (SerializableUpdateInitializeNames names){
        for (int id = 1; id <= board.getNumOfPlayers(); id++)
            board.setPlayer(new ClientPlayer(names.getPlayersNames().get(id - 1)), id);//aggiungo i nomi alla board
        view.displayPlayerNames(names); //mostro a schermo i nomi degli altri giocatori
    }

    public void onHello(){
        communicator.sendMessage("HELLO");
    }

    public void onPlayerIdAssigned(String message){
        int playerId = (Character.getNumericValue(message.charAt(message.length()-1))); //creo il playerID
        board.setMyPlayerId(playerId); //creo il playerID
    }

    public void onRestart(int error) throws GameEndedException {
        view.displayError(error);
        view.askForStartupInfos();
        throw new GameEndedException();
    }



    /* ViewObserver methods */

    public void onCompletedStartup (String myName, int numOfPlayers) {
        try {
            communicator = new ClientCommunicator(port, IP);
        }catch (Exception e){
            onError();
            return;
        }
        try{
            board = new ClientBoard(numOfPlayers); //crea una board con 3 player, è copia di quella del model, ma si salva solo le informazioni della caselle con la Z maggiore, quindi al massimo mi pare 25 caselle
            view.setBoard(board); //passa il riferimento alla board creata alla View
            communicator.addObserver(this);
            communicator.start();
            communicator.sendObject(new SerializableConnection(numOfPlayers, myName)); //invio al server le scelte del nome del plauyer
            view.displayWaitingRoom();
        }catch (Exception e){
            e.printStackTrace();
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

    public void onCompletedInitializeGodPower(String chosenGodPower){
        communicator.sendObject(new SerializableInitializeGodPower(chosenGodPower));
    }

    public void onCompletedInitializeWorkerPositions(List<Position> myWorkerPositions){
        communicator.sendObject(new SerializableInitializeWorkerPositions(myWorkerPositions));
    }
}
