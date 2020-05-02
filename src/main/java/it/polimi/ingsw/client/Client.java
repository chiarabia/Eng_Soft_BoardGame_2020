package it.polimi.ingsw.client;

// CLIENTBOARD -----> CLIENTPLAYER [3] -----------> CLIENTWORKER [2]
//      Ͱ-----------> CLIENTBUILDING [5][5]

import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static ClientBoard board;
    public static Socket socket;
    public final static String serverIP = "127.0.0.1";
    public static int numOfPlayers = 0;
    public static int myPlayerId = 0;
    public static int playerTurnId = 0; // mostra il playerId del giocatore che sta giocando
    public static void startClient() throws Exception {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("name: ");
        String myName = keyboard.next();
        System.out.print("numOfPlayers: ");
        numOfPlayers = keyboard.nextInt();
        board = new ClientBoard(numOfPlayers);

        if (numOfPlayers == 2) setup(myName, 555);
        else setup(myName, 556);

        displayBoard();
        Object object;
        int loserPlayerId, playerId, workerId, oldLevel, x, y, z;
        boolean isDome;
        Position position;

        while (true){
            object = waitForObject();
            if (object instanceof SerializableRequestMove){
                System.out.print("Worker 1 possible moves: ");
                for (Position p: ((SerializableRequestMove) object).getWorker1Moves())
                    System.out.print("(" + p.getX()+", "+p.getY()+", "+", "+p.getZ()+") ");
                System.out.println();
                System.out.print("Worker 2 possible moves: ");
                for (Position p: ((SerializableRequestMove) object).getWorker2Moves())
                    System.out.print("(" + p.getX()+", "+p.getY()+", "+", "+p.getZ()+") ");
                System.out.println();
                System.out.print("worker id: ");
                workerId = keyboard.nextInt();
                System.out.print("x: ");
                x = keyboard.nextInt();
                System.out.print("y: ");
                y = keyboard.nextInt();
                System.out.print("z: ");
                z = keyboard.nextInt();
                position = new Position(x, y, z);
                sendObject(new SerializableConsolidateMove(position, workerId));
            }
            if (object instanceof SerializableRequestBuild){
                System.out.print("Worker 1 possible builds: ");
                for (Position p: ((SerializableRequestBuild) object).getWorker1Builds())
                    System.out.print("(" + p.getX()+", "+p.getY()+", "+", "+p.getZ()+") ");
                System.out.println();
                System.out.print("Worker 2 possible builds: ");
                for (Position p: ((SerializableRequestBuild) object).getWorker2Builds())
                    System.out.print("(" + p.getX()+", "+p.getY()+", "+", "+p.getZ()+") ");
                System.out.println();
                System.out.println("Can force dome: " + ((SerializableRequestBuild) object).isCanForceMove());
                System.out.print("worker id: ");
                workerId = keyboard.nextInt();
                System.out.print("x: ");
                x = keyboard.nextInt();
                System.out.print("y: ");
                y = keyboard.nextInt();
                System.out.print("z: ");
                z = keyboard.nextInt();
                System.out.print("is dome: ");
                isDome = keyboard.nextBoolean();
                position = new Position(x, y, z);
                sendObject(new SerializableConsolidateBuild(position, workerId, isDome));
            }
            if (object instanceof SerializableUpdateMove){
                playerId = ((SerializableUpdateMove) object).getPlayerId();
                workerId = ((SerializableUpdateMove) object).getWorkerId();
                x = ((SerializableUpdateMove) object).getNewPosition().getX();
                y = ((SerializableUpdateMove) object).getNewPosition().getY();
                board.getPlayer(playerId).getWorker(workerId).setX(x);
                board.getPlayer(playerId).getWorker(workerId).setY(y);
                System.out.println("Player " + playerId+ " has moved worker "+workerId+" to ("+ x + ", " + y + ")");
            }
            if (object instanceof SerializableUpdateBuild){
                x = ((SerializableUpdateBuild) object).getNewPosition().getX();
                y = ((SerializableUpdateBuild) object).getNewPosition().getY();
                isDome = ((SerializableUpdateBuild) object).isDome();
                if (board.getCell(x, y)!=null) oldLevel = board.getCell(x,y).getLevel();
                else oldLevel = -1;
                board.setCell(new ClientBuilding(oldLevel +1, isDome), x, y);
                if (!isDome) System.out.println("Building in ("+ x + ", " + y + ")");
                else System.out.println("Dome in ("+ x + ", " + y + ")");
            }
            if (object instanceof SerializableUpdateTurn){
                playerTurnId = ((SerializableUpdateTurn) object).getPlayerId();
                System.out.println("Player "+((SerializableUpdateTurn) object).getPlayerId() + " now playing");
            }
            if (object instanceof SerializableUpdateLoser){
                loserPlayerId = ((SerializableUpdateLoser) object).getPlayerId();
                System.out.println("Player "+loserPlayerId +" has lost");
                if (loserPlayerId == myPlayerId) return;
            }
            if (object instanceof SerializableUpdateWinner){
                System.out.println("Player " + ((SerializableUpdateWinner) object).getPlayerId() + " has won");
                return;
            }
            if (object instanceof SerializableUpdateDisconnection){
                System.out.println("Player " + ((SerializableUpdateDisconnection) object).getPlayerId() + " disconnected");
                return;
            }
        }
    }

    private static void setup (String myName, int serverPort) throws Exception {
        Scanner keyboard = new Scanner(System.in);
        Socket socket = new Socket(serverIP, serverPort);
        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        String message;
        boolean isServerReady = false;
        while (!isServerReady) {
            System.out.println("Waiting for message...");
            message = in.nextLine();
            System.out.println(message + " from Server");
            if (message.equals("Hello")) {
                out.println("Hello");
                out.flush();
            }
            isServerReady = message.equals("Ready");
        }
        message = in.nextLine();
        if (message.equals("Player's name")) {
            System.out.println("Server has asked for name");
            out.println(myName);
        }
        message = in.nextLine();
        if (message.equals("Close")) {
            System.out.println("Server has closed connection");
            throw new Exception();
        } else if (message.equals("Start Serializable")) {
            System.out.println("Server has switched to Serializable");
        }
        SerializableUpdateInitializeInfos infos = (SerializableUpdateInitializeInfos) waitForObject();
        for (int id = 1; id <= numOfPlayers; id++) {
            board.setPlayer(new ClientPlayer(infos.getPlayersNames().get(id - 1), infos.getGodPowersNames().get(id - 1)), id);
            if (infos.getPlayersNames().get(id - 1).equals(myName)) myPlayerId = id;
        }
        System.out.println("You are player " + myPlayerId);

        Object object;
        int whichPlayerId;
        Position positionWorker1, positionWorker2;
        while (true){
            object = waitForObject();
            if (object instanceof SerializableUpdateTurn) break;
            if (object instanceof SerializableUpdateInitializeWorkers){
                displayBoard();
                whichPlayerId = ((SerializableUpdateInitializeWorkers) object).getPlayerId();
                positionWorker1 = ((SerializableUpdateInitializeWorkers) object).getWorkerPositions().get(0);
                positionWorker2 = ((SerializableUpdateInitializeWorkers) object).getWorkerPositions().get(1);
                board.getPlayer(whichPlayerId).getWorker(1).setX(positionWorker1.getX());
                board.getPlayer(whichPlayerId).getWorker(1).setY(positionWorker1.getY());
                board.getPlayer(whichPlayerId).getWorker(2).setX(positionWorker2.getX());
                board.getPlayer(whichPlayerId).getWorker(2).setY(positionWorker2.getY());
            }
            if (object instanceof SerializableRequestInitializeWorkers){
                System.out.print("Worker 1 X: ");
                int myWorker1x = keyboard.nextInt();
                System.out.print("Worker 1 Y: ");
                int myWorker1y = keyboard.nextInt();
                System.out.print("Worker 2 X: ");
                int myWorker2x = keyboard.nextInt();
                System.out.print("Worker 2 Y: ");
                int myWorker2y = keyboard.nextInt();
                List<Position> myWorkerPositions = new ArrayList<>();
                myWorkerPositions.add(new Position(myWorker1x, myWorker1y, 0));
                myWorkerPositions.add(new Position(myWorker2x, myWorker2y, 0));
                sendObject(new SerializableInitializeWorkers(myWorkerPositions));
            }
        }
        playerTurnId = ((SerializableUpdateTurn) object).getPlayerId();
    }
    private static Object waitForObject() throws IOException, ClassNotFoundException {
        ObjectInputStream fileObjectIn = new ObjectInputStream(socket.getInputStream());
        return (Object) fileObjectIn.readObject();
    }
    private static void sendObject (Object object) throws IOException {
        ObjectOutputStream fileObjectOut = new ObjectOutputStream(socket.getOutputStream());
        fileObjectOut.writeObject(object);
        fileObjectOut.flush();
    }
    public static void displayBoard(){
        System.out.print("\n-------------------------\n");
        for (int i = 0; i<5; i++){
            for (int j = 4; j >=0; j--){
                if (board.getCell(i, j)==null) System.out.print("|   |");
                else if (board.getCell(i, j).isDome()) System.out.print("| D |");
                else System.out.print("| "+ board.getCell(i, j).getLevel() + " |");
            }
            System.out.print("\n-------------------------\n");
        }
        System.out.println();
        for (int i = 0; i < numOfPlayers; i++){
            if (!board.getPlayer(i+1).hasLost()) {
                System.out.print("Player " + (i + 1) + ": Worker 1 (" + board.getPlayer(i + 1).getWorker(1).getX() + ", " + board.getPlayer(i + 1).getWorker(1).getY() + ")");
                System.out.print(", Worker 2 (" + board.getPlayer(i + 1).getWorker(2).getX() + ", " + board.getPlayer(i + 1).getWorker(2).getY() + ")");
                System.out.println(", " + board.getPlayer(i + 1).getGodPowerName());
            } else System.out.println("Player " + (i + 1) + " has lost");
        }
    }
}
