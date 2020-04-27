package it.polimi.ingsw.server.serializable;

import java.util.List;

public class SerializableUpdateInitializeInfos implements SerializableUpdate {
    private final List<String> playersNames;
    private final List<String> godPowersNames;

    public List<String> getPlayersNames() {
        return playersNames;
    }

    public List<String> getGodPowersNames() {
        return godPowersNames;
    }

    public SerializableUpdateInitializeInfos(List<String> playersNames, List<String> godPowersNames) {
        this.playersNames = playersNames;
        this.godPowersNames = godPowersNames;
    }
}
