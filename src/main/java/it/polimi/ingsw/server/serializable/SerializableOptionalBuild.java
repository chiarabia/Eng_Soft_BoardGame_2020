package it.polimi.ingsw.server.serializable;

public class SerializableOptionalBuild implements Serializable {
    private final boolean wantToBuild;

    public boolean isWantToBuild() {
        return wantToBuild;
    }

    public SerializableOptionalBuild(boolean wantToBuild) {
        this.wantToBuild = wantToBuild;
    }
}
