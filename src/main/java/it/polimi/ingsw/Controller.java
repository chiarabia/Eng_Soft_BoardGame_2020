package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import it.polimi.ingsw.effects.winCondition.StandardLoseCondition;
import it.polimi.ingsw.effects.winCondition.StandardWinCondition;
import it.polimi.ingsw.server.ProxyObserver;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.serializable.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;

public class Controller implements ProxyObserver {
    private Game game;
    private List <GodPower> godPowersLeft;
    private ServerView serverView;
    public Controller(Game game, ServerView serverView) {
        this.game = game;
        this.serverView = serverView;
    }

    // Decide quale player deve eseguire quale operazione, appoggiandosi alle informazioni di Turn e GodPower
    public void nextOperation(){ //todo: Questo metodo per ora gestisce solo un turno classico (move + build)
        int playerId = game.getTurn().getPlayerId();
        boolean canForceDome = game.getGodPowers().get(playerId-1).isAskToBuildDomes();
        Position worker1Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 1).getPosition();
        Position worker2Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 2).getPosition();
        Set<Position> worker1Moves = game.getGodPowers().get(playerId-1).move(worker1Position, game.getBoard(), game.getTurn());
        Set<Position> worker2Moves = game.getGodPowers().get(playerId-1).move(worker2Position, game.getBoard(), game.getTurn());
        Set<Position> worker1Builds = game.getGodPowers().get(playerId-1).build(worker1Position, game.getBoard(), game.getTurn());
        Set<Position> worker2Builds = game.getGodPowers().get(playerId-1).build(worker2Position, game.getBoard(), game.getTurn());

        if (!checkWinOrLose(playerId, worker1Moves, worker2Moves, worker1Builds, worker1Builds)) { // controlla se ha vinto e nel caso salta ogni altra procedura
            if (!game.getTurn().isMoveBeforeBuild()) { // deve ancora muovere e poi costruire
                SerializableRequest request = new SerializableRequestMove(playerId, worker1Moves, worker2Moves);
                game.notifyAnswerOnePlayer(request);
            } else if (game.getTurn().isMoveBeforeBuild() && !game.getTurn().isBuildAfterMove()) { // deve ancora costruire
                SerializableRequest request = new SerializableRequestBuild(playerId, worker1Builds, worker2Builds, canForceDome);
                game.notifyAnswerOnePlayer(request);
            } else if (game.getTurn().isBuildAfterMove()) { // turno terminato
                onEndedTurn(playerId);
            }
        }
    }

    @Override
    // Termina il turno corrente, setta il nuovo oggetto Turn e invia l'update relativo al cambio di turno
    public void onEndedTurn (int playerId) {
        int nextPlayerId = nextPlayerId(playerId);
        Turn newTurn = game.getGodPowers().get(playerId-1).endTurn(game.getTurn(), game.getGodPowers(), game.getPlayers().get(nextPlayerId-1));
        game.setTurn(newTurn);
        SerializableUpdate update = new SerializableUpdateTurn(nextPlayerId);
        game.notifyJustUpdateAll(update);
        nextOperation();
    }

    @Override
    // Consolida la move, aggiorna Turn, invia il relativo oggetto update e verifica se il player nell'effettuare tale mossa abbia vinto
    public void onConsolidateMove(int playerId, int workerId, Position newPosition) {
        Position workerPosition = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), workerId).getPosition();
        game.getGodPowers().get(playerId-1).moveInto(game.getBoard(), workerPosition, newPosition);
        game.getTurn().updateTurnInfoAfterMove(workerPosition, newPosition, game.getBoard());
        SerializableUpdate update = new SerializableUpdateMove(newPosition, playerId, workerId);
        game.notifyJustUpdateAll(update);

        if (game.getGodPowers().get(playerId-1).win(workerPosition, newPosition, game.getBoard())) onPlayerWin(playerId);
        else nextOperation();
    }

    @Override
    // Consolida la build, aggiorna Turn e invia il relativo oggetto update
    public void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome) {
        game.getGodPowers().get(playerId-1).buildUp(newPosition, game.getBoard(), forceDome);
        game.getTurn().updateTurnInfoAfterBuild(newPosition);
        SerializableUpdate update = new SerializableUpdateBuild(newPosition, game.getBoard().getCell(newPosition).isDome());
        game.notifyJustUpdateAll(update);
        nextOperation();
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

    @Override
    // Notifica i player della disconnessione
    public void onPlayerDisconnection(int playerId) {
        SerializableUpdate update = new SerializableUpdateDisconnection(playerId);
        game.notifyJustUpdateAll(update);
        serverView.stopProcess();
        // Game ends due to disconnection
    }

    // termina il turno del perdente settando il Turn successivo, rimuove i worker dalle celle,
    // setta Player e GodPower a null, invia gli update relativi a perdita e turno
    public void onPlayerLoss(int playerId)  {
        int nextPlayerId = nextPlayerId(playerId);
        Position worker1Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 1).getPosition();
        Position worker2Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 2).getPosition();

        Turn newTurn = game.getGodPowers().get(playerId-1).endTurn(game.getTurn(), game.getGodPowers(), game.getPlayers().get(nextPlayerId-1));
        game.setTurn(newTurn);
        game.getPlayers().set(playerId-1, null);
        game.getGodPowers().set(playerId-1, null);
        game.getBoard().getCell(worker1Position).setWorker(null);
        game.getBoard().getCell(worker2Position).setWorker(null);
        List<SerializableUpdate> tempUpdates = new ArrayList<>();
        tempUpdates.add( new SerializableUpdateLoser(playerId));
        tempUpdates.add(new SerializableUpdateTurn(nextPlayerId));
        game.notifyJustUpdateAll(tempUpdates);
        nextOperation();
    }

    // Notifica i player della vittoria
    public void onPlayerWin (int playerId)  {
        game.notifyJustUpdateAll(new SerializableUpdateWinner(playerId));
        serverView.stopProcess();
        // Game ends due to win
    }

    // Controllo di vittoria/sconfitta generico che non considera mosse da consolidare
    // Controlla: se è rimasto un solo giocatore, condizioni di vittoria di tutti i players, condizioni di sconfitta del player corrente
    // Restituisce true in caso di vittoria
    private boolean checkWinOrLose (int playerId, Set<Position> worker1Moves, Set<Position> worker2Moves, Set<Position> worker1Builds, Set<Position> worker2Builds)  {
        if (game.getPlayers().stream().filter(x->x!=null).count()==1) { // controlla se è rimasto solo il giocatore corrente
            onPlayerWin(playerId);
            return true;
        }
        Set<Position> allMoves = new HashSet<>(worker1Moves);
        allMoves.addAll(worker2Moves);
        Set<Position> allBuilds = new HashSet<>(worker1Builds);
        allMoves.addAll(worker2Builds);
        StandardLoseCondition loseCondition = game.getGodPowers().get(playerId-1).getLoseCondition();
        for (GodPower p: game.getGodPowers()){
            List <StandardWinCondition> positiveWinCondition= p.getPositiveWinConditions();
            List <StandardWinCondition> blockingWinCondition= p.getBlockingWinConditions();
            if (blockingWinCondition.stream().allMatch(x -> x.win(null, null, game.getBoard())) &&
                    positiveWinCondition.stream().anyMatch(x -> x.win(null, null, game.getBoard()))) {
                onPlayerWin(p.getPlayerId());
                return true;
            }
        }
        // if (loseCondition.lose(allMoves, allBuilds)) onPlayerLoss(playerId); // todo:tolto momentaneamente poiché risponde true a caso
        return false;
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
