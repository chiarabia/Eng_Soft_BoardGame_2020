package it.polimi.ingsw.controller.server.serializable;

import java.util.List;

public class SerializableRequestInitializeGodPower implements SerializableRequest {
    private final int playerId;
    private final List<String> godPowers;

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public List<String> getGodPowers() { return godPowers; }

    public SerializableRequestInitializeGodPower(int playerId, List<String> godPowers) {
        this.godPowers = godPowers;
        this.playerId = playerId;
    }
}
