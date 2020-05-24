package it.polimi.ingsw.client;

public class ClientBoard {
    private ClientPlayer[] players;
    private ClientBuilding[][] cells;
    private int playerTurnId; // mostra il playerId del giocatore che sta giocando
    private int myPlayerId = 0;

    public void setMyPlayerId(int playerId){ myPlayerId = playerId;}

    public int getMyPlayerId(){ return myPlayerId;}

    public void setPlayerTurnId(int playerTurnId) { this.playerTurnId = playerTurnId; }

    public int getPlayerTurnId() { return playerTurnId; }

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

    public int numOfPlayers(){ return players.length; }

    public ClientBoard(int numOfPlayers) {
        this.players = new ClientPlayer[numOfPlayers];
        this.cells = new ClientBuilding[5][5]; // da 0 a 3, null se nessuna costruzione
    }
}
