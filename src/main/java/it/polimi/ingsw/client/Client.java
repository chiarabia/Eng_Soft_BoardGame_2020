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
    view.displayStartUp();
    String myName = view.askForName();
    int numOfPlayers = view.askForNumOfPlayers();
    board = new ClientBoard(numOfPlayers);
    view.setBoard(board);
    try {
        setup(myName, IP, port);
        while (true) reactToServer(waitForObject());
    } catch (GameEndedException e) {
    } catch (Exception e) {
        view.displayMessage(Terminal.Color.RED.set() + "Oops... something went wrong");
        e.printStackTrace();
    }
}

private static void reactToServer(Object object) throws Exception {
    boolean isDome;
    Position position;
    int playerId, workerId = 0, oldLevel, x, y;

    if (object instanceof SerializableUpdateMove) {
        playerId = ((SerializableUpdateMove) object).getPlayerId();
        workerId = ((SerializableUpdateMove) object).getWorkerId();
        x = ((SerializableUpdateMove) object).getNewPosition().getX();
        y = ((SerializableUpdateMove) object).getNewPosition().getY();
        board.getPlayer(playerId).getWorker(workerId).setX(x);
        board.getPlayer(playerId).getWorker(workerId).setY(y);
        view.displayBoard();
    }
    if (object instanceof SerializableUpdateBuild) {
        x = ((SerializableUpdateBuild) object).getNewPosition().getX();
        y = ((SerializableUpdateBuild) object).getNewPosition().getY();
        isDome = ((SerializableUpdateBuild) object).isDome();
        if (board.getCell(x, y) != null) oldLevel = board.getCell(x, y).getLevel();
        else oldLevel = -1;
        board.setCell(new ClientBuilding(oldLevel + 1, isDome), x, y);
        view.displayBoard();
    }

    if (object instanceof SerializableRequestAction) {
        SerializableRequestAction receivedObject = (SerializableRequestAction) object;
        boolean move = false;
        boolean build = false;

        view.displayRequestAction(receivedObject);

        if (receivedObject.canDecline()) { //Se il player può terminare il turno
            if (receivedObject.areBuildsEmpty() && receivedObject.areMovesEmpty()) {
                view.displayEndTurn("There are no more moves available. The turn is over.");
                sendObject(new SerializableDeclineLastAction());
                return;
            }
            else if (view.askForDecline("Do you want to decline(y/n)? ")) {
                sendObject(new SerializableDeclineLastAction());
                return;
            }
        }

        if (receivedObject.canWorkerDoAction(1)&&receivedObject.canWorkerDoAction(2)) {
            workerId = view.askForWorker(); //se entrambi i lavoratori possono fare qualche azione chiedo al player;
        }
        else {
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
        else {
            if (receivedObject.areMovesEmpty()) build = true;
            else move = true;
        }

        if (move) {
            if (workerId==1) position = view.askForRightPosition(receivedObject.getWorker1Moves());
            else position = view.askForRightPosition(receivedObject.getWorker2Moves());
            sendObject(new SerializableConsolidateMove(position, workerId));
        } else if (build) {
            if (((SerializableRequestAction) object).isCanForceDome()) isDome = view.askForDome();
            else isDome = false;
            if (workerId==1) position = view.askForRightPosition(receivedObject.getWorker1Builds());
            else position = view.askForRightPosition(receivedObject.getWorker2Builds());
            sendObject(new SerializableConsolidateBuild(position, workerId, isDome));
        }
    }

        if (object instanceof SerializableUpdateTurn) {
            board.setPlayerTurnId(((SerializableUpdateTurn) object).getPlayerId());
            view.displayTurn();
        }
        if (object instanceof SerializableUpdateLoser) {
            playerId = ((SerializableUpdateLoser) object).getPlayerId();
            board.getPlayer(playerId).setLost(true);
            view.displayBoard();
            if (playerId == board.getMyPlayerId()) {
                view.displayMessage("You have lost!");
            } else view.displayMessage(board.getPlayer(playerId).getPlayerName() + " has lost");
        }
        if (object instanceof SerializableUpdateWinner) {
            playerId = ((SerializableUpdateWinner) object).getPlayerId();
            if (playerId == board.getMyPlayerId()) view.displayMessage("You have won!");
            else
                view.displayMessage(board.getPlayer(((SerializableUpdateWinner) object).getPlayerId()).getPlayerName() + " has won");
            throw new GameEndedException();
        }
        if (object instanceof SerializableUpdateDisconnection) {
            view.displayMessage(board.getPlayer(((SerializableUpdateDisconnection) object).getPlayerId()).getPlayerName() + " disconnected");
            throw new GameEndedException();
        }
    }

    private static void setup (String myName, String serverIP,int serverPort) throws Exception {
        serverSocket = new Socket(serverIP, serverPort);
        sendMessage(board.numOfPlayers() + " players");
        String message = "";
        while (message.equals("Player's name") || message.equals("")) {
            message = waitForMessage();
            if (message.equals("Player's name")) sendMessage(myName);
        }
        board.setMyPlayerId(Character.getNumericValue(message.charAt(15)));
        SerializableUpdateInitializeNames names = (SerializableUpdateInitializeNames) waitForObject();
        for (int id = 1; id <= board.numOfPlayers(); id++)
            board.setPlayer(new ClientPlayer(names.getPlayersNames().get(id - 1)), id);
        view.displayPlayerNames(names);
        while (true) {
            Object object = waitForObject();
            if (object instanceof SerializableUpdateTurn) {
                board.setPlayerTurnId(((SerializableUpdateTurn) object).getPlayerId());
                view.displayTurn();
                return;
            }
            if (object instanceof SerializableUpdateInitializeGame) {
                String godPower = ((SerializableUpdateInitializeGame) object).getGodPower();
                int whichPlayerId = ((SerializableUpdateInitializeGame) object).getPlayerId();
                Position positionWorker1 = ((SerializableUpdateInitializeGame) object).getWorkerPositions().get(0);
                Position positionWorker2 = ((SerializableUpdateInitializeGame) object).getWorkerPositions().get(1);
                board.getPlayer(whichPlayerId).setGodPowerName(godPower);
                board.getPlayer(whichPlayerId).getWorker(1).setX(positionWorker1.getX());
                board.getPlayer(whichPlayerId).getWorker(1).setY(positionWorker1.getY());
                board.getPlayer(whichPlayerId).getWorker(2).setX(positionWorker2.getX());
                board.getPlayer(whichPlayerId).getWorker(2).setY(positionWorker2.getY());
                view.displayBoard();
            }
            if (object instanceof SerializableRequestInitializeGame) {
                String chosenGodPower = view.askForGodPower(((SerializableRequestInitializeGame) object).getGodPowers());
                List<Position> myWorkerPositions = view.askForWorkersInitialPositions();
                sendObject(new SerializableInitializeGame(myWorkerPositions, chosenGodPower));
            }
            if (object instanceof SerializableUpdateDisconnection) {
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
