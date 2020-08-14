package it.polimi.ingsw.controller.server.serializable;

public class SerializableInitializeGodPower implements Serializable {
    private final String godPower;

    public String getGodPower() { return godPower; }

    public SerializableInitializeGodPower(String godPower) {
        this.godPower = godPower;
    }
}
