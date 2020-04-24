package it.polimi.ingsw;

import it.polimi.ingsw.server.ProxyObserver;

public class GameController implements ProxyObserver {

    @Override
    public void onMove(int playerId, int workerId) {

    }

    @Override
    public void onBuild(int playerId, int workerId) {

    }

    @Override
    public void onConsolidateMove(int playerId, int workerId, Position newPosition) {

    }

    @Override
    public void onConsolidateBuild(Position newPosition, boolean forceDome) {

    }

    @Override
    public void onPlayerDisconnection(int playerId) {

    }
}
