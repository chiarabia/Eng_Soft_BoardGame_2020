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

public class Controller implements ProxyObserver {
    private Game game;
    private List <GodPower> godPowersLeft;
    private ServerView serverView;
    private boolean isDisconnected = false;

    public Controller(Game game, ServerView serverView) {
        this.game = game;
        this.serverView = serverView;
    }

    // Decide quale player deve eseguire quale operazione, appoggiandosi alle informazioni di Turn e GodPower
    public void nextOperation(){
        Turn turn = getTurn();
        Board board = getBoard();
        int playerId = turn.getPlayerId();
        SerializableRequest request;

        boolean canForceDome = getPlayerGodPower(playerId).isAskToBuildDomes();
        Position worker1Position = getWorkerPosition(playerId, 1);
        Position worker2Position = getWorkerPosition(playerId, 2);

        Set<Position> worker1Moves  =  getPlayerGodPower(playerId).move(worker1Position , board, turn);
        Set<Position> worker1Builds =  getPlayerGodPower(playerId).build(worker1Position, board, turn);
        Set<Position> worker2Moves  =  getPlayerGodPower(playerId).move(worker2Position , board, turn);
        Set<Position> worker2Builds =  getPlayerGodPower(playerId).build(worker2Position, board, turn);

        if (checkLose(playerId, worker1Moves, worker1Builds, worker2Moves, worker2Builds, turn)) {
            //return;
        }
        else if (checkWin (playerId, null, null)) {
           // return;
        }
        else {
            request = new SerializableRequestAction(playerId,
                        turn.isMoveOptional(worker1Moves, worker2Moves),                    //controllo se le mosse sono opzionali o meno controllando i valori di turno
                        turn.isBuildOptional(worker1Builds, worker2Builds),
                        turn.canDecline(),                                                  //controllo se entrambi i valori sono a true
                        worker1Moves, worker2Moves,
                        worker1Builds, worker2Builds,
                        canForceDome);

            game.notifyAnswerOnePlayer(request);
            return;
        }
    }

    @Override
    // Termina il turno corrente
    public void onEndedTurn (int playerId) {
        int nextPlayerId = nextPlayerId(playerId);
        Turn newTurn = getPlayerGodPower(playerId).endTurn(getTurn(), getGodPowers(), getPlayer(nextPlayerId)); // termina il turno precedente, serve l'end turn del giocatore corrente
        game.setTurn(newTurn); // setta il turno successivo
        SerializableUpdate update = new SerializableUpdateTurn(nextPlayerId);
        game.notifyJustUpdateAll(update); // aggiorna i players del cambio turno
        nextOperation(); // apre una nuova operazione
    }

    @Override
    // Gestisce una ConsolidateMove
    public void onConsolidateMove(int playerId, int workerId, Position newPosition) {
        Board board = getBoard();
        Position workerPosition = getWorkerPosition(playerId, workerId);
        getPlayerGodPower(playerId).moveInto(board, workerPosition, newPosition); // consolida la mossa
        getTurn().updateTurnInfoAfterMove(workerPosition, newPosition, board); // aggiorna Turn
        SerializableUpdate update = new SerializableUpdateMove(newPosition, playerId, workerId);
        game.notifyJustUpdateAll(update); // aggiorna i players della move

        checkWin(playerId, workerPosition, newPosition); // se la move determina una vittoria apre la procedura di vittoria...
        nextOperation(); //...altrimenti apre una nuova operazione
    }

    @Override
    // Gestisce una ConsolidateBuild
    public void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome) {
        Board board = getBoard();
        getPlayerGodPower(playerId).buildUp(newPosition, board, forceDome); // consolida la build
        getTurn().updateTurnInfoAfterBuild(newPosition); // aggiorna Turn
        SerializableUpdate update = new SerializableUpdateBuild(newPosition, board.getCell(newPosition).isDome());
        game.notifyJustUpdateAll(update); // aggiorna i players della build
        nextOperation(); // apre una nuova operazione
    }

    @Override
    // Notifica i player della disconnessione
    public void onPlayerDisconnection(int playerId) {
        if (getPlayer(playerId)!=null && !isDisconnected) { // Se la disconnessione di un player non è dovuta a una sconfitta e se non è ancora stato lanciato il segnale di disconnessione...
            SerializableUpdate update = new SerializableUpdateDisconnection(playerId);
            game.notifyJustUpdateAll(update); // aggiorna i players della disconnessione
            serverView.stopAllEventGenerators(); // termina tutti i thread legati alla partita
            isDisconnected = true; // dichiara la disconnessione avvenuta
        }
    }

    // Notifica i players della vittoria
    public void onPlayerWin (int playerId)  {
        game.notifyJustUpdateAll(new SerializableUpdateWinner(playerId)); // avvisa i players della vittoria
        serverView.stopAllEventGenerators(); // termina tutti i thread legati alla partita
    }

    // Gestisce la sconfitta di un giocatore
    public void onPlayerLoss(int playerId)  {
        int nextPlayerId = nextPlayerId(playerId);
        List<GodPower> godPowerList = getGodPowers();

        Turn newTurn = getPlayerGodPower(playerId).endTurn(getTurn(), godPowerList, getPlayer(nextPlayerId)); // termina il turno precedente
        game.setTurn(newTurn); // setta il turno successivo
        removePlayerInfos(playerId); //rimuove tutte le informazioni del giocatore dalla partita

        List<SerializableUpdate> tempUpdates = new ArrayList<>();
        tempUpdates.add( new SerializableUpdateLoser(playerId));
        tempUpdates.add(new SerializableUpdateTurn(nextPlayerId));
        game.notifyJustUpdateAll(tempUpdates); // aggiorna i players della sconfitta
        nextOperation(); // apre una nuova operazione
    }


    //controllo di tutte le condizioni di vittoria, globali o in seguito ad una mossa
    //restituisce true in caso di vittoria
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

    private boolean checkLose (int playerId, Set<Position> worker1Moves, Set<Position> worker1Builds, Set<Position> worker2Moves, Set<Position> worker2Builds, Turn turn) {
        StandardLoseCondition playerLoseCondition = getPlayerGodPower(playerId).getLoseCondition();

        if (!turn.canDecline() && playerLoseCondition.lose(worker1Moves, worker1Builds) && playerLoseCondition.lose(worker2Moves, worker2Builds)) {
            onPlayerLoss(playerId);
            return true;
        }
        return false;
    }

    @Override
    // Primo metodo lanciato del controller, avvia MVC e procedura di InitializeGame
    public void onGodPowerInitialization(){
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

    @Override
    // Prosegue nella procedura di InitializeGame avanzando di un player
    public void onGodPowerInitialization(int playerId, String godPower) {
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


    @Override
    // Primo metodo lanciato del controller, avvia MVC e procedura di InitializeGame
    public void onWorkerPositionsInitialization(){
        try {
            SerializableRequest request = new SerializableRequestInitializeWorkerPositions(1);
            game.notifyAnswerOnePlayer(request);
        } catch (Exception e){}
    }

    @Override
    // Prosegue nella procedura di InitializeGame avanzando di un player
    public void onWorkerPositionsInitialization(int playerId, List<Position> workerPositions) {
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
            SerializableUpdateTurn updateTurn = new SerializableUpdateTurn(1);
            game.setTurn(new Turn(getPlayers().get(0)));
            List <SerializableUpdate> tempUpdates = new ArrayList<>();
            tempUpdates.add(update);
            tempUpdates.add(updateTurn);
            game.notifyJustUpdateAll(tempUpdates);
            System.out.println("Game started");
            nextOperation();
        } else {
            SerializableRequest request = new SerializableRequestInitializeWorkerPositions(playerId + 1);
            game.notifyUpdateAllAndAnswerOnePlayer(update, request);
        }
    }

    // restituisce il giocatore successivo a quello corrente
    private int nextPlayerId(int playerId){
        int firstPlayerId = (playerId % getNumOfPlayers()) + 1;
        for (int i = firstPlayerId; i < firstPlayerId + getNumOfPlayers() - 1; i++)
            if (getPlayer((i-1) %getNumOfPlayers()+1) != null) return ((i-1) % getNumOfPlayers()) +1;
        return playerId;
    }

    // metodo riservato per onInitialization(...)
    private List<String> getGodPowersLeftNames (){
        List<String> godPowersNames = new ArrayList<>();
        for (GodPower godPower: godPowersLeft) godPowersNames.add(godPower.getGodName());
        return godPowersNames;
    }

    // metodo riservato per onInitialization(...)
    private void chooseGodPower(String godPower){
        for (int i = 0; i < godPowersLeft.size(); i++){
            if (godPowersLeft.get(i).getGodName().equals(godPower)){
                getGodPowers().add(godPowersLeft.get(i));
                godPowersLeft.remove(i);
                break;
            }
        }
    }

    private void removePlayerInfos (int playerId) {
        getBoard().getCell(getWorkerPosition(playerId, 1)).setWorker(null); // rimuove i worker dalle celle
        getBoard().getCell(getWorkerPosition(playerId, 2)).setWorker(null);
        game.removeGodPower(playerId); // setta il GodPower a null
        game.removePlayer(playerId); // setta il Player a null


    }

    private int getNumOfPlayers() {return game.getNumOfPlayers();}
    private Board getBoard() { return game.getBoard(); }
    private Turn getTurn() { return game.getTurn(); }
    private List<Player> getPlayers() { return game.getPlayers(); }
    private List<GodPower> getGodPowers() { return game.getGodPowers(); }
    private Player getPlayer(int playerId) {
        return game.getPlayer(playerId);
    }
    private GodPower getPlayerGodPower(int playerId) {
        return game.getPlayerGodPower(playerId);
    }
    private Worker getPlayerWorker (int playerId, int workerId) {
        return game.getPlayerWorker(playerId, workerId);
    }
    private Position getWorkerPosition(int playerId, int workerId) {
        return game.getWorkerPosition(playerId, workerId);
    }

    public List<GodPower> getGodPowersLeft() {
        return godPowersLeft;
    }
}
