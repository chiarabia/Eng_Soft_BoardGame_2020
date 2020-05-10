package it.polimi.ingsw.server.serializable;

import java.util.List;

public class SerializableUpdateInitializeNames implements SerializableUpdate {
    private final List<String> playersNames;

    public List<String> getPlayersNames() {
        return playersNames;
    }

    public SerializableUpdateInitializeNames(List<String> playersNames) {
        this.playersNames = playersNames;
    }
}
