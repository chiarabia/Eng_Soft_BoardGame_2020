package it.polimi.ingsw.server.serializable;

public class SerializableConnection implements Serializable {
    private final int numOfPlayers;
    private final String name;

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public String getName() {
        return name;
    }

    public SerializableConnection(int numOfPlayers, String name) {
        this.numOfPlayers = numOfPlayers;
        this.name = name;
    }
}
