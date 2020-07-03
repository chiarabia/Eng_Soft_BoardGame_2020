package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;

public class Worker {
    private final Player player;
    private final int id;

    public Player getPlayer() {
        return player;
    }

    public int getWorkerId() {
        return id;
    }

    public int getPlayerId() {
        return player.getId();
    }

    public Worker(Player player, int id) {
        this.player = player;
        this.id = id;
    }
}