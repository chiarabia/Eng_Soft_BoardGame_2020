package it.polimi.ingsw.server;

import it.polimi.ingsw.Position;
import java.io.IOException;
import java.util.List;

public interface ProxyObserver {
    void onConsolidateMove(int playerId, int workerId, Position newPosition) throws IOException;
    void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome) throws IOException;
    void onInitialization(int playerId, List<Position> workerPositions, String godPower) throws IOException;
    void onPlayerLoss(int playerId) throws IOException;
    void onPlayerDisconnection(int playerId) throws IOException;
}
