package it.polimi.ingsw;

import it.polimi.ingsw.server.ProxyObserver;
import it.polimi.ingsw.server.serializable.*;
import java.io.IOException;
import java.util.Set;

public class GameController implements ProxyObserver {
    private Game game;
    public GameController(Game game) {this.game = game;}

    @Override
    public void onMove(int playerId, int workerId) throws IOException {
        Position workerPosition = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), workerId).getPosition();
        Set<Position> moves = game.getGodPowers().get(playerId-1).move(workerPosition, game.getBoard(), game.getTurn());
        SerializableAnswer answer = new SerializableAnswerMove(playerId, moves);
        game.notifyAnswerOnePlayer(answer);
    }

    @Override
    public void onBuild(int playerId, int workerId) throws IOException {
        Position workerPosition = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), workerId).getPosition();
        Set<Position> builds = game.getGodPowers().get(playerId-1).build(workerPosition, game.getBoard(), game.getTurn());
        boolean canForceDome = false; //todo: sistemare
        SerializableAnswer answer = new SerializableAnswerBuild(playerId, builds, canForceDome);
        game.notifyAnswerOnePlayer(answer);
    }

    @Override
    public void onConsolidateMove(int playerId, int workerId, Position newPosition) throws IOException {
        Position workerPosition = game.getBoard().getWorkerCell(game.getPlayers().get(playerId-1), workerId).getPosition();
        game.getGodPowers().get(playerId-1).moveInto(game.getBoard(), workerPosition, newPosition);
        SerializableUpdate update = new SerializableUpdateMove(newPosition, playerId, workerId);
        SerializableAnswer answer = null; //todo: sistemare
        game.notifyUpdateAllAndAnswerOnePlayer(update, answer);
    }

    @Override
    public void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome) throws IOException {
        game.getGodPowers().get(playerId-1).buildUp(newPosition, game.getBoard(), forceDome);
        SerializableUpdate update = new SerializableUpdateBuild(newPosition, game.getBoard().getCell(newPosition).isDome());
        SerializableAnswer answer = null; //todo: sistemare
        game.notifyUpdateAllAndAnswerOnePlayer(update, answer);
    }

    @Override
    public void onPlayerDisconnection(int playerId) throws IOException {
        SerializableUpdate update = new SerializableUpdateDisconnection(playerId);
        game.notifyUpdateAll(update);
        // Game ends due to disconnection
    }
}
