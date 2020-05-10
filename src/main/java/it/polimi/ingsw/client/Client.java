package it.polimi.ingsw.client;

// CLIENTBOARD -----> CLIENTPLAYER [3] -----------> CLIENTWORKER [2]
//      Ͱ-----------> CLIENTBUILDING [5][5]

// Questo codice è interamente work in progress, soprattutto nei metodi grafico-testuali

import it.polimi.ingsw.Position;
import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.server.serializable.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class Client {
    private static String serverIP;
    private static int serverPort;
    private static ClientBoard board;
    private static Socket serverSocket;
    private static int numOfPlayers = 0;
    private static int myPlayerId = 0;
    private static int playerTurnId = 0; // mostra il playerId del giocatore che sta giocando
    private static Scanner keyboard = new Scanner(System.in);

    public static void startClient(int port, String IP) {
        serverPort = port;
        serverIP = IP;
        String myName = askForInfos();
        board = new ClientBoard(numOfPlayers);
        try{
            setup(myName);
            while (true) reactToServer(waitForObject());
        } catch (GameEndedException e){}
        catch (Exception e){System.out.println(Color.RED.set() + "Oops... something went wrong");}
    }

    private static void reactToServer(Object object) throws Exception {
        boolean isDome;
        int playerId = 0, workerId = 0, oldLevel, x, y, z;
        if (object instanceof SerializableRequestMove){
            if (((SerializableRequestMove) object).getWorker1Moves().size()!=0) {
                workerId = 1;
                System.out.print("Worker 1 possible moves: ");
                for (Position p : ((SerializableRequestMove) object).getWorker1Moves())
                    System.out.print("(" + p.getX() + ", " + p.getY() + ", " + p.getZ() + ") ");
                System.out.println();
            }
            if (((SerializableRequestMove) object).getWorker2Moves().size()!=0) {
                workerId = 2;
                System.out.print("Worker 2 possible moves: ");
                for (Position p : ((SerializableRequestMove) object).getWorker2Moves())
                    System.out.print("(" + p.getX() + ", " + p.getY() + ", " + p.getZ() + ") ");
                System.out.println();
            }
            if (((SerializableRequestMove) object).getWorker1Moves().size()!=0 && ((SerializableRequestMove) object).getWorker2Moves().size()!=0 ) workerId = 0;
            if (workerId == 0) workerId = askForInt("worker id:");
            Position position = null;
            while (!isPositionCorrect(position, ((SerializableRequestMove) object).getWorker1Moves()) && !isPositionCorrect(position, ((SerializableRequestMove) object).getWorker2Moves()))
                position = askForPosition();
            sendObject(new SerializableConsolidateMove(position, workerId));
        }
        if (object instanceof SerializableRequestBuild){
            if (((SerializableRequestBuild) object).getWorker1Builds().size()!=0) {
                workerId = 1;
                System.out.print("Worker 1 possible builds: ");
                for (Position p : ((SerializableRequestBuild) object).getWorker1Builds())
                    System.out.print("(" + p.getX() + ", " + p.getY() + ", " + p.getZ() + ") ");
                System.out.println();
            }
            if (((SerializableRequestBuild) object).getWorker2Builds().size()!=0) {
                workerId = 2;
                System.out.print("Worker 2 possible builds: ");
                for (Position p : ((SerializableRequestBuild) object).getWorker2Builds())
                    System.out.print("(" + p.getX() + ", " + p.getY() + ", " + p.getZ() + ") ");
                System.out.println();
            }
            if (((SerializableRequestBuild) object).getWorker1Builds().size()!=0 && ((SerializableRequestBuild) object).getWorker2Builds().size()!=0) workerId = 0;
            if (workerId == 0) workerId = askForInt("worker id:");
            if (((SerializableRequestBuild) object).isCanForceMove()) isDome = askForBoolean("is dome (y/n): ");
            else isDome = false;
            Position position = null;
            while (!isPositionCorrect(position, ((SerializableRequestBuild) object).getWorker1Builds()) && !isPositionCorrect(position, ((SerializableRequestBuild) object).getWorker2Builds()) )
                position = askForPosition();
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
            playerId = ((SerializableUpdateLoser) object).getPlayerId();
            System.out.println("Player "+playerId +" has lost");
            if (playerId == myPlayerId) return;
        }
        if (object instanceof SerializableUpdateWinner){
            System.out.println("Player " + ((SerializableUpdateWinner) object).getPlayerId() + " has won");
            throw new GameEndedException();
        }
        if (object instanceof SerializableUpdateDisconnection){
            System.out.println("Player " + ((SerializableUpdateDisconnection) object).getPlayerId() + " disconnected");
            throw new GameEndedException();
        }
        displayBoard();
    }

    private static void setup (String myName) throws Exception {
        serverSocket = new Socket(serverIP, serverPort);
        sendMessage(numOfPlayers + " players");
        String message = "";
        while (message.equals("Player's name") || message.equals("")) {
            message = waitForMessage();
            if (message.equals("Player's name")) sendMessage(myName);
        }
        if (message.substring(0, 15).equals("You are player ")) {
            myPlayerId = Character.getNumericValue(message.charAt(15));
            System.out.println("You are player " + myPlayerId);
        }
        SerializableUpdateInitializeNames names = (SerializableUpdateInitializeNames) waitForObject();
        for (int id = 1; id <= numOfPlayers; id++)
            board.setPlayer(new ClientPlayer(names.getPlayersNames().get(id - 1)), id);
        Object object;
        String godPower;
        int whichPlayerId;
        Position positionWorker1, positionWorker2;
        while (true){
            object = waitForObject();
            if (object instanceof SerializableUpdateTurn) break;
            if (object instanceof SerializableUpdateInitializeGame){
                godPower = ((SerializableUpdateInitializeGame) object).getGodPower();
                whichPlayerId = ((SerializableUpdateInitializeGame) object).getPlayerId();
                positionWorker1 = ((SerializableUpdateInitializeGame) object).getWorkerPositions().get(0);
                positionWorker2 = ((SerializableUpdateInitializeGame) object).getWorkerPositions().get(1);
                board.getPlayer(whichPlayerId).setGodPowerName(godPower);
                board.getPlayer(whichPlayerId).getWorker(1).setX(positionWorker1.getX());
                board.getPlayer(whichPlayerId).getWorker(1).setY(positionWorker1.getY());
                board.getPlayer(whichPlayerId).getWorker(2).setX(positionWorker2.getX());
                board.getPlayer(whichPlayerId).getWorker(2).setY(positionWorker2.getY());
                displayBoard();
            }
            if (object instanceof SerializableRequestInitializeGame){
                String chosenGodPower = askForGodPower(((SerializableRequestInitializeGame) object).getGodPowers());
                List <Position> myWorkerPositions = askForWorkersInitialPositions();
                sendObject(new SerializableInitializeGame(myWorkerPositions, chosenGodPower));
            }
            if (object instanceof SerializableUpdateDisconnection){
                System.out.println("Player "+ ((SerializableUpdateDisconnection) object).getPlayerId() + " disconnected");
                throw new GameEndedException();
            }
        }
        playerTurnId = ((SerializableUpdateTurn) object).getPlayerId();
        displayBoard();
    }

    private static Object waitForObject() throws IOException, ClassNotFoundException {
        ObjectInputStream fileObjectIn = new ObjectInputStream(serverSocket.getInputStream());
        return (Object) fileObjectIn.readObject();
    }

    private static void sendObject (Object object) throws IOException {
        ObjectOutputStream fileObjectOut = new ObjectOutputStream(serverSocket.getOutputStream());
        fileObjectOut.writeObject(object);
        fileObjectOut.flush();
    }

    private static void sendMessage(String message) throws IOException {
        sendObject(new Message(message));
    }

    private static String waitForMessage() throws IOException, ClassNotFoundException {
        return ((Message) waitForObject()).getMessage();
    }

    private static boolean isPositionCorrect (Position position, Set <Position> collection){
        if (position==null) return false;
        return collection.stream().anyMatch(x -> x.equals(position));
    }

    private static String askForInfos(){
        System.out.println(Color.BLUE.set());
        System.out.println("  ╔══ ╔═╗ ╖ ╓ ═╦═ ╔═╗ ╔═╗ ╥ ╖ ╓ ╥ ®");
        System.out.println("  ╚═╗ ╠═╣ ║\\║  ║  ║ ║ ╠\\╝ ║ ║\\║ ║");
        System.out.println("  ══╝ ╜ ╙ ╜ ╙  ╨  ╚═╝ ╜ \\ ╨ ╜ ╙ ╨\n");
        String myName = askForString(Color.YELLOW.set() + "What's your name? ");
        numOfPlayers = askForInt("How many players? ");
        return myName;
    }

    private static String askForGodPower (List <String> godPowers){
        String godPower = "";
        if(godPowers.size()==1) return godPowers.get(0);
        while (true) {
            for (String s : godPowers) System.out.print(s + " ");
            godPower = askForString("left, choose God Power: ");
            String finalGodPower = godPower;
            if  (godPowers.stream().anyMatch(x -> x.equals(finalGodPower))) break;
        }
        return godPower;
    }

    private static List <Position> askForWorkersInitialPositions (){
        int myWorker1x = askForInt("Worker 1 X: ");
        int myWorker1y = askForInt("Worker 1 Y: ");
        int myWorker2x = askForInt("Worker 2 X: ");
        int myWorker2y = askForInt("Worker 2 Y: ");
        List<Position> myWorkerPositions = new ArrayList<>();
        myWorkerPositions.add(new Position(myWorker1x, myWorker1y, 0));
        myWorkerPositions.add(new Position(myWorker2x, myWorker2y, 0));
        return myWorkerPositions;
    }

    private static Position askForPosition(){
        int x = askForInt("x: ");
        int y = askForInt("y: ");
        int z = askForInt("z: ");
        return new Position(x, y, z);
    }

    private static int askForInt(String request){
        try {
            System.out.print(request);
            int fromKeyboard = keyboard.nextInt();
            return fromKeyboard;
        } catch(Exception e){return askForInt(request);}
    }

    private static String askForString(String request){
        System.out.print(request);
        String fromKeyboard = keyboard.next();
        return fromKeyboard;
    }

    private static boolean askForBoolean(String request){
        String fromKeyboard;
        while (true) {
            System.out.print(request);
            fromKeyboard = keyboard.next();
            if (fromKeyboard.toLowerCase().equals("y") || fromKeyboard.toLowerCase().equals("n")) break;
        }
        return fromKeyboard.toLowerCase().equals("y");
    }

    private static void displayBoard(){
        System.out.print(Color.WHITE.set()+"\n┌─────────────────────────────┐\n");
        //for (int j = 0; j<5; j++){
        //    for (int i = 4; i >=0; i--){
        for (int j = 4; j>=0; j--){
            for (int i = 0; i <5; i++){
                boolean isThereAWorker = false;
                for (int k = 0; k < numOfPlayers; k++){
                    if (!board.getPlayer(k+1).hasLost()) {
                        int worker1x = board.getPlayer(k+1).getWorker(1).getX();
                        int worker1y = board.getPlayer(k+1).getWorker(1).getY();
                        int worker2x = board.getPlayer(k+1).getWorker(2).getX();
                        int worker2y = board.getPlayer(k+1).getWorker(2).getY();
                        if ((i==worker1x && j==worker1y)||(i==worker2x && j==worker2y)) isThereAWorker = true;
                    }
                }
                if (isThereAWorker)  System.out.print(Color.WHITE.set()+"│ "+Color.BLUE.set()+" ■  ");
                else if (board.getCell(i, j)==null) System.out.print(Color.WHITE.set()+"│     ");
                else if (board.getCell(i, j).isDome()) System.out.print(Color.WHITE.set()+"│ "+Color.BLUE.set()+"▲▲▲ ");
                else {
                    System.out.print(Color.WHITE.set()+"│ "+ Color.BLUE.set());
                    switch (board.getCell(i, j).getLevel()){
                        case 0: System.out.print("░░░"); break;
                        case 2: System.out.print("▒▒▒"); break;
                        case 1: System.out.print("▓▓▓"); break;
                    }
                    System.out.print(Color.WHITE.set()+ " ");
                }
            }
            System.out.print(Color.WHITE.set()+"│");
            if (j==0) System.out.print(Color.WHITE.set()+"\n└─────────────────────────────┘\n");
            else System.out.print(Color.WHITE.set()+"\n├─────────────────────────────┤\n");
        }
        System.out.println();
        for (int i = 0; i < numOfPlayers; i++){
            if (!board.getPlayer(i+1).hasLost() && board.getPlayer(i+1).getGodPowerName()!=null) {
                System.out.print("Player " + (i + 1) + ": Worker 1 (" + board.getPlayer(i + 1).getWorker(1).getX() + ", " + board.getPlayer(i + 1).getWorker(1).getY() + ")");
                System.out.print(", Worker 2 (" + board.getPlayer(i + 1).getWorker(2).getX() + ", " + board.getPlayer(i + 1).getWorker(2).getY() + ")");
                System.out.println(", " + board.getPlayer(i + 1).getGodPowerName());
            } else if (board.getPlayer(i+1).getGodPowerName()==null) System.out.println("Player " + (i + 1));
            else System.out.println("Player " + (i + 1) + " has lost");
        }
    }
}
