package it.polimi.ingsw.server;

import it.polimi.ingsw.Position;
import java.io.IOException;
import java.util.List;

/**
 * This interface represents proxy-view observers (i.e. Controller)
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
