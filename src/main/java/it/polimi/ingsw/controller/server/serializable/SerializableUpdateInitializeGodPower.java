package it.polimi.ingsw.controller.server.serializable;

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
