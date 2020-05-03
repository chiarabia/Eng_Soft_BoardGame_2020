package it.polimi.ingsw.client;

public class ClientBoard {
    private ClientPlayer[] players;
    private ClientBuilding[][] cells;

    public ClientPlayer getPlayer(int playerId){
        return players[playerId-1];
    }

    public ClientBuilding getCell (int x, int y){
        return cells[x][y];
    }

    public void setPlayer(ClientPlayer player, int playerId){
        players[playerId-1] = player;
    }

    public void setCell (ClientBuilding cell, int x, int y){
        cells[x][y] = cell;
    }

    public ClientBoard(int numOfPlayers) {
        this.players = new ClientPlayer[numOfPlayers];
        this.cells = new ClientBuilding[5][5]; // da 0 a 3, null se nessuna costruzione
    }
}
