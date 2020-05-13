package it.polimi.ingsw.server;

import it.polimi.ingsw.Position;
import java.io.IOException;
import java.util.List;

public interface ProxyObserver {
    void onEndedTurn (int playerId);
    void onConsolidateMove(int playerId, int workerId, Position newPosition);
    void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome);
    void onInitialization();
    void onInitialization(int playerId, List<Position> workerPositions, String godPower);
    void onPlayerDisconnection(int playerId);
}
