package it.polimi.ingsw.server;

public abstract class EventGenerator extends Thread {
    public abstract void addObserver(ProxyObserver observer);
    public abstract void stopProcess();
}
