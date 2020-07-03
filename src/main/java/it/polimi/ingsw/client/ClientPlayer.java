package it.polimi.ingsw.client;

public class ClientPlayer {
    private ClientWorker[] workers;
    private final String playerName;
    private String godPowerName;
    private boolean lost;

    public void setLost(boolean lost) {
        this.lost = lost;
    }
    public void setWorker(ClientWorker worker, int workerId) {
        workers [workerId-1] = worker;
    }
    public void setGodPowerName(String godPowerName) { this.godPowerName = godPowerName; }

    public boolean hasLost() {
        return lost;
    }
    public String getPlayerName() {
        return playerName;
    }
    public ClientWorker getWorker(int workerId) {
        return workers[workerId-1];
    }
    public String getGodPowerName() {
        return godPowerName;
    }

    public ClientPlayer(String playerName) {
        this.lost = false;
        this.playerName = playerName;
        this.workers = new ClientWorker[2];
        workers[0] = new ClientWorker();
        workers[1] = new ClientWorker();
    }
}
