package it.polimi.ingsw.client;

public class ClientBuilding {
    private final int level;
    private final boolean dome;

    public ClientBuilding(int level, boolean dome) {
        this.level = level;
        this.dome = dome;
    }

    public int getLevel() {
        return level;
    }

    public boolean isDome() {
        return dome;
    }
}
