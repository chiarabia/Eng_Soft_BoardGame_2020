package it.polimi.ingsw.client;

// CLIENTBOARD -----> CLIENTPLAYER [3] -----------> CLIENTWORKER [2]
//      Ͱ-----------> CLIENTBUILDING [5][5]

import it.polimi.ingsw.Position;
import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.server.serializable.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.ClientKeyboard.*;

public class Client {
    private static ClientBoard board;
    private static Socket serverSocket;
    private static int numOfPlayers = 0;
    private static int myPlayerId = 0;

    public static void startClient(int port, String IP) {
        displayLogo();
        String myName = askForString(Color.WHITE.set() + "What's your name? ");
        numOfPlayers = askForInt("How many players? ");
        board = new ClientBoard(numOfPlayers);
        try{
            setup(myName, IP, port);
            while (true) reactToServer(waitForObject());
        } catch (GameEndedException e){}
        catch (Exception e){System.out.println(Color.RED.set() + "Oops... something went wrong"); e.printStackTrace();}
    }

    private static void reactToServer(Object object) throws Exception {
        boolean isDome;
        Position position = null;
        int playerId, workerId = 0, oldLevel, x, y;
        if (object instanceof SerializableRequestMove) {
            if (((SerializableRequestMove) object).getWorker1Moves().size() != 0) {
                workerId = 1;
                System.out.print("Worker 1 possible moves: ");
                printCells (((SerializableRequestMove) object).getWorker1Moves());
            }
            if (((SerializableRequestMove) object).getWorker2Moves().size() != 0) {
                workerId = 2;
                System.out.print("Worker 2 possible moves: ");
                printCells (((SerializableRequestMove) object).getWorker2Moves());
            }
            if (((SerializableRequestMove) object).getWorker1Moves().size() != 0 && ((SerializableRequestMove) object).getWorker2Moves().size() != 0)
                workerId = askForInt("worker id: ");
            while (!isPositionCorrect(position, ((SerializableRequestMove) object).getWorker1Moves()) && !isPositionCorrect(position, ((SerializableRequestMove) object).getWorker2Moves()))
                position = askForPosition();
            Position position1 = new Position(position.getX(), position.getY(), 0);
            if (((SerializableRequestMove) object).getWorker1Moves().stream().anyMatch(p -> p.getX() == position1.getX() && p.getY() == position1.getY())){
                position = new Position(position1.getX(), position1.getY(), ((SerializableRequestMove) object).getWorker1Moves().stream().filter(p -> p.getX() == position1.getX() && p.getY() == position1.getY()).map(p -> p.getZ()).collect(Collectors.toList()).get(0));
            }else {
                position = new Position(position1.getX(), position1.getY(), ((SerializableRequestMove) object).getWorker2Moves().stream().filter(p -> p.getX() == position1.getX() && p.getY() == position1.getY()).map(p -> p.getZ()).collect(Collectors.toList()).get(0));
            }
             sendObject(new SerializableConsolidateMove(position, workerId));
        }
        if (object instanceof SerializableRequestBuild){
            if (((SerializableRequestBuild) object).getWorker1Builds().size()!=0) {
                workerId = 1;
                System.out.print("Worker 1 possible builds: ");
                printCells (((SerializableRequestBuild) object).getWorker1Builds());
            }
            if (((SerializableRequestBuild) object).getWorker2Builds().size()!=0) {
                workerId = 2;
                System.out.print("Worker 2 possible builds: ");
                printCells (((SerializableRequestBuild) object).getWorker2Builds());
            }
            if (((SerializableRequestBuild) object).getWorker1Builds().size()!=0 && ((SerializableRequestBuild) object).getWorker2Builds().size()!=0) workerId = askForInt("worker id:");
            if (((SerializableRequestBuild) object).isCanForceMove()) isDome = askForBoolean("is dome (y/n): ");
            else isDome = false;
            while (!isPositionCorrect(position, ((SerializableRequestBuild) object).getWorker1Builds()) && !isPositionCorrect(position, ((SerializableRequestBuild) object).getWorker2Builds()) )
                position = askForPosition();
            Position position1 = new Position(position.getX(), position.getY(), 0);
            if (((SerializableRequestBuild) object).getWorker1Builds().stream().anyMatch(p->p.getX()==position1.getX()&& p.getY()==position1.getY())) {
                position = new Position (position1.getX(), position1.getY(), ((SerializableRequestBuild) object).getWorker1Builds().stream().filter(p->p.getX()==position1.getX()&& p.getY()==position1.getY()).map(p->p.getZ()).collect(Collectors.toList()).get(0));
            }else {
                position = new Position (position1.getX(), position1.getY(), ((SerializableRequestBuild) object).getWorker2Builds().stream().filter(p->p.getX()==position1.getX()&& p.getY()==position1.getY()).map(p->p.getZ()).collect(Collectors.toList()).get(0));
            }
            sendObject(new SerializableConsolidateBuild(position, workerId, isDome));
        }
        if (object instanceof SerializableUpdateMove){
            playerId = ((SerializableUpdateMove) object).getPlayerId();
            workerId = ((SerializableUpdateMove) object).getWorkerId();
            x = ((SerializableUpdateMove) object).getNewPosition().getX();
            y = ((SerializableUpdateMove) object).getNewPosition().getY();
            board.getPlayer(playerId).getWorker(workerId).setX(x);
            board.getPlayer(playerId).getWorker(workerId).setY(y);
            System.out.println(board.getPlayer(playerId).getPlayerName() + " has moved worker "+workerId+" to ("+ x + ", " + y + ")");
            displayBoard();
        }
        if (object instanceof SerializableUpdateBuild){
            x = ((SerializableUpdateBuild) object).getNewPosition().getX();
            y = ((SerializableUpdateBuild) object).getNewPosition().getY();
            isDome = ((SerializableUpdateBuild) object).isDome();
            if (board.getCell(x, y)!=null) oldLevel = board.getCell(x,y).getLevel();
            else oldLevel = -1;
            board.setCell(new ClientBuilding(oldLevel + 1, isDome), x, y);
            if (!isDome) System.out.println("Building in ("+ x + ", " + y + ")");
            else System.out.println("Dome in ("+ x + ", " + y + ")");
            displayBoard();
        }
        if (object instanceof SerializableRequestOptional){ //todo: mai testata per assenza di controparte sul Controller
            System.out.print("Worker 1 possible moves: ");
            printCells(((SerializableRequestOptional) object).getWorker1Moves());
            System.out.print("Worker 2 possible moves: ");
            printCells(((SerializableRequestOptional) object).getWorker2Moves());
            System.out.print("Worker 1 possible builds: ");
            printCells(((SerializableRequestOptional) object).getWorker1Builds());
            System.out.print("Worker 2 possible builds: ");
            printCells(((SerializableRequestOptional) object).getWorker2Builds());
            if (((SerializableRequestOptional) object).isMoveOptional()) System.out.println("Moves are optional");
            else System.out.println("Builds are optional");
            if (((SerializableRequestOptional) object).canDecline()) {
                if (askForBoolean("Do you want to decline(y/n)? ")) {
                    sendObject(new SerializableDeclineLastOptional());
                    return;
                }
            }
            if (askForBoolean("Do you want to move(y/n)? ")){
                if (((SerializableRequestOptional) object).getWorker1Moves().size() != 0 && ((SerializableRequestOptional) object).getWorker2Moves().size() != 0)
                    workerId = askForInt("worker id: ");
                while (!isPositionCorrect(position, ((SerializableRequestOptional) object).getWorker1Moves()) && !isPositionCorrect(position, ((SerializableRequestOptional) object).getWorker2Moves()))
                    position = askForPosition();
                Position position1 = new Position(position.getX(), position.getY(), 0);
                if (((SerializableRequestOptional) object).getWorker1Moves().stream().anyMatch(p -> p.getX() == position1.getX() && p.getY() == position1.getY())){
                    position = new Position(position1.getX(), position1.getY(), ((SerializableRequestOptional) object).getWorker1Moves().stream().filter(p -> p.getX() == position1.getX() && p.getY() == position1.getY()).map(p -> p.getZ()).collect(Collectors.toList()).get(0));
                }else {
                    position = new Position(position1.getX(), position1.getY(), ((SerializableRequestOptional) object).getWorker2Moves().stream().filter(p -> p.getX() == position1.getX() && p.getY() == position1.getY()).map(p -> p.getZ()).collect(Collectors.toList()).get(0));
                }
                sendObject(new SerializableConsolidateMove(position, workerId));
            } else {
                if (((SerializableRequestOptional) object).isCanForceDome()) isDome = askForBoolean("is dome (y/n): ");
                else isDome = false;
                while (!isPositionCorrect(position, ((SerializableRequestOptional) object).getWorker1Builds()) && !isPositionCorrect(position, ((SerializableRequestOptional) object).getWorker2Builds()) )
                    position = askForPosition();
                Position position1 = new Position(position.getX(), position.getY(), 0);
                if (((SerializableRequestOptional) object).getWorker1Builds().stream().anyMatch(p->p.getX()==position1.getX()&& p.getY()==position1.getY())) {
                    position = new Position (position1.getX(), position1.getY(), ((SerializableRequestOptional) object).getWorker1Builds().stream().filter(p->p.getX()==position1.getX()&& p.getY()==position1.getY()).map(p->p.getZ()).collect(Collectors.toList()).get(0));
                }else {
                    position = new Position (position1.getX(), position1.getY(), ((SerializableRequestOptional) object).getWorker2Builds().stream().filter(p->p.getX()==position1.getX()&& p.getY()==position1.getY()).map(p->p.getZ()).collect(Collectors.toList()).get(0));
                }
                sendObject(new SerializableConsolidateBuild(position, workerId, isDome));
            }
        }
        if (object instanceof SerializableUpdateTurn){
            int playerTurnId = ((SerializableUpdateTurn) object).getPlayerId();
            board.setPlayerTurnId(playerTurnId);
            if (playerTurnId==myPlayerId) System.out.println("You are playing");
            else System.out.println(board.getPlayer(((SerializableUpdateTurn) object).getPlayerId()).getPlayerName() + " now playing");
        }
        if (object instanceof SerializableUpdateLoser){
            playerId = ((SerializableUpdateLoser) object).getPlayerId();
            if (playerId == myPlayerId) {
                System.out.println("You have lost!");
                throw new GameEndedException();
            }
            else System.out.println(board.getPlayer(playerId).getPlayerName()  +" has lost");
        }
        if (object instanceof SerializableUpdateWinner){
            playerId = ((SerializableUpdateWinner) object).getPlayerId();
            if (playerId==myPlayerId) System.out.println("You have won!");
            else System.out.println(board.getPlayer(((SerializableUpdateWinner) object).getPlayerId()).getPlayerName()  + " has won");
            throw new GameEndedException();
        }
        if (object instanceof SerializableUpdateDisconnection){
            System.out.println(board.getPlayer(((SerializableUpdateDisconnection) object).getPlayerId()).getPlayerName()  + " disconnected");
            throw new GameEndedException();
        }
    }

    private static void setup (String myName, String serverIP, int serverPort) throws Exception {
        serverSocket = new Socket(serverIP, serverPort);
        sendMessage(numOfPlayers + " players");
        String message = "";
        while (message.equals("Player's name") || message.equals("")) {
            message = waitForMessage();
            if (message.equals("Player's name")) sendMessage(myName);
        }
        myPlayerId = Character.getNumericValue(message.charAt(15));
        SerializableUpdateInitializeNames names = (SerializableUpdateInitializeNames) waitForObject();
        System.out.print("You are playing with ");
        boolean firstName = true;
        for (int id = 1; id <= numOfPlayers; id++) {
            board.setPlayer(new ClientPlayer(names.getPlayersNames().get(id - 1)), id);
            if (id!=myPlayerId) {
                if (firstName) {
                    System.out.print(names.getPlayersNames().get(id - 1));
                    firstName = false;
                } else System.out.print(" and " + names.getPlayersNames().get(id - 1));
            }
        }
        System.out.println(".");
        while (true){
            Object object = waitForObject();
            if (object instanceof SerializableUpdateTurn) {
                int playerTurnId = ((SerializableUpdateTurn) object).getPlayerId();
                board.setPlayerTurnId(playerTurnId);
                if (playerTurnId==myPlayerId) System.out.println("You are playing");
                else System.out.println(board.getPlayer(((SerializableUpdateTurn) object).getPlayerId()).getPlayerName() + " now playing");
                return;
            }
            if (object instanceof SerializableUpdateInitializeGame){
                String godPower = ((SerializableUpdateInitializeGame) object).getGodPower();
                int whichPlayerId = ((SerializableUpdateInitializeGame) object).getPlayerId();
                Position positionWorker1 = ((SerializableUpdateInitializeGame) object).getWorkerPositions().get(0);
                Position positionWorker2 = ((SerializableUpdateInitializeGame) object).getWorkerPositions().get(1);
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
                System.out.println(board.getPlayer(((SerializableUpdateDisconnection) object).getPlayerId()).getPlayerName()  + " disconnected");
                throw new GameEndedException();
            }
        }
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

    private static void printCells( Set<Position> positions) {
        for (Position p : positions)
            System.out.print("(" + p.getX() + ", " + p.getY() + ") ");
        System.out.println();
    }

    private static boolean isPositionCorrect (Position position, Set <Position> collection){
        if (position==null) return false;
        return collection.stream().anyMatch(x -> x.getX()==position.getX() && x.getY()==position.getY());
    }

    private static void displayLogo(){
        System.out.println(Color.BLUE.set());
        System.out.println("  ╔══ ╔═╗ ╖ ╓ ═╦═ ╔═╗ ╔═╗ ╥ ╖ ╓ ╥ ®");
        System.out.println("  ╚═╗ ╠═╣ ║\\║  ║  ║ ║ ╠\\╝ ║ ║\\║ ║");
        System.out.println("  ══╝ ╜ ╙ ╜ ╙  ╨  ╚═╝ ╜ \\ ╨ ╜ ╙ ╨\n");
    }

    private static void displayBoard(){
        System.out.print(Color.WHITE.set()+"   ╔═════╤═════╤═════╤═════╤═════╗\n");
        for (int j = 4; j>=0; j--){
            String element = "║";
            System.out.print(" " +  j + " ");
            for (int i = 0; i < 5; i++){
                boolean isThereAWorker = false;
                int playerId = 0;
                for (int k = 0; k < numOfPlayers; k++){
                    if (!board.getPlayer(k+1).hasLost()) {
                        int worker1x = board.getPlayer(k+1).getWorker(1).getX();
                        int worker1y = board.getPlayer(k+1).getWorker(1).getY();
                        int worker2x = board.getPlayer(k+1).getWorker(2).getX();
                        int worker2y = board.getPlayer(k+1).getWorker(2).getY();
                        if ((i==worker1x && j==worker1y)||(i==worker2x && j==worker2y)) {
                            isThereAWorker = true;
                            playerId = k + 1;
                            break;
                        }
                    }
                }
                if (isThereAWorker)  {
                    System.out.print(Color.WHITE.set()+element+" ");
                    switch (playerId) {
                        case 1: System.out.print(Color.RED.set()); break;
                        case 2: System.out.print(Color.YELLOW.set()); break;
                        case 3: System.out.print(Color.BLUE.set()); break;
                    }
                    System.out.print(" ■  ");
                }
                else if (board.getCell(i, j)==null) System.out.print(Color.WHITE.set()+element+"     ");
                else if (board.getCell(i, j).isDome()) System.out.print(Color.WHITE.set()+element+" "+Color.BLUE.set()+"▲▲▲ ");
                else {
                    System.out.print(Color.WHITE.set()+element+ Color.BLUE.set());
                    switch (board.getCell(i, j).getLevel()){
                        case 0: System.out.print(" ░░░ "); break;
                        case 1: System.out.print(" ▒▒▒ "); break;
                        case 2: System.out.print(" ▓▓▓ "); break;
                    }
                }
                element = "│";
            }
            System.out.print(Color.WHITE.set()+"║");
            if (j==0) System.out.print(Color.WHITE.set()+"\n   ╚═════╧═════╧═════╧═════╧═════╝\n      0     1     2     3     4");
            else System.out.print(Color.WHITE.set()+     "\n   ╟─────┼─────┼─────┼─────┼─────╢\n");
        }
        System.out.println();
        for (int i = 0; i < numOfPlayers; i++){
            switch (i) {
                case 0: System.out.print(Color.RED.set()); break;
                case 1: System.out.print(Color.YELLOW.set()); break;
                case 2: System.out.print(Color.BLUE.set()); break;
            }
            System.out.print(board.getPlayer(i+1).getPlayerName());
            if (!board.getPlayer(i+1).hasLost() && board.getPlayer(i+1).getGodPowerName()!=null) {
                System.out.print(": Worker 1 (" + board.getPlayer(i + 1).getWorker(1).getX() + ", " + board.getPlayer(i + 1).getWorker(1).getY() + ")");
                System.out.print(", Worker 2 (" + board.getPlayer(i + 1).getWorker(2).getX() + ", " + board.getPlayer(i + 1).getWorker(2).getY() + ")");
                System.out.println(", " + board.getPlayer(i + 1).getGodPowerName());
            } else if (board.getPlayer(i+1).getGodPowerName()==null) System.out.println();
            else System.out.println(" has lost");
        }
        System.out.print(Color.WHITE.set());
    }
}
