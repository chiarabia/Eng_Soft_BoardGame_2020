package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;
import java.util.List;

public class SerializableUpdateInitializeGame implements SerializableUpdate {
    private final List<Position> workerPositions;
    private final String godPower;
    private final int playerId;

    public List<Position> getWorkerPositions() {
        return workerPositions;
    }

    public String getGodPower() { return godPower; }

    public int getPlayerId() {
        return playerId;
    }

    public SerializableUpdateInitializeGame(List<Position> workerPositions, String godPower, int playerId) {
        this.workerPositions = workerPositions;
        this.playerId = playerId;
        this.godPower = godPower;
    }
}
