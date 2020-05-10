package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

import java.util.List;

public class SerializableInitializeGame implements Serializable {
    private final List<Position> workerPositions;
    private final String godPower;

    public List<Position> getWorkerPositions() {
        return workerPositions;
    }

    public String getGodPower() { return godPower; }

    public SerializableInitializeGame(List<Position> workerPositions, String godPower) {
        this.godPower = godPower;
        this.workerPositions = workerPositions;
    }
}
