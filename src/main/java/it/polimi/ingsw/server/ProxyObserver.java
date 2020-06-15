package it.polimi.ingsw.server;

import it.polimi.ingsw.Position;
import java.io.IOException;
import java.util.List;

public interface ProxyObserver {
    void onEndedTurn (int playerId);
    void onConsolidateMove(int playerId, int workerId, Position newPosition);
    void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome);
    void onGodPowerInitialization();
    void onGodPowerInitialization(int playerId, String godPower);
    void onWorkerPositionsInitialization();
    void onWorkerPositionsInitialization(int playerId, List<Position> workerPositions);
    void onPlayerDisconnection(int playerId);
}
