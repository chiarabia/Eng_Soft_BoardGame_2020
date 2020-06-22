package it.polimi.ingsw.client;

/**
 * This class represents a summary representation of the game board
 * It stores relevant players and buildings information
 */

public class ClientBoard {
    private ClientPlayer[] players;
    private ClientBuilding[][] cells; /* cell[x][y]==null <==> no building is located in that cell */
    private int playerTurnId;
    private int myPlayerId;

    public int getMyPlayerId(){ return myPlayerId;}
    public int getPlayerTurnId() { return playerTurnId; }
    public ClientBuilding getCell (int x, int y){ return cells[x][y]; }
    public ClientPlayer getPlayer(int playerId){ return players[playerId-1]; }
    public int getNumOfPlayers(){ return players.length; }

    public void setPlayerTurnId(int playerTurnId) { this.playerTurnId = playerTurnId; }
    public void setPlayer(ClientPlayer player, int playerId){ players[playerId-1] = player; }
    public void setCell (ClientBuilding cell, int x, int y){ cells[x][y] = cell; }
    public void setMyPlayerId(int playerId){ myPlayerId = playerId;}

    public ClientBoard(int numOfPlayers) {
        this.myPlayerId = 0;
        this.players = new ClientPlayer[numOfPlayers];
        this.cells = new ClientBuilding[5][5];
    }
}
