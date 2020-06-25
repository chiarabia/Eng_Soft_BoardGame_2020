package it.polimi.ingsw.client;

public class ClientWorker {
    /* (x==-1 && y==-1) <==> workers have not been set yet */
    private int x;
    private int y;

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public int getX() { return x; }
    public int getY() { return y; }

    public ClientWorker() {
        this.x = -1;
        this.y = -1;
    }
}
