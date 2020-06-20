package it.polimi.ingsw.server;

/**
 * This abstract class represents Thread objects that
 * wait for events and report them to Observers
 */

public abstract class EventGenerator extends Thread {
    public abstract void addObserver(ProxyObserver observer);
    public abstract void stopProcess();
}
