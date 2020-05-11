package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import it.polimi.ingsw.effects.winCondition.StandardLoseCondition;
import it.polimi.ingsw.effects.winCondition.StandardWinCondition;
import it.polimi.ingsw.server.ProxyObserver;
import it.polimi.ingsw.server.serializable.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Controller implements ProxyObserver {
    private Game game;
    private List <GodPower> godPowersLeft;
    public Controller(Game game) { this.game = game; }

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

    // tempUpdates è una coda di Updates che verranno inviati ai client non appena sarà possibile,
    // cioè non appena ci sarà una Request da inviare a un client singolo.
    // Questo accade perché, affinché il ciclo MVC non sia rotto, bisogna far sì che ServerProxy
    // non si limiti a inviare oggetti ma debba poi anche attenderne uno; quindi
    // non bastano gli Updates (che non richiedono risposta dai client), ma è necessaria
    // una Request (che costringe ServerProxy a rimanere in attesa di una risposta
    // per poi invocare un metodo del Controller, garantendo così l'irreversibilità del ciclo).

    // decide quale player deve eseguire quale operazione, appoggiandosi alle informazioni di Turn e GodPower
    public void nextOperation(int playerId, List <SerializableUpdate> tempUpdates) throws IOException {
        // Questo metodo per ora gestisce solo un turno classico (move + build)
        int nextPlayerId = game.nextPlayerId(playerId);
        boolean canForceDome = game.getGodPowers().get(playerId-1).isAskToBuildDomes();
        Position worker1Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 1).getPosition();
        Position worker2Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 2).getPosition();
        Set<Position> worker1Moves = game.getGodPowers().get(playerId-1).move(worker1Position, game.getBoard(), game.getTurn());
        Set<Position> worker2Moves = game.getGodPowers().get(playerId-1).move(worker2Position, game.getBoard(), game.getTurn());
        Set<Position> worker1Builds = game.getGodPowers().get(playerId-1).build(worker1Position, game.getBoard(), game.getTurn());
        Set<Position> worker2Builds = game.getGodPowers().get(playerId-1).build(worker2Position, game.getBoard(), game.getTurn());

        checkWinOrLose(playerId, worker1Moves, worker2Moves, worker1Builds, worker1Builds, tempUpdates);

        if (!game.getTurn().isMoveBeforeBuild()){ // deve ancora muovere e poi costruire
            SerializableRequest request = new SerializableRequestMove(playerId, worker1Moves, worker2Moves);
            game.notifyUpdateAllAndAnswerOnePlayer(tempUpdates, request);
        } else if (game.getTurn().isMoveBeforeBuild() && !game.getTurn().isBuildAfterMove()){ // deve ancora costruire
            SerializableRequest request = new SerializableRequestBuild(playerId, worker1Builds, worker2Builds, canForceDome);
            game.notifyUpdateAllAndAnswerOnePlayer(tempUpdates, request);
        } else if (game.getTurn().isBuildAfterMove()){ // turno terminato
            Turn newTurn = game.getGodPowers().get(playerId-1).endTurn(game.getTurn(), game.getGodPowers(), game.getPlayers().get(nextPlayerId-1));
            game.setTurn(newTurn);
            tempUpdates.add(new SerializableUpdateTurn(nextPlayerId));
            nextOperation(nextPlayerId, tempUpdates);
        }
    }

    public void nextOperation(int playerId, SerializableUpdate tempUpdate) throws IOException {
        List <SerializableUpdate> tempUpdates = new ArrayList<>();
        tempUpdates.add(tempUpdate);
        nextOperation(playerId, tempUpdates);
    }

    @Override
    // consolida la move, genera il relativo oggetto update e verifica se il player nell'effettuare tale mossa ha vinto
    public void onConsolidateMove(int playerId, int workerId, Position newPosition) throws IOException {
        Position workerPosition = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), workerId).getPosition();
        game.getGodPowers().get(playerId-1).moveInto(game.getBoard(), workerPosition, newPosition);
        game.getTurn().updateTurnInfoAfterMove(workerPosition, newPosition, game.getBoard());
        SerializableUpdate update = new SerializableUpdateMove(newPosition, playerId, workerId);

        if (game.getGodPowers().get(playerId-1).win(workerPosition, newPosition, game.getBoard())) onPlayerWin(playerId, update);
        else nextOperation(playerId, update);
    }

    @Override
    // consolida la build e genera il relativo oggetto update
    public void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome) throws IOException {
        game.getGodPowers().get(playerId-1).buildUp(newPosition, game.getBoard(), forceDome);
        game.getTurn().updateTurnInfoAfterBuild(newPosition);
        SerializableUpdate update = new SerializableUpdateBuild(newPosition, game.getBoard().getCell(newPosition).isDome());
        nextOperation(playerId, update);
    }

    // primo metodo lanciato del controller, avvia MVC e procedura di InitializeGame
    public void onInitialization() throws IOException, ParseException {
        godPowersLeft = GodPowerManager.createGodPowers(game.getNumOfPlayers());
        List<String> godPowersNames = getGodPowersLeftNames();
        List<String> playersNames = new ArrayList<>();
        for (Player player: game.getPlayers()) playersNames.add(player.getName());
        SerializableUpdateInitializeNames update = new SerializableUpdateInitializeNames(playersNames);
        SerializableRequest request = new SerializableRequestInitializeGame(1, godPowersNames);
        game.notifyUpdateAllAndAnswerOnePlayer(update, request);
    }

    @Override
    // prosegue nella procedura di InitializeGame avanzando di un player
    public void onInitialization(int playerId, List<Position> workerPositions, String godPower) throws IOException {
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
            nextOperation(1, tempUpdates);
        } else {
            SerializableRequest request = new SerializableRequestInitializeGame(playerId + 1, getGodPowersLeftNames());
            game.notifyUpdateAllAndAnswerOnePlayer(update, request);
        }
    }

    @Override
    // notifica i player della disconnessione e rompe MVC
    public void onPlayerDisconnection(int playerId) throws IOException {
        SerializableUpdate update = new SerializableUpdateDisconnection(playerId);
        game.notifyJustUpdateAll(update);
        // Game ends due to disconnection
    }

    // termina il turno del perdente settando il Turn successivo, rimuove i worker dalle celle,
    // setta Player e GodPower a null, chiama nextOperation mettendo in coda gli update relativi a perdita e turno
    public void onPlayerLoss(int playerId, List<SerializableUpdate> tempUpdates) throws IOException {
        int nextPlayerId = game.nextPlayerId(playerId);
        Position worker1Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 1).getPosition();
        Position worker2Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 2).getPosition();

        Turn newTurn = game.getGodPowers().get(playerId-1).endTurn(game.getTurn(), game.getGodPowers(), game.getPlayers().get(nextPlayerId-1));
        game.setTurn(newTurn);
        game.getPlayers().set(playerId-1, null);
        game.getGodPowers().set(playerId-1, null);
        game.getBoard().getCell(worker1Position).setWorker(null);
        game.getBoard().getCell(worker2Position).setWorker(null);
        tempUpdates.add( new SerializableUpdateLoser(playerId));
        tempUpdates.add(new SerializableUpdateTurn(nextPlayerId));
        nextOperation(nextPlayerId, tempUpdates);
    }

    @Override
    public void onPlayerLoss(int playerId) throws IOException {
        onPlayerLoss(playerId, new ArrayList<>());
    }

    public void onPlayerLoss(int playerId, SerializableUpdate update) throws IOException {
        List <SerializableUpdate> tempUpdates = new ArrayList<>();
        tempUpdates.add(update);
        onPlayerLoss(playerId, tempUpdates);
    }

    // notifica i player della vittoria e rompe MVC
    public void onPlayerWin (int playerId, List<SerializableUpdate> tempUpdates) throws IOException {
        tempUpdates.add(new SerializableUpdateWinner(playerId));
        game.notifyJustUpdateAll(tempUpdates);
        // Game ends due to win
    }

    public void onPlayerWin(int playerId, SerializableUpdate update) throws IOException {
        List <SerializableUpdate> tempUpdates = new ArrayList<>();
        tempUpdates.add(update);
        onPlayerWin(playerId, tempUpdates);
    }


    // controllo di vittoria/sconfitta generico che non considera mosse da consolidare
    // controlla: condizioni di vittoria di tutti, condizioni di sconfitta del player, giocatore unico rimasto
    private void checkWinOrLose (int playerId, Set<Position> worker1Moves, Set<Position> worker2Moves, Set<Position> worker1Builds, Set<Position> worker2Builds, List<SerializableUpdate> tempUpdates) throws IOException {
        if (game.getPlayers().stream().filter(x->x!=null).count()==1) { // è rimasto solo il giocatore corrente
            onPlayerWin(playerId, tempUpdates);
            return;
        }
        Set<Position> allMoves = new HashSet<>(worker1Moves);
        allMoves.addAll(worker2Moves);
        Set<Position> allBuilds = new HashSet<>(worker1Builds);
        allMoves.addAll(worker2Builds);
        StandardLoseCondition loseCondition = game.getGodPowers().get(playerId-1).getLoseCondition();
        for (GodPower p: game.getGodPowers()){
            List <StandardWinCondition> positiveWinCondition= p.getPositiveWinConditions();
            List <StandardWinCondition> blockingWinCondition= p.getBlockingWinConditions();
            if (blockingWinCondition.stream()
                    .filter(x->!x.win(null, null, game.getBoard()))
                    .count() == 0 &&
                    positiveWinCondition.stream()
                            .filter(x->x.win(null, null, game.getBoard()))
                            .count() > 0) {
                onPlayerWin(p.getPlayerId(), tempUpdates);
                return;
            }
        }
        //if (loseCondition.lose(allMoves, allBuilds)) onPlayerLoss(playerId, tempUpdates);
        // todo:tolto momentaneamente poiché risponde true a caso
    }
}
