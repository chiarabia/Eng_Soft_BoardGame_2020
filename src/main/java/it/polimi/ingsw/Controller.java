package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import it.polimi.ingsw.effects.winCondition.StandardLoseCondition;
import it.polimi.ingsw.server.ProxyObserver;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.serializable.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Controller implements ProxyObserver {
    private Game game;
    private List <GodPower> godPowersLeft;
    private ServerView serverView;
    private boolean isDisconnected = false;

    public Controller(Game game, ServerView serverView) {
        this.game = game;
        this.serverView = serverView;
    }

    /**This method decides which player should perform which action, relying on Turn and GodPower information */
    public void nextOperation(){
        int playerId = getTurn().getPlayerId();
        boolean canForceDome = getPlayerGodPower(playerId).isAskToBuildDomes();
        Position worker1Position = getWorkerPosition(playerId, 1);
        Position worker2Position = getWorkerPosition(playerId, 2);

        Set<Position> worker1Moves  =  getPlayerGodPower(playerId).move(worker1Position , getBoard(), getTurn());
        Set<Position> worker1Builds =  getPlayerGodPower(playerId).build(worker1Position, getBoard(), getTurn());
        Set<Position> worker2Moves  =  getPlayerGodPower(playerId).move(worker2Position , getBoard(), getTurn());
        Set<Position> worker2Builds =  getPlayerGodPower(playerId).build(worker2Position, getBoard(), getTurn());

        if (checkLose(playerId, worker1Moves, worker1Builds, worker2Moves, worker2Builds, getTurn())) return;
        if (checkWin (playerId, null, null)) return;

        SerializableRequest request = new SerializableRequestAction(playerId,
                                            getTurn().isMoveOptional(worker1Moves, worker2Moves),                    //controllo se le mosse sono opzionali o meno controllando i valori di turno
                                            getTurn().isBuildOptional(worker1Builds, worker2Builds),
                                            getTurn().canDecline(),                                                  //controllo se entrambi i valori sono a true
                                            worker1Moves, worker2Moves,
                                            worker1Builds, worker2Builds,
                                            canForceDome);

        game.notifyAnswerOnePlayer(request);
    }

    /**This method terminates current turn
     *@param playerId player who asked to end the turn
     */
    @Override
    public void onEndedTurn (int playerId) {
        if (!(playerId == getTurn().getPlayerId())){ // INITIAL CHECK
            game.notifyAnswerOnePlayer(new SerializableRequestError(playerId, "REJECTED"));
            return;
        }

        int nextPlayerId = nextPlayerId(playerId);
        Turn newTurn = getPlayerGodPower(playerId).endTurn(getTurn(), getGodPowers(), getPlayer(nextPlayerId)); // termina il turno precedente, serve l'end turn del giocatore corrente
        game.setTurn(newTurn); // setta il turno successivo
        SerializableUpdate update = new SerializableUpdateTurn(nextPlayerId, false);
        game.notifyJustUpdateAll(update); // aggiorna i players del cambio turno
        nextOperation(); // apre una nuova operazione
    }

    /**This method manages a ConsolidateMove object received from a client
     *@param playerId player who sent the object
     *@param workerId workerId to be modified
     *@param newPosition position chosen by player*/
    @Override
    public void onConsolidateMove(int playerId, int workerId, Position newPosition) {
        Position worker1Position = getWorkerPosition(getTurn().getPlayerId(), 1);
        Position worker2Position = getWorkerPosition(getTurn().getPlayerId(), 2);
        Set<Position> worker1Moves  =  getPlayerGodPower(getTurn().getPlayerId()).move(worker1Position , getBoard(), getTurn());
        Set<Position> worker2Moves  =  getPlayerGodPower(getTurn().getPlayerId()).move(worker2Position , getBoard(), getTurn());

        if (!(playerId == getTurn().getPlayerId() && // INITIAL CHECK
            (workerId == 1 || workerId == 2) &&
            newPosition != null &&
            (workerId==1 && worker1Moves.stream().anyMatch(x->x.equals(newPosition)) || workerId==2 && worker2Moves.stream().anyMatch(x->x.equals(newPosition))) )){
                game.notifyAnswerOnePlayer(new SerializableRequestError(playerId, "REJECTED"));
                return;
        }

        Board board = getBoard();
        Position workerPosition = getWorkerPosition(playerId, workerId);
        SerializableUpdate update = getPlayerGodPower(playerId).moveInto(board, workerPosition, newPosition); // consolida la mossa
        getTurn().updateTurnInfoAfterMove(workerPosition, newPosition, board); // aggiorna Turn
        game.notifyJustUpdateAll(update); // aggiorna i players della move

        checkWin(playerId, workerPosition, newPosition); // se la move determina una vittoria apre la procedura di vittoria...
        nextOperation(); //...altrimenti apre una nuova operazione
    }

    /**This method manages a ConsolidateBuild object received from a client
     *@param playerId player who sent the object
     *@param forceDome true if client asked to force a dome
     *@param newPosition position chosen by player*/
    @Override
    public void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome) {
        Position worker1Position = getWorkerPosition(getTurn().getPlayerId(), 1);
        Position worker2Position = getWorkerPosition(getTurn().getPlayerId(), 2);
        Set<Position> worker1Builds =  getPlayerGodPower(getTurn().getPlayerId()).build(worker1Position, getBoard(), getTurn());
        Set<Position> worker2Builds =  getPlayerGodPower(getTurn().getPlayerId()).build(worker2Position, getBoard(), getTurn());

        if (!(playerId == getTurn().getPlayerId() && // INITIAL CHECK
            newPosition != null &&
            (!forceDome || getPlayerGodPower(playerId).isAskToBuildDomes()) &&
            (worker1Builds.stream().anyMatch(x->x.equals(newPosition))||worker2Builds.stream().anyMatch(x->x.equals(newPosition))) )){
                game.notifyAnswerOnePlayer(new SerializableRequestError(playerId, "REJECTED"));
                return;
        }


        Board board = getBoard();
        SerializableUpdate update = getPlayerGodPower(playerId).buildUp(newPosition, board, forceDome); // consolida la build
        getTurn().updateTurnInfoAfterBuild(newPosition); // aggiorna Turn
        game.notifyJustUpdateAll(update); // aggiorna i players della build
        nextOperation(); // apre una nuova operazione
    }

    /**This method manages a client disconnection
     *@param playerId player who disconnected*/
    @Override
    public void onPlayerDisconnection(int playerId) {
        if (getPlayer(playerId)!=null && !isDisconnected) { // Se la disconnessione di un player non è dovuta a una sconfitta e se non è ancora stato lanciato il segnale di disconnessione...
            isDisconnected = true; // dichiara la disconnessione avvenuta
            SerializableUpdate update = new SerializableUpdateDisconnection(playerId);
            game.notifyJustUpdateAll(update); // aggiorna i players della disconnessione
            serverView.stopAllEventGenerators(); // termina tutti i thread legati alla partita
        }
    }

    /**This method reports to players that someone has won
     *@param playerId player who has won*/
    public void onPlayerWin (int playerId)  {
        isDisconnected = true;
        game.notifyJustUpdateAll(new SerializableUpdateWinner(playerId)); // avvisa i players della vittoria
        serverView.stopAllEventGenerators(); // termina tutti i thread legati alla partita
    }

    /**This method reports to players that someone has lost
     *@param playerId player who has lost*/
    public void onPlayerLoss(int playerId)  {
        int nextPlayerId = nextPlayerId(playerId);
        List<GodPower> godPowerList = getGodPowers();

        Turn newTurn = getPlayerGodPower(playerId).endTurn(getTurn(), godPowerList, getPlayer(nextPlayerId)); // termina il turno precedente
        game.setTurn(newTurn); // setta il turno successivo
        removePlayerInfos(playerId); //rimuove tutte le informazioni del giocatore dalla partita

        List<SerializableUpdate> tempUpdates = new ArrayList<>();
        tempUpdates.add( new SerializableUpdateLoser(playerId));
        tempUpdates.add(new SerializableUpdateTurn(nextPlayerId, false));
        game.notifyJustUpdateAll(tempUpdates); // aggiorna i players della sconfitta
        nextOperation(); // apre una nuova operazione
    }

    /**First controller method thrown, it starts initialization procedures
     * and asks the first player to choose a GodPower */
    @Override
    public void onInitialization(){
        System.out.println("Game initialization started");
        try {
            godPowersLeft = GodPowerManager.createGodPowers(getNumOfPlayers());
            List<String> godPowersNames = getGodPowersLeftNames();
            List<String> playersNames = new ArrayList<>();
            for (Player player : getPlayers()) playersNames.add(player.getName());
            SerializableUpdateInitializeNames update = new SerializableUpdateInitializeNames(playersNames);
            SerializableRequest request = new SerializableRequestInitializeGodPower(1, godPowersNames);
            game.notifyUpdateAllAndAnswerOnePlayer(update, request);
        } catch (Exception e){}
    }

    /**This method proceeds the god powers initialization, sets the god power chosen
     * by a player and asks next player to chose a remaining one, or, if no players are left,
     * it starts the workers positions initialization
     *@param playerId player who has chosen god power
     *@param godPower name of the god power chosen */
    @Override
    public void onGodPowerInitialization(int playerId, String godPower) {
        if (!(playerId > 0 && playerId <= getNumOfPlayers() && // INITIAL CHECK
                getGodPowers().size() == playerId - 1 &&
                godPower != null &&
                godPowersLeft.stream().map(GodPower::getGodName).anyMatch(x->x.equals(godPower)))){
            game.notifyAnswerOnePlayer(new SerializableRequestError(playerId, "REJECTED"));
            return;
        }

        chooseGodPower(godPower);

        SerializableUpdateInitializeGodPower update = new SerializableUpdateInitializeGodPower(godPower, playerId);
        if (playerId == getPlayers().size()){ // tutti i god powers sono stati scelti
            game.notifyJustUpdateAll(update);
            onWorkerPositionsInitialization();
        } else {
            SerializableRequest request = new SerializableRequestInitializeGodPower(playerId + 1, getGodPowersLeftNames());
            game.notifyUpdateAllAndAnswerOnePlayer(update, request);
        }
    }


    /**This method starts the workers positions initialization by asking the first player to choose*/
    public void onWorkerPositionsInitialization(){
        try {
            SerializableRequest request = new SerializableRequestInitializeWorkerPositions(getBoard()
                    .getStream()
                    .filter(Cell::isFree)
                    .map(Cell::getPosition)
                    .collect(Collectors.toList()), 1);
            game.notifyAnswerOnePlayer(request);
        } catch (Exception e){}
    }

    /**This method proceeds the workers positions initialization, sets positions chosen
     * by a player and asks next player to chose, or, if no players are left, first game turn starts
     *@param playerId player who has chosen positions
     *@param workerPositions positions chosen */
    @Override
    public void onWorkerPositionsInitialization(int playerId, List<Position> workerPositions) {
        if (!(playerId > 0 && playerId <= getNumOfPlayers() && // INITIAL CHECK
                getPlayer(playerId) !=null &&
                workerPositions != null &&
                workerPositions.size() == 2 &&
                !workerPositions.get(0).equals(workerPositions.get(1)) &&
                workerPositions.stream().allMatch(x->x.getZ()==0) &&
                !getPlayer(playerId).areWorkersSet() &&
                ((playerId > 1 && getPlayer(playerId-1).areWorkersSet())||playerId==1) &&
                getBoard().isFreeZone(workerPositions.get(0).getX(), workerPositions.get(0).getY()) &&
                getBoard().isFreeZone(workerPositions.get(1).getX(), workerPositions.get(1).getY()) )){
            game.notifyAnswerOnePlayer(new SerializableRequestError(playerId, "REJECTED"));
            return;
        }

        Player player = getPlayer(playerId);
        Worker worker1 = new Worker(player, 1);
        Worker worker2 = new Worker(player, 2);
        player.addWorker(worker1);
        player.addWorker(worker2);
        Cell worker1Cell = getBoard().getCell(workerPositions.get(0));
        Cell worker2Cell = getBoard().getCell(workerPositions.get(1));
        worker1Cell.setWorker(worker1);
        worker2Cell.setWorker(worker2);

        SerializableUpdateInitializeWorkerPositions update = new SerializableUpdateInitializeWorkerPositions(workerPositions, playerId);
        if (playerId == getPlayers().size()){ // tutti i worker sono pronti, il primo turno ha inizio
            SerializableUpdateTurn updateTurn = new SerializableUpdateTurn(1, true);
            game.setTurn(new Turn(getPlayers().get(0)));
            List <SerializableUpdate> tempUpdates = new ArrayList<>();
            tempUpdates.add(update);
            tempUpdates.add(updateTurn);
            game.notifyJustUpdateAll(tempUpdates);
            System.out.println("Game started");
            nextOperation();
        } else {
            SerializableRequest request = new SerializableRequestInitializeWorkerPositions(getBoard().getStream().filter(Cell::isFree).map(Cell::getPosition).collect(Collectors.toList()),playerId + 1);
            game.notifyUpdateAllAndAnswerOnePlayer(update, request);
        }
    }


    /**This method check if a player has won
     *@param playerId player to be checked
     *@param workerPosition beginning cell of a worker's move, null if no movement is happening
     *@param destinationPosition final cell of a worker's move, null if no movement is happening*/
    private boolean checkWin (int playerId, Position workerPosition, Position destinationPosition) {
        if (getPlayers().stream().filter(Objects::nonNull).count()==1) { // controlla se è rimasto solo il giocatore corrente
            onPlayerWin(playerId);
            return true;
        }
        if (workerPosition == null && destinationPosition == null) {
            for (GodPower p: getGodPowers()) { // controlla le condizioni di vittoria di tutti i players
                if (p!=null) {
                    if (p.win(null, null, getBoard())) {
                        onPlayerWin(p.getPlayerId());
                        return true;
                    }
                }
            }
        }
        else {
            if (getPlayerGodPower(playerId).win(workerPosition, destinationPosition, getBoard())) {
                onPlayerWin(playerId);
                return true;
            }
        }
        return false;
    }

    /**This method check if a player has lost
     *@param turn current turn
     *@param playerId player to be checked
     *@param worker1Moves worker 1 moves
     *@param worker1Builds worker 1 builds
     *@param worker2Moves worker 2 moves
     *@param worker2Builds worker 2 builds */
    private boolean checkLose (int playerId, Set<Position> worker1Moves, Set<Position> worker1Builds, Set<Position> worker2Moves, Set<Position> worker2Builds, Turn turn) {
        StandardLoseCondition playerLoseCondition = getPlayerGodPower(playerId).getLoseCondition();

        if (!turn.canDecline() && playerLoseCondition.lose(worker1Moves, worker1Builds) && playerLoseCondition.lose(worker2Moves, worker2Builds)) {
            onPlayerLoss(playerId);
            return true;
        }
        return false;
    }

    /**This method returns a list of names of still not chosen god powers
     * @return List<String>
     */
    private List<String> getGodPowersLeftNames (){
        List<String> godPowersNames = new ArrayList<>();
        for (GodPower godPower: godPowersLeft) godPowersNames.add(godPower.getGodName());
        return godPowersNames;
    }

    /**This method extracts a god power from the list of still not chosen ones
     * and adds it to god powers list in game model
     *@param godPower name of chosen god power
     */
    private void chooseGodPower(String godPower){
        for (int i = 0; i < godPowersLeft.size(); i++){
            if (godPowersLeft.get(i).getGodName().equals(godPower)){
                getGodPowers().add(godPowersLeft.get(i));
                godPowersLeft.remove(i);
                break;
            }
        }
    }

    /** This method sets GodPower and Player to null and removes workers belonging to a player
     * @param playerId player to be removed
     */
    private void removePlayerInfos (int playerId) {
        getBoard().getCell(getWorkerPosition(playerId, 1)).setWorker(null); // rimuove i worker dalle celle
        getBoard().getCell(getWorkerPosition(playerId, 2)).setWorker(null);
        game.removeGodPower(playerId); // setta il GodPower a null
        game.removePlayer(playerId); // setta il Player a null
    }

    /**This method returns next player compared to the one passed by argument
     * @param playerId player ID
     */
    private int nextPlayerId(int playerId){
        int firstPlayerId = (playerId % getNumOfPlayers()) + 1;
        for (int i = firstPlayerId; i < firstPlayerId + getNumOfPlayers() - 1; i++)
            if (getPlayer((i-1) %getNumOfPlayers()+1) != null) return ((i-1) % getNumOfPlayers()) +1;
        return playerId;
    }

    private int getNumOfPlayers() {return game.getNumOfPlayers();}
    private Board getBoard() { return game.getBoard(); }
    private Turn getTurn() { return game.getTurn(); }
    private List<Player> getPlayers() { return game.getPlayers(); }
    private List<GodPower> getGodPowers() { return game.getGodPowers(); }
    private Player getPlayer(int playerId) {return game.getPlayer(playerId); }
    private GodPower getPlayerGodPower(int playerId) { return game.getPlayerGodPower(playerId); }
    private Position getWorkerPosition(int playerId, int workerId) { return game.getWorkerPosition(playerId, workerId); }
    public List<GodPower> getGodPowersLeft() { return godPowersLeft; }
}
