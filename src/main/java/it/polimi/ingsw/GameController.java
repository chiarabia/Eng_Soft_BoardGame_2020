package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.server.ProxyObserver;
import it.polimi.ingsw.server.serializable.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameController implements ProxyObserver {
    private Game game;
    public GameController(Game game) {
        this.game = game;
    }

    public void nextOperation(int playerId, List <SerializableUpdate> tempUpdates) throws IOException {
        // I tempUpdates sono update che potranno essere inviati dopo aver deciso chi farà cosa
        // Questo metodo per ora gestisce solo le primitive di un turno classico (move + build)
        Position worker1Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 1).getPosition();
        Position worker2Position = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), 2).getPosition();
        if (!game.getTurn().isMoveBeforeBuild()){ // deve ancora muovere e poi costruire
            Set<Position> worker1Moves = game.getGodPowers().get(playerId-1).move(worker1Position, game.getBoard(), game.getTurn());
            Set<Position> worker2Moves = game.getGodPowers().get(playerId-1).move(worker2Position, game.getBoard(), game.getTurn());
            SerializableRequest request = new SerializableRequestMove(playerId, worker1Moves, worker2Moves);
            game.notifyUpdateAllAndAnswerOnePlayer(tempUpdates, request);
        } else if (game.getTurn().isMoveBeforeBuild() && !game.getTurn().isBuildAfterMove()){ // deve ancora costruire
            Set<Position> worker1Builds = game.getGodPowers().get(playerId-1).build(worker1Position, game.getBoard(), game.getTurn());
            Set<Position> worker2Builds = game.getGodPowers().get(playerId-1).build(worker2Position, game.getBoard(), game.getTurn());
            boolean canForceDome = game.getGodPowers().get(playerId-1).isAskToBuildDomes();
            SerializableRequest request = new SerializableRequestBuild(playerId, worker1Builds, worker2Builds, canForceDome);
            game.notifyUpdateAllAndAnswerOnePlayer(tempUpdates, request);
        } else if (game.getTurn().isBuildAfterMove()){ // turno terminato
            int nextPlayerId = game.nextPlayerId(playerId);
            Turn newTurn = game.getGodPowers().get(playerId-1).endTurn(game.getTurn(), game.getGodPowers(), game.getPlayers().get(nextPlayerId-1));
            game.setTurn(newTurn);
            tempUpdates.add(new SerializableUpdateTurn(nextPlayerId));
            nextOperation(nextPlayerId, tempUpdates);
        }
    }

    public void nextOperation(int playerId) throws IOException {
        nextOperation(playerId, new ArrayList<>());
    }

    public void nextOperation(int playerId, SerializableUpdate tempUpdate) throws IOException {
        List <SerializableUpdate> tempUpdates = new ArrayList<>();
        tempUpdates.add(tempUpdate);
        nextOperation(playerId, tempUpdates);
    }

    @Override
    public void onOptionalMove(int playerId, boolean wantToMove) throws IOException {
        // prende atto del fatto che il giocatore ha accettato o meno di muovere
    }

    @Override
    public void onOptionalBuild(int playerId, boolean wantToBuild) throws IOException {
        // prende atto del fatto che il giocatore ha accettato o meno di costruire
    }

    @Override
    public void onConsolidateMove(int playerId, int workerId, Position newPosition) throws IOException {
        Position workerPosition = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), workerId).getPosition();
        game.getGodPowers().get(playerId-1).moveInto(game.getBoard(), workerPosition, newPosition);
        game.getTurn().updateTurnInfoAfterMove(workerPosition, newPosition, game.getBoard());
        SerializableUpdate update = new SerializableUpdateMove(newPosition, playerId, workerId);
        nextOperation(playerId, update);
    }

    @Override
    public void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome) throws IOException {
        game.getGodPowers().get(playerId-1).buildUp(newPosition, game.getBoard(), forceDome);
        game.getTurn().updateTurnInfoAfterBuild(newPosition);
        SerializableUpdate update = new SerializableUpdateBuild(newPosition, game.getBoard().getCell(newPosition).isDome());
        nextOperation(playerId, update);
    }

    @Override
    public void onInitialization() throws IOException {
        List<String> playersNames = new ArrayList<>();
        List<String> godPowersNames = new ArrayList<>();
        for (Player player: game.getPlayers()) playersNames.add(player.getName());
        for (GodPower godPower: game.getGodPowers()) godPowersNames.add(godPower.getGodName());
        SerializableUpdateInitializeInfos update = new SerializableUpdateInitializeInfos(playersNames, godPowersNames);
        SerializableRequest request = new SerializableRequestInitializeWorkers(1);
        game.notifyUpdateAllAndAnswerOnePlayer(update, request);
    }

    @Override
    public void onInitialization(int playerId, List<Position> workerPositions) throws IOException {
        Worker worker1 = new Worker(game.getPlayers().get(playerId-1), 1);
        Worker worker2 = new Worker(game.getPlayers().get(playerId-1), 2);
        game.getPlayers().get(playerId-1).addWorker(worker1);
        game.getPlayers().get(playerId-1).addWorker(worker2);
        Cell worker1Cell = game.getBoard().getCell(workerPositions.get(0));
        Cell worker2Cell = game.getBoard().getCell(workerPositions.get(1));
        worker1Cell.setWorker(worker1);
        worker2Cell.setWorker(worker2);
        SerializableUpdateInitializeWorkers update = new SerializableUpdateInitializeWorkers(workerPositions, playerId);
        if (playerId == game.getPlayers().size()){ // tutti i worker sono pronti, il primo turno ha inizio
            SerializableUpdateTurn updateTurn = new SerializableUpdateTurn(1);
            game.setTurn(new Turn(game.getPlayers().get(0)));
            List <SerializableUpdate> tempUpdates = new ArrayList<>();
            tempUpdates.add(update);
            tempUpdates.add(updateTurn);
            nextOperation(1, tempUpdates);
        } else {
            SerializableRequest request = new SerializableRequestInitializeWorkers(playerId + 1);
            game.notifyUpdateAllAndAnswerOnePlayer(update, request);
        }
    }

    @Override
    public void onPlayerDisconnection(int playerId) throws IOException {
        SerializableUpdate update = new SerializableUpdateDisconnection(playerId);
        game.notifyJustUpdateAll(update);
        // Game ends due to disconnection
    }
}
