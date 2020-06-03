package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

import java.util.List;

public class SerializableInitializeGodPower implements Serializable {
    private final String godPower;

    public String getGodPower() { return godPower; }

    public SerializableInitializeGodPower(String godPower) {
        this.godPower = godPower;
    }
}
