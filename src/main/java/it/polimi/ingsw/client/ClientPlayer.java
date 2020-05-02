package it.polimi.ingsw.client;

public class ClientPlayer {
    private ClientWorker[] workers;
    private final String playerName;
    private final String godPowerName;
    private boolean lost = false;

    public ClientWorker getWorker(int workerId) {
        return workers[workerId-1];
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public boolean hasLost() {
        return lost;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getGodPowerName() {
        return godPowerName;
    }

    public void setWorker(ClientWorker worker, int workerId) {
        workers [workerId-1] = worker;
    }

    public ClientPlayer(String playerName, String godPowerName) {
        this.workers = new ClientWorker[2];
        workers[0] = new ClientWorker();
        workers[1] = new ClientWorker();
        this.playerName = playerName;
        this.godPowerName = godPowerName;
    }
}
