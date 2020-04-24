package it.polimi.ingsw.server;

import it.polimi.ingsw.Position;

public interface ProxyObserver {
    public void onMove(int playerId, int workerId);
    public void onBuild(int playerId, int workerId);
    public void onConsolidateMove(int playerId, int workerId, Position newPosition);
    public void onConsolidateBuild(Position newPosition, boolean forceDome);
    public void onPlayerDisconnection(int playerId);
}
