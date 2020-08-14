package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.model.Position;

import java.util.List;

/**
 * This interface implements the proxy-view observers (like Controller)
 */

public interface ProxyObserver {
    void onEndedTurn (int playerId);
    void onConsolidateMove(int playerId, int workerId, Position newPosition);
    void onConsolidateBuild(int playerId, int workerId, Position newPosition, boolean forceDome);
    void onInitialization();
    void onGodPowerInitialization(int playerId, String godPower);
    void onWorkerPositionsInitialization(int playerId, List<Position> workerPositions);
    void onPlayerDisconnection(int playerId);
}
