package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;

public class ServerProxy implements GameObserver{
    private ServerThread serverThread;
    private List<ProxyObserver> observerList;
    public void addObserver(ProxyObserver observer){
        observerList.add(observer);
    }
    public ServerProxy(ServerThread serverThread) {
        this.serverThread = serverThread;
        observerList = new ArrayList();
    }
}
