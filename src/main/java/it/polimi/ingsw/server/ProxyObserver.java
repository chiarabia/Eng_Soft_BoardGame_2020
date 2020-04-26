package it.polimi.ingsw.server;

import it.polimi.ingsw.Position;
import java.io.IOException;

public interface ProxyObserver {
    void onMove(int playerId, int workerId) throws IOException;
    void onBuild(int playerId, int workerId) throws IOException;
    void onConsolidateMove(int playerId, int workerId, Position newPosition) throws IOException;
    void onConsolidateBuild(int playerId, Position newPosition, boolean forceDome) throws IOException;
    void onPlayerDisconnection(int playerId) throws IOException;
}
