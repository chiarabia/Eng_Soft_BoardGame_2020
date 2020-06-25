package it.polimi.ingsw.server;

import it.polimi.ingsw.server.serializable.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class ServerView implements GameObserver{
    private ServerThread serverThread;
    private List<ProxyObserver> observerList;
    private List<EventGenerator> eventGenerators;

    public void addObserver(ProxyObserver observer){
        observerList.add(observer);
    }

    public void justUpdateAll(SerializableUpdate update) {
        serverThread.sendAllObject(update);
    }

    public void justUpdateAll(List<SerializableUpdate> updates) {
        for (SerializableUpdate update : updates) serverThread.sendAllObject(update);
    }

    public void answerOnePlayer(SerializableRequest request){
        serverThread.sendObject(request, request.getPlayerId() - 1);
    }

    public void updateAllAndAnswerOnePlayer(List<SerializableUpdate> updates, SerializableRequest request){
        for (SerializableUpdate update : updates) serverThread.sendAllObject(update);
        answerOnePlayer(request);
    }

    public void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableRequest request) {
        List <SerializableUpdate> updates = new ArrayList<>();
        updates.add(update);
        updateAllAndAnswerOnePlayer(updates, request);
    }

    public ServerView(ServerThread serverThread) {
        this.serverThread = serverThread;
        this.observerList = new ArrayList<>();
        this.eventGenerators = new ArrayList<>();
    }

    /**
     * This method adds to list and starts a new EventGenerator
     * @param eventGenerator EventGenerator
     */
    public void startNewEventGenerator(EventGenerator eventGenerator){
        eventGenerators.add(eventGenerator);
        for (int i = 0; i< observerList.size(); i++) eventGenerator.addObserver(observerList.get(i));
        eventGenerators.get(eventGenerators.size()-1).start();
    }

    /**
     * This method creates, adds and starts one ServerAsyncReceiver event generator each player,
     * then notifies all observers about initialization process
     * @param playersList players list
     */
    public void startNewEventGenerators(List <Socket> playersList){
        int playerId = 1;
        for (Socket s: playersList){
            startNewEventGenerator(new ServerAsyncReceiver(s, playerId));
            playerId++;
        }
        for (int i = 0; i< observerList.size(); i++)  observerList.get(i).onInitialization();
    }

    /**
     * This method stops last event generator and removes it from the list
     */
    public void stopLastEventGenerator(){
        eventGenerators.get(eventGenerators.size()-1).stopProcess();
        eventGenerators.remove(eventGenerators.size()-1);
    }

    /**
     * This method stops and removes all event generators
     */
    public void stopAllEventGenerators(){
        while (eventGenerators.size()>0) stopLastEventGenerator();
        System.out.println("Game ended");
    }
}
