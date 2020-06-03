package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

import java.util.List;

public class SerializableUpdateInitializeGodPower implements SerializableUpdate {
    private final String godPower;
    private final int playerId;

    public String getGodPower() { return godPower; }

    public int getPlayerId() {
        return playerId;
    }

    public SerializableUpdateInitializeGodPower(String godPower, int playerId) {
        this.playerId = playerId;
        this.godPower = godPower;
    }
}
