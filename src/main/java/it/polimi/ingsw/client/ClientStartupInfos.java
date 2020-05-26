package it.polimi.ingsw.client;

public class ClientStartupInfos {
    private final String name;
    private final int numOfPlayers;

    public String getName() {
        return name;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public ClientStartupInfos(String name, int numOfPlayers) {
        this.name = name;
        this.numOfPlayers = numOfPlayers;
    }
}
