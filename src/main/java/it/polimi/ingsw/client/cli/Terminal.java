package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.*;
import it.polimi.ingsw.server.serializable.*;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.util.*;
import java.util.stream.Collectors;

/** This class contains CLI methods */

public class Terminal implements View {
    private Textfields textfields = new Textfields();
    private final Scanner keyboard = new Scanner(System.in);
    private ClientBoard board;
    private List<ViewObserver> observerList = new ArrayList<>();

    public Terminal() throws ParseException {}
    public void addObserver(ViewObserver observer){observerList.add(observer);}
    public void setBoard(ClientBoard board) {
        this.board = board;
    }

    public void displayWaitingRoom(){}

    /**Displays the board scene for the first time*/
    public void displayBoardScreen(){
        System.out.println();
        displayBoard();
    }

    /**
     * Displays players' names
     * @param names object received from server
     * */
    public void displayPlayerNames(SerializableUpdateInitializeNames names){
        System.out.print(textfields.getInitialplaying());
        boolean firstName = true;
        for (int id = 1; id <= board.getNumOfPlayers(); id++) {
            if (id != board.getMyPlayerId()) {
                if (firstName) {
                    setColor(id);
                    System.out.print(names.getPlayersNames().get(id - 1));
                    firstName = false;
                } else {
                    System.out.print(Color.WHITE.set()+" and ");
                    setColor(id);
                    System.out.print (names.getPlayersNames().get(id - 1));
                }
            }
        }
        System.out.println(Color.WHITE.set()+".\n");
    }

    /**
     * Shows the board after one or more players' actions
     * @param update object received from server
     * */
    public void displayBoard(SerializableUpdateActions update) {
        displayBoard();
    }

    /**
     * Shows the board after workers' positions initialization
     * @param update object received from server
     * */
    public void displayBoard(SerializableUpdateInitializeWorkerPositions update){displayBoard();}

    /**
     * Reports that a player has won
     * @param playerId player ID
     * */
    public void displayWinner (int playerId) {
        displayBoard();
        setColor(playerId);
        if (playerId == board.getMyPlayerId()) System.out.println("You" + Color.WHITE.set() + textfields.getWon1());
        else System.out.println(board.getPlayer(playerId).getPlayerName() + Color.WHITE.set() +  textfields.getWon2());
    }

    /**
     * Reports that a player has lost
     * @param playerId player ID
     * */
    public void displayLoser (int playerId) {
        displayBoard();
        setColor(playerId);
        if (playerId == board.getMyPlayerId()) System.out.println("You" + Color.WHITE.set() + textfields.getLost1());
        else System.out.println(board.getPlayer(playerId).getPlayerName() + Color.WHITE.set() +  textfields.getLost2());
    }

    /**
     * Reports that a player disconnected
     * @param playerId player ID
     * */
    public void displayDisconnection (int playerId){
        setColor(playerId);
        System.out.println(board.getPlayer(playerId).getPlayerName() + Color.WHITE.set() +  textfields.getDisconnected());
    }

    /**
     * Reports that an error occurred
     * @param errorId error ID
     * @param isFatalError true if the error causes the client to stop
     * */
    public void displayError(int errorId, boolean isFatalError){
        String message = null;
        switch (errorId){
            case 0: message = textfields.getErr0(); break;
            case 1: message = textfields.getErr1(); break;
            case 2: message = textfields.getErr2(); break;
        }
        displayErrorMessage(message);
    }

    public void displayGameStart(){
        System.out.println();
    }

    /**Show the initial ASCII-art Santorini logo*/
    public void displayStartup () {
        System.out.println(Terminal.Color.BLUE.set());
        System.out.println("  ╔══ ╔═╗ ╖ ╓ ═╦═ ╔═╗ ╔═╗ ╥ ╖ ╓ ╥ ®");
        System.out.println("  ╚═╗ ╠═╣ ║\\║  ║  ║ ║ ╠\\╝ ║ ║\\║ ║");
        System.out.println("  ══╝ ╜ ╙ ╜ ╙  ╨  ╚═╝ ╜ \\ ╨ ╜ ╙ ╨\n");
    }

    /**
     * Reports that a player has chosen a god power
     * @param playerId player ID
     * */
    public void displayGodPower(int playerId){
        String godPowerName = board.getPlayer(playerId).getGodPowerName();
        String playerName = board.getPlayer(playerId).getPlayerName();
        if (playerId != board.getMyPlayerId()) {
            setColor(playerId);
            System.out.print(playerName + Color.WHITE.set());
            System.out.println(textfields.getChosen2()+ godPowerName);
        }
    }

    /**
     * Reports that a new turn started
     * @param playerTurnId player ID
     * */
    public void displayTurn(int playerTurnId){
        setColor(playerTurnId);
        if (playerTurnId == board.getMyPlayerId()) System.out.println("You" + Color.WHITE.set() + textfields.getPlaying1());
        else System.out.println(board.getPlayer(playerTurnId).getPlayerName() + Color.WHITE.set() +  textfields.getPlaying2());
    }

    /**
     * Asks to perform an action, based on available actions listed in the object received
     * @param object object received from server
     * */
    public void askForAction(SerializableRequestAction object){
            boolean isDome;
            Position position;
            int workerId = 0;
            boolean move = false, build = false;
            displayRequestAction(object);

            if (object.canDecline()) {
                if (object.areBuildsEmpty() && object.areMovesEmpty()) {
                    displayMessage(textfields.getTurnover());
                    for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedDecline();
                    return;
                }
                else if (askForDecision("decline")) {
                    for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedDecline();
                    return;
                }
            }
            if (object.canWorkerDoAction(1)&&object.canWorkerDoAction(2)) workerId = askForWorkerId();
            else {
                int i;
                for(i = 1; i<=2; i++) if (object.canWorkerDoAction(i)) workerId = i;
            }

            if (!object.areMovesEmpty()&&!object.areBuildsEmpty()) {
                while (!move && !build) {
                    if (askForDecision("move")) move = true;
                    if (askForDecision("build")) build = true;
                }
            } else {
                if (object.areMovesEmpty()) build = true;
                else move = true;
            }

            if (move) {
                if (workerId==1) position = askForRightPosition(object.getWorker1Moves());
                else position = askForRightPosition(object.getWorker2Moves());
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedMove(position, workerId);
            }
            if (build) {
                if (object.isCanForceDome()) isDome = askForDome();
                else isDome = false;
                if (workerId==1) position = askForRightPosition(object.getWorker1Builds());
                else position = askForRightPosition(object.getWorker2Builds());
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedBuild(position, workerId, isDome);
            }
    }

    /**
     * Asks to choose between available god powers
     * @param godPowers List of available god powers
     * */
    public void askForInitialGodPower(List<GodCard> godPowers){
            String chosenGodPower = askForGodPower(godPowers);
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedInitializeGodPower(chosenGodPower);
    }

    /**
     * Asks to choose initial workers positions
     * @param possiblePositions List of possible positions
     * */
    public void askForInitialWorkerPositions(List <Position> possiblePositions){
            List<Position> myWorkerPositions = askForWorkersInitialPositions();
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedInitializeWorkerPositions(myWorkerPositions);
    }

    /**
     * Asks for a name and the number of players in the game
     * @param errorId error ID
     * */
    public void askForStartupInfos(int errorId) {
            if (errorId >= 0) displayError(errorId, false);
            String name = askForString(Terminal.Color.WHITE.set() + textfields.getName());
            int numOfPlayers = askForInt(textfields.getNumofplayers());
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedStartup(name, numOfPlayers);
    }





    /* private methods */

    /**Pints a message
     * @param string message
     */
    private void displayMessage(String string){
        System.out.println(string);
    }

    /**Prints a red error message
     * @param message error message
     */
    private void displayErrorMessage(String message){
        displayMessage(Terminal.Color.RED.set() + message + Terminal.Color.WHITE.set());
    }

    /**Prints all the positions in the set
     * @param positions set of positions
     */
    private void displayCells (Set < Position > positions) {
        for (Position p : positions)
            System.out.print("(" + p.getX() + ", " + p.getY() + ") ");
        System.out.println();
    }

    /**Asks for a position and checks if that position is present in a set of possible positions
     * @param positions set of possible positions
     * @return chosen position
     */
    private Position askForRightPosition (Set<Position> positions) {
        Position position = null;
        while (!isPositionCorrect(position, positions))
            position = askForPosition();
        Position position1 = new Position(position.getX(), position.getY(), 0);
        return new Position(position1.getX(), position1.getY(), positions.stream().filter(p -> p.getX() == position1.getX() && p.getY() == position1.getY()).map(Position::getZ).collect(Collectors.toList()).get(0));
    }

    /**Asks for an action
     * @param action action to choose
     * @return true if user decides to accept the decision, false otherwise
     * */
    private boolean askForDecision(String action){
        return askForBoolean("Do you want to "+action+"(y/n)? ");
    }

    /**Asks for worker id
     * @return worker Id*/
    private int askForWorkerId(){
        int workerId;
        while (true) {
            workerId = askForInt("worker id: ");
            if (workerId > 0 && workerId < 3) break;
        }
        return workerId;
    }

    /**Asks for a dome to be built
     * @return if user chooses to build a dome */
    private boolean askForDome(){
        return askForBoolean("is dome (y/n): ");
    }

    /**Asks to choose a god power present in a list of possible god powers
     * @param godPowers list of possible god powers
     * @return god power's name
     */
    private String askForGodPower (List<GodCard> godPowers){
        List<String> godNames = new ArrayList<>();
        for (int i = 0 ; i < godPowers.size(); i++) {
            godNames.add(godPowers.get(i).getGodName());
            System.out.println(Color.BLUE.set() + godPowers.get(i).getGodName()+Color.WHITE.set()+" "+godPowers.get(i).getGodDescription());
        }
        String godPower = "";
        while (true) {
            setColor(board.getMyPlayerId());
            System.out.print("You"+Color.WHITE.set()+textfields.getChosen1() + " (");
            for (int i = 0; i<godNames.size(); i++) {
                if (i!=godNames.size()-1) System.out.print(godNames.get(i) + "/");
                else System.out.print(godNames.get(i));
            }
            godPower = askForString("): ");
            String finalGodPower = godPower;
            if  (godNames.stream().anyMatch(x -> x.equals(finalGodPower))) break;
        }
        return godPower;
    }

    /**Asks to choose two initial positions
     * @return List of the two chosen initial positions, respectively about worker 1 and worker 2*/
    private List <Position> askForWorkersInitialPositions (){
        while (true) {
            System.out.println(textfields.getChooseworkerpositions());
            System.out.println(textfields.getWorker1());
            Position worker1Position = askForPosition();
            System.out.println(textfields.getWorker2());
            Position worker2Position = askForPosition();
            int myWorker1x = worker1Position.getX();
            int myWorker1y = worker1Position.getY();
            int myWorker2x = worker2Position.getX();
            int myWorker2y = worker2Position.getY();
            List<Position> myWorkerPositions = new ArrayList<>();
            myWorkerPositions.add(new Position(myWorker1x, myWorker1y, 0));
            myWorkerPositions.add(new Position(myWorker2x, myWorker2y, 0));
            boolean isValid = !(myWorker1x == myWorker2x && myWorker1y == myWorker2y);
            for (int i = 1; i < board.getNumOfPlayers(); i++) {
                if (board.getPlayer(i) != null) {
                    if  (
                        (board.getPlayer(i).getWorker(1).getX() == myWorker1x && board.getPlayer(i).getWorker(1).getY() == myWorker1y) ||
                        (board.getPlayer(i).getWorker(2).getX() == myWorker1x && board.getPlayer(i).getWorker(2).getY() == myWorker1y) ||
                        (board.getPlayer(i).getWorker(1).getX() == myWorker2x && board.getPlayer(i).getWorker(1).getY() == myWorker2y) ||
                        (board.getPlayer(i).getWorker(2).getX() == myWorker2x && board.getPlayer(i).getWorker(2).getY() == myWorker2y)
                        ) isValid = false;
                }
            }
            if (isValid) return myWorkerPositions;
        }
    }

    /**Waits for a position from the player
     * @return answer*/
    private Position askForPosition(){
        int x, y;
        while (true) {
            x = askForInt("x: ");
            y = askForInt("y: ");
            if (x >= 0 && y >= 0 && x < 5 && y < 5) break;
        }
        return new Position(x, y, 0);
    }

    /**Shows a request and waits for an integer from user
     * @param request request
     * @return answer*/
    private int askForInt(String request) {
        int num;
        while (true) {
            try{
                System.out.print(request);
                num = Integer.parseInt(keyboard.next());
                break;
            }catch(Exception e){}
        }
        return num;
    }

    /**Shows a request and waits for an answer from the player
     * @param request request
     * @return answer*/
    private String askForString(String request){
        System.out.print(request);
        return keyboard.next();
    }

    /**Shows a request and accepts as an answer only 'y' or 'n'
     * @param request request
     * @return true if user types 'y', false if user types 'n'*/
    private boolean askForBoolean(String request){
        String fromKeyboard;
        do {
            System.out.print(request);
            fromKeyboard = keyboard.next();
        } while (!fromKeyboard.toLowerCase().equals("y") && !fromKeyboard.toLowerCase().equals("n"));
        return fromKeyboard.toLowerCase().equals("y");
    }

    /**
     * Prints information about an action requested to the player
     * @param object object received from server
     * */
    private void displayRequestAction(SerializableRequestAction object){
        if (object.getWorker1Moves().size()>0) {
            System.out.print(textfields.getMoves1());
            displayCells((object).getWorker1Moves());
        }
        if (object.getWorker2Moves().size()>0) {
            System.out.print(textfields.getMoves2());
            displayCells((object).getWorker2Moves());
        }
        if (object.getWorker1Builds().size()>0) {
            System.out.print(textfields.getBuilds1());
            displayCells((object).getWorker1Builds());
        }
        if (object.getWorker2Builds().size()>0) {
            System.out.print(textfields.getBuilds2());
            displayCells((object).getWorker2Builds());
        }
        if ((object).isMoveOptional())
            System.out.println(textfields.getMovesopt());
        if ((object).isBuildOptional())
            System.out.println(textfields.getBuildsopt());

        if(!(object.canWorkerDoAction(1) && object.canWorkerDoAction(2))&&!(object.areBuildsEmpty()&&object.areMovesEmpty())) {
            int workerId = 0, i;
            for(i = 1; i<=2; i++) if (object.canWorkerDoAction(i)) workerId = i;
            System.out.println("worker id: "+ workerId);
        }
    }

    /**Checks if a move/build position with x,y coordinates is inside a set of possible choices
     * with x,y,z coordinates
     * @param position chosen position
     * @param collection set of possible choices
     * @return true if position is inside the set, false otherwise */
    private boolean isPositionCorrect (Position position, Set < Position > collection){
        if (position == null) return false;
        return collection.stream().anyMatch(x -> x.getX() == position.getX() && x.getY() == position.getY());
    }

    /** Prints game information about players */
    private void displayPlayersController(){
        System.out.println();
        for (int i = 0; i < board.getNumOfPlayers(); i++) {
            setColor(i+1);
            System.out.print(board.getPlayer(i + 1).getPlayerName());
            if (!board.getPlayer(i + 1).hasLost() && board.getPlayer(i + 1).getWorker(1).getX() != -1) {
                System.out.print(": " + textfields.getWorker1() + " (" + board.getPlayer(i + 1).getWorker(1).getX() + ", " + board.getPlayer(i + 1).getWorker(1).getY() + ")");
                System.out.print(", " + textfields.getWorker2() + " (" + board.getPlayer(i + 1).getWorker(2).getX() + ", " + board.getPlayer(i + 1).getWorker(2).getY() + ")");
                System.out.println(", " + board.getPlayer(i + 1).getGodPowerName());
            } else if (board.getPlayer(i + 1).getWorker(1).getX() == -1) {
                System.out.println(": "+board.getPlayer(i + 1).getGodPowerName());
            }
            else System.out.println(": lost");
        }
        System.out.print(Terminal.Color.WHITE.set());
    }

    /**
     * Prints the building texture if a zone contains a building with no workers on top of it
     * @param i x coordinate
     * @param j y coordinate
     * */
    private void showBuilding(int i, int j){
        System.out.print(Terminal.Color.BLUE.set());
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

    /**Prints the whole board and information about the game */
    private void displayBoard () {
        System.out.print(Terminal.Color.WHITE.set() + "   ╔═════╤═════╤═════╤═════╤═════╗\n");
        for (int j = 4; j >= 0; j--) {
            String element = "║";
            System.out.print(" " + j + " ");
            for (int i = 0; i < 5; i++) {
                boolean isThereAWorker = false;
                int playerId = 0;
                for (int k = 0; k < board.getNumOfPlayers(); k++) {
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
                System.out.print(Terminal.Color.WHITE.set() + element);
                if (isThereAWorker) {
                    System.out.print(" ");
                    showBuildingUnderWorker (i, j);
                    setColor(playerId);
                    System.out.print("■");
                    showBuildingUnderWorker (i, j);
                    System.out.print(" ");
                } else if (board.getCell(i, j) == null) System.out.print("     ");
                else if (board.getCell(i, j).isDome()) System.out.print(" " + Terminal.Color.BLUE.set() + "▲▲▲ ");
                else showBuilding(i, j);
                element = "│";
            }
            System.out.print(Terminal.Color.WHITE.set() + "║");
            if (j == 0)
                System.out.print(Terminal.Color.WHITE.set() + "\n   ╚═════╧═════╧═════╧═════╧═════╝\n      0     1     2     3     4");
            else System.out.print(Terminal.Color.WHITE.set() + "\n   ╟─────┼─────┼─────┼─────┼─────╢\n");
        }
        displayPlayersController();
    }

    /**
     * Prints the building texture if a zone contains a building and a worker on top of it
     * @param i x coordinate
     * @param j y coordinate
     * */
    private void showBuildingUnderWorker (int i, int j){
        if (board.getCell(i, j) == null) System.out.print(" ");
        else {
            System.out.print(Terminal.Color.BLUE.set());
            switch (board.getCell(i, j).getLevel()) {
                case 0:
                    System.out.print("░");
                    break;
                case 1:
                    System.out.print("▒");
                    break;
                case 2:
                    System.out.print("▓");
                    break;
            }
        }
    }

    /**
     * Sets a color based on a player ID
     * @param playerId player ID
     * */
    private void setColor(int playerId){
        switch (playerId) {
            case 1:
                System.out.print(Terminal.Color.RED.set());
                break;
            case 2:
                System.out.print(Terminal.Color.YELLOW.set());
                break;
            case 3:
                System.out.print(Terminal.Color.CYAN.set());
                break;
            default:
                System.out.print(Terminal.Color.WHITE.set());
                break;
        }
    }

    private enum Color {
        RED("\u001B[31m"),
        YELLOW("\u001B[33m"),
        CYAN("\u001B[36m"),
        BLUE("\u001B[34m"),
        WHITE("\u001B[37m");
        private String set;

        Color(String set) {
            this.set = set;
        }

        public String set() {return set;}
    }
}
