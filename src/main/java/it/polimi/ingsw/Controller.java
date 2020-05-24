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
    public Controller(Game game, ServerView serverView) {
        this.game = game;
        this.serverView = serverView;
    }

    // Decide quale player deve eseguire quale operazione, appoggiandosi alle informazioni di Turn e GodPower
    public void nextOperation(){
        int playerId = game.getTurn().getPlayerId();
        Turn turn = game.getTurn();
        SerializableRequest request;

        boolean canForceDome = game.getGodPowers().get(playerId-1).isAskToBuildDomes();
        Position worker1Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 1).getPosition();
        Position worker2Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 2).getPosition();

        Set<Position> worker1Moves = game.getGodPowers().get(playerId-1).move(worker1Position, game.getBoard(), game.getTurn());
        Set<Position> worker2Moves = game.getGodPowers().get(playerId-1).move(worker2Position, game.getBoard(), game.getTurn());
        Set<Position> worker1Builds = game.getGodPowers().get(playerId-1).build(worker1Position, game.getBoard(), game.getTurn());
        Set<Position> worker2Builds = game.getGodPowers().get(playerId-1).build(worker2Position, game.getBoard(), game.getTurn());

        if (checkLose(playerId, worker1Moves, worker1Builds, worker2Moves, worker2Builds, turn)) {
            return;
        }
        else if (checkWin (playerId, null, null)) {
            return;
        }
        else {
            request = new SerializableRequestAction(playerId,
                    turn.isMoveOptional(worker1Moves, worker2Moves),                    //controllo se le mosse sono opzionali o meno controllando i valori di turno
                    turn.isBuildOptional(worker1Builds, worker2Builds),
                    turn.canDecline(),                                                  //controllo se entrambi i valori sono a true
                    worker1Moves, worker2Moves, worker1Builds, worker2Builds, canForceDome);
            game.notifyAnswerOnePlayer(request);
            return;
        }
    }

    @Override
    // Termina il turno corrente
    public void onEndedTurn (int playerId) {
        int nextPlayerId = nextPlayerId(playerId);
        Turn newTurn = game.getGodPowers().get(playerId-1).endTurn(game.getTurn(), game.getGodPowers(), game.getPlayers().get(nextPlayerId-1)); // termina il turno precedente, serve l'end turn del giocatore corrente
        game.setTurn(newTurn); // setta il turno successivo
        SerializableUpdate update = new SerializableUpdateTurn(nextPlayerId);
        game.notifyJustUpdateAll(update); // aggiorna i players del cambio turno
        nextOperation(); // apre una nuova operazione
    }

    @Override
    // Gestisce una ConsolidateMove
    public void onConsolidateMove(int playerId, int workerId, Position newPosition) {
        Position workerPosition = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), workerId).getPosition();
        game.getGodPowers().get(playerId-1).moveInto(game.getBoard(), workerPosition, newPosition); // consolida la mossa
        game.getTurn().updateTurnInfoAfterMove(workerPosition, newPosition, game.getBoard()); // aggiorna Turn
        SerializableUpdate update = new SerializableUpdateMove(newPosition, playerId, workerId);
        game.notifyJustUpdateAll(update); // aggiorna i players della move

        checkWin(playerId, workerPosition, newPosition); // se la move determina una vittoria apre la procedura di vittoria...
        nextOperation(); //...altrimenti apre una nuova operazione
    }

    @Override
    // Gestisce una ConsolidateBuild
    public void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome) {
        game.getGodPowers().get(playerId-1).buildUp(newPosition, game.getBoard(), forceDome); // consolida la build
        game.getTurn().updateTurnInfoAfterBuild(newPosition); // aggiorna Turn
        SerializableUpdate update = new SerializableUpdateBuild(newPosition, game.getBoard().getCell(newPosition).isDome());
        game.notifyJustUpdateAll(update); // aggiorna i players della build
        nextOperation(); // apre una nuova operazione
    }

    @Override
    // Notifica i player della disconnessione
    public void onPlayerDisconnection(int playerId) {
        if (game.getPlayers().get(playerId-1)!=null) { // Se la disconnessione di un player non è dovuta a una sconfitta...
            SerializableUpdate update = new SerializableUpdateDisconnection(playerId);
            game.notifyJustUpdateAll(update); // aggiorna i players della disconnessione
            serverView.stopAllEventGenerators(); // termina tutti i thread legati alla partita
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
        Position worker1Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 1).getPosition();
        Position worker2Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 2).getPosition();

        Turn newTurn = game.getGodPowers().get(playerId-1).endTurn(game.getTurn(), game.getGodPowers(), game.getPlayers().get(nextPlayerId-1)); // termina il turno precedente
        game.setTurn(newTurn); // setta il turno successivo
        game.getPlayers().set(playerId-1, null); // setta il Player a null
        game.getGodPowers().set(playerId-1, null); // setta il GodPower a null
        game.getBoard().getCell(worker1Position).setWorker(null); // rimuove i worker dalle celle
        game.getBoard().getCell(worker2Position).setWorker(null);
        List<SerializableUpdate> tempUpdates = new ArrayList<>();
        tempUpdates.add( new SerializableUpdateLoser(playerId));
        tempUpdates.add(new SerializableUpdateTurn(nextPlayerId));
        game.notifyJustUpdateAll(tempUpdates); // aggiorna i players della sconfitta
        nextOperation(); // apre una nuova operazione
    }


    //controllo di tutte le condizioni di vittoria, globali o in seguito ad una mossa
    //restituisce true in caso di vittoria
    private boolean checkWin (int playerId, Position workerPosition, Position destinationPosition) {
        if (game.getPlayers().stream().filter(Objects::nonNull).count()==1) { // controlla se è rimasto solo il giocatore corrente
            onPlayerWin(playerId);
            return true;
        }
        if (workerPosition == null && destinationPosition == null) {
            for (GodPower p: game.getGodPowers()) { // controlla le condizioni di vittoria di tutti i players
                if (p!=null) {
                    if (p.win(null, null, game.getBoard())) {
                        onPlayerWin(p.getPlayerId());
                        return true;
                    }
                }
            }
        }
        else {
            if (game.getGodPowers().get(playerId-1).win(workerPosition, destinationPosition, game.getBoard())) {
                onPlayerWin(playerId);
                return true;
            }
        }
        return false;
    }

    private boolean checkLose (int playerId, Set<Position> worker1Moves, Set<Position> worker1Builds, Set<Position> worker2Moves, Set<Position> worker2Builds, Turn turn) {
        StandardLoseCondition standardLoseCondition = game.getGodPowers().get(playerId-1).getLoseCondition();

        if (!turn.canDecline() && standardLoseCondition.lose(worker1Moves, worker1Builds) && standardLoseCondition.lose(worker2Moves, worker2Builds)) {
            onPlayerLoss(playerId);
            return true;
        }
        return false;
    }

    @Override
    // Primo metodo lanciato del controller, avvia MVC e procedura di InitializeGame
    public void onInitialization(){
        try {
            godPowersLeft = GodPowerManager.createGodPowers(game.getNumOfPlayers());
            List<String> godPowersNames = getGodPowersLeftNames();
            List<String> playersNames = new ArrayList<>();
            for (Player player : game.getPlayers()) playersNames.add(player.getName());
            SerializableUpdateInitializeNames update = new SerializableUpdateInitializeNames(playersNames);
            SerializableRequest request = new SerializableRequestInitializeGame(1, godPowersNames);
            game.notifyUpdateAllAndAnswerOnePlayer(update, request);
        } catch (Exception e){}
    }

    @Override
    // Prosegue nella procedura di InitializeGame avanzando di un player
    public void onInitialization(int playerId, List<Position> workerPositions, String godPower) {
        chooseGodPower(godPower);
        Worker worker1 = new Worker(game.getPlayers().get(playerId-1), 1);
        Worker worker2 = new Worker(game.getPlayers().get(playerId-1), 2);
        game.getPlayers().get(playerId-1).addWorker(worker1);
        game.getPlayers().get(playerId-1).addWorker(worker2);
        Cell worker1Cell = game.getBoard().getCell(workerPositions.get(0));
        Cell worker2Cell = game.getBoard().getCell(workerPositions.get(1));
        worker1Cell.setWorker(worker1);
        worker2Cell.setWorker(worker2);
        SerializableUpdateInitializeGame update = new SerializableUpdateInitializeGame(workerPositions, godPower, playerId);
        if (playerId == game.getPlayers().size()){ // tutti i worker sono pronti, il primo turno ha inizio
            SerializableUpdateTurn updateTurn = new SerializableUpdateTurn(1);
            game.setTurn(new Turn(game.getPlayers().get(0)));
            List <SerializableUpdate> tempUpdates = new ArrayList<>();
            tempUpdates.add(update);
            tempUpdates.add(updateTurn);
            game.notifyJustUpdateAll(tempUpdates);
            nextOperation();
        } else {
            SerializableRequest request = new SerializableRequestInitializeGame(playerId + 1, getGodPowersLeftNames());
            game.notifyUpdateAllAndAnswerOnePlayer(update, request);
        }
    }

    // restituisce il giocatore successivo a quello corrente
    private int nextPlayerId(int playerId){
        int firstPlayerId = (playerId % game.getNumOfPlayers()) + 1;
        for (int i = firstPlayerId; i < firstPlayerId + game.getNumOfPlayers() - 1; i++)
            if (game.getPlayers().get(((i-1) % game.getNumOfPlayers())) != null) return ((i-1) % game.getNumOfPlayers()) +1;
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
                game.getGodPowers().add(godPowersLeft.get(i));
                godPowersLeft.remove(i);
                break;
            }
        }
    }
}
