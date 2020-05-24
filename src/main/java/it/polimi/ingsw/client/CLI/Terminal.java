package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.ClientBoard;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.serializable.SerializableRequestAction;
import it.polimi.ingsw.server.serializable.SerializableUpdateInitializeNames;

import java.util.*;
import java.util.stream.Collectors;

public class Terminal implements View {
    private final Scanner keyboard = new Scanner(System.in);
    private ClientBoard board;

    public void setBoard(ClientBoard board) {
        this.board = board;
    }

    public void displayPlayerNames(SerializableUpdateInitializeNames names){
        System.out.print("You are playing with ");
        boolean firstName = true;
        for (int id = 1; id <= board.numOfPlayers(); id++) {
            if (id != board.getMyPlayerId()) {
                if (firstName) {
                    System.out.print(names.getPlayersNames().get(id - 1));
                    firstName = false;
                } else System.out.print(" and " + names.getPlayersNames().get(id - 1));
            }
        }
        System.out.println(".");
    }

    public void displayBoard () {
        System.out.print(Terminal.Color.WHITE.set() + "   ╔═════╤═════╤═════╤═════╤═════╗\n");
        for (int j = 4; j >= 0; j--) {
            String element = "║";
            System.out.print(" " + j + " ");
            for (int i = 0; i < 5; i++) {
                boolean isThereAWorker = false;
                int playerId = 0;
                for (int k = 0; k < board.numOfPlayers(); k++) {
                    if (!board.getPlayer(k + 1).hasLost()) {
                        int worker1x = board.getPlayer(k + 1).getWorker(1).getX();
                        int worker1y = board.getPlayer(k + 1).getWorker(1).getY();
                        int worker2x = board.getPlayer(k + 1).getWorker(2).getX();
                        int worker2y = board.getPlayer(k + 1).getWorker(2).getY();
                        if ((i == worker1x && j == worker1y) || (i == worker2x && j == worker2y)) {
                            isThereAWorker = true;
                            playerId = k + 1;
                            break;
                        }
                    }
                }
                if (isThereAWorker) {
                    System.out.print(Terminal.Color.WHITE.set() + element + " ");
                    switch (playerId) {
                        case 1:
                            System.out.print(Terminal.Color.RED.set());
                            break;
                        case 2:
                            System.out.print(Terminal.Color.YELLOW.set());
                            break;
                        case 3:
                            System.out.print(Terminal.Color.BLUE.set());
                            break;
                    }
                    System.out.print(" ■  ");
                } else if (board.getCell(i, j) == null) System.out.print(Terminal.Color.WHITE.set() + element + "     ");
                else if (board.getCell(i, j).isDome())
                    System.out.print(Terminal.Color.WHITE.set() + element + " " + Terminal.Color.BLUE.set() + "▲▲▲ ");
                else {
                    System.out.print(Terminal.Color.WHITE.set() + element + Terminal.Color.BLUE.set());
                    switch (board.getCell(i, j).getLevel()) {
                        case 0:
                            System.out.print(" ░░░ ");
                            break;
                        case 1:
                            System.out.print(" ▒▒▒ ");
                            break;
                        case 2:
                            System.out.print(" ▓▓▓ ");
                            break;
                    }
                }
                element = "│";
            }
            System.out.print(Terminal.Color.WHITE.set() + "║");
            if (j == 0)
                System.out.print(Terminal.Color.WHITE.set() + "\n   ╚═════╧═════╧═════╧═════╧═════╝\n      0     1     2     3     4");
            else System.out.print(Terminal.Color.WHITE.set() + "\n   ╟─────┼─────┼─────┼─────┼─────╢\n");
        }
        System.out.println();
        for (int i = 0; i < board.numOfPlayers(); i++) {
            switch (i) {
                case 0:
                    System.out.print(Terminal.Color.RED.set());
                    break;
                case 1:
                    System.out.print(Terminal.Color.YELLOW.set());
                    break;
                case 2:
                    System.out.print(Terminal.Color.BLUE.set());
                    break;
            }
            System.out.print(board.getPlayer(i + 1).getPlayerName());
            if (!board.getPlayer(i + 1).hasLost() && board.getPlayer(i + 1).getGodPowerName() != null) {
                System.out.print(": Worker 1 (" + board.getPlayer(i + 1).getWorker(1).getX() + ", " + board.getPlayer(i + 1).getWorker(1).getY() + ")");
                System.out.print(", Worker 2 (" + board.getPlayer(i + 1).getWorker(2).getX() + ", " + board.getPlayer(i + 1).getWorker(2).getY() + ")");
                System.out.println(", " + board.getPlayer(i + 1).getGodPowerName());
            } else if (board.getPlayer(i + 1).getGodPowerName() == null) System.out.println();
            else System.out.println(" has lost");
        }
        System.out.print(Terminal.Color.WHITE.set());
    }

    public void displayMessage(String string){
        System.out.println(string);
    }

    public void displayStartUp () {
        System.out.println(Terminal.Color.BLUE.set());
        System.out.println("  ╔══ ╔═╗ ╖ ╓ ═╦═ ╔═╗ ╔═╗ ╥ ╖ ╓ ╥ ®");
        System.out.println("  ╚═╗ ╠═╣ ║\\║  ║  ║ ║ ╠\\╝ ║ ║\\║ ║");
        System.out.println("  ══╝ ╜ ╙ ╜ ╙  ╨  ╚═╝ ╜ \\ ╨ ╜ ╙ ╨\n");
    }

    public void displayCells (Set < Position > positions) {
        for (Position p : positions)
            System.out.print("(" + p.getX() + ", " + p.getY() + ") ");
        System.out.println();
    }

    public void displayTurn(){
        int playerTurnId = board.getPlayerTurnId();
        if (playerTurnId == board.getMyPlayerId()) System.out.println("You are playing");
        else
            System.out.println(board.getPlayer(playerTurnId).getPlayerName() + " now playing");
        return;
    }

    public void displayEndTurn(String message){
        System.out.println(message);

    }

    public void displayRequestAction(SerializableRequestAction object){
        if (object.getWorker1Moves().size()>0) {
            System.out.print("Worker 1 possible moves: ");
            displayCells((object).getWorker1Moves());
        }
        if (object.getWorker2Moves().size()>0) {
            System.out.print("Worker 2 possible moves: ");
            displayCells((object).getWorker2Moves());
        }
        if (object.getWorker1Builds().size()>0) {
            System.out.print("Worker 1 possible builds: ");
            displayCells((object).getWorker1Builds());
        }
        if (object.getWorker2Builds().size()>0) {
            System.out.print("Worker 2 possible builds: ");
            displayCells((object).getWorker2Builds());
        }
        if ((object).isMoveOptional())
            System.out.println("Moves are optional");
        if ((object).isBuildOptional())
            System.out.println("Builds are optional");

        if(!(object.canWorkerDoAction(1) && object.canWorkerDoAction(2))&&!(object.areBuildsEmpty()&&object.areMovesEmpty())) {
            int workerId = 0;
            int i;
            for(i = 1; i<=2; i++) {
                if (object.canWorkerDoAction(i)) {//in caso contrario non ho bisogno di chiedere al player
                    workerId = i;
                }
            }
            System.out.println("worker id: "+ workerId);
        }
    }

    public String askForName(){
        return askForString(Terminal.Color.WHITE.set() + "What's your name? ");
    }

    public int askForNumOfPlayers(){
        return askForInt("How many players? ");
    }

    // todo:sistemare
    public Position askForRightPosition (Set<Position> positions) {
        Position position = null;
        while (!isPositionCorrect(position, positions))
            position = askForPosition();
        Position position1 = new Position(position.getX(), position.getY(), 0);
        return new Position(position1.getX(), position1.getY(), positions.stream().filter(p -> p.getX() == position1.getX() && p.getY() == position1.getY()).map(Position::getZ).collect(Collectors.toList()).get(0));
    }

    public boolean askForDecline(String request) {
        return askForBoolean(request);
    }

    public boolean askForDecision(String action){
        return askForBoolean("Do you want to "+action+"(y/n)? ");
    }

    public int askForWorker(){
        return askForInt("worker id: ");
    }

    public boolean askForDome(){
        return askForBoolean("is dome (y/n): ");
    }

    public String askForGodPower (List<String> godPowers){
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

    public List <Position> askForWorkersInitialPositions (){
        int myWorker1x = askForInt("Worker 1 x: ");
        int myWorker1y = askForInt("Worker 1 y: ");
        int myWorker2x = askForInt("Worker 2 x: ");
        int myWorker2y = askForInt("Worker 2 y: ");
        List<Position> myWorkerPositions = new ArrayList<>();
        myWorkerPositions.add(new Position(myWorker1x, myWorker1y, 0));
        myWorkerPositions.add(new Position(myWorker2x, myWorker2y, 0));
        return myWorkerPositions;
    }

    public Position askForPosition(){
        int x = askForInt("x: ");
        int y = askForInt("y: ");
        return new Position(x, y, 0);
    }



    // metodi riservati

    private int askForInt(String request){
        try {
            System.out.print(request);
            int fromKeyboard = keyboard.nextInt();
            return fromKeyboard;
        } catch(Exception e){return 0;}
    }

    private String askForString(String request){
        System.out.print(request);
        String fromKeyboard = keyboard.next();
        return fromKeyboard;
    }

    private boolean askForBoolean(String request){
        String fromKeyboard;
        while (true) {
            System.out.print(request);
            fromKeyboard = keyboard.next();
            if (fromKeyboard.toLowerCase().equals("y") || fromKeyboard.toLowerCase().equals("n")) break;
        }
        return fromKeyboard.toLowerCase().equals("y");
    }


    private boolean isPositionCorrect (Position position, Set < Position > collection){
        if (position == null) return false;
        return collection.stream().anyMatch(x -> x.getX() == position.getX() && x.getY() == position.getY());
    }

    public enum Color {
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
}
