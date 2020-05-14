package it.polimi.ingsw.server;

import it.polimi.ingsw.server.serializable.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class ServerView extends Thread implements GameObserver{
    private ServerThread serverThread;
    private List<ProxyObserver> observerList;
    private List<EventGenerator> eventGenerators;
    private boolean processStopped = false;

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

    public ServerView(ServerThread serverThread, List <Socket> playersList) {
        this.serverThread = serverThread;
        this.observerList = new ArrayList<>();
        this.eventGenerators = new ArrayList<>();
        for (Socket s: playersList) startNewEventGenerator(new ServerAsyncReceiver(s));
    }

    public void startNewEventGenerator(EventGenerator eventGenerator){
        eventGenerators.add(eventGenerator);
        eventGenerators.get(eventGenerators.size()-1).start();
    }

    public void stopLastEventGenerator(){
        try{ eventGenerators.get(eventGenerators.size()-1).interrupt(); }catch(Exception e){}
        eventGenerators.remove(eventGenerators.size()-1);
    }

    public void stopAllEventGenerators(){
        while (eventGenerators.size()>0) stopLastEventGenerator();
    }

    public void stopProcess(){
        stopAllEventGenerators();
        processStopped = true;
    }

    // Ogni 0.1 secondi controlla se è successo qualcosa (se ha ricevuto oggetti da un qualunque client)
    // Ogni oggetto-evento captato genera una notifica al Controller
    public void run() {
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onInitialization();
            Object fromClient = null;
            int playerId;
            while (!processStopped) {
                try {
                    while (true) {
                        playerId = 1;
                        for (int i = 0; i < eventGenerators.size(); i++) {
                            fromClient = eventGenerators.get(i).getNewEvent();
                            if (fromClient != null) break;
                            playerId++;
                        }
                        if (fromClient != null) break;
                        try {
                           sleep(10);
                        } catch (InterruptedException e) {}
                    }
                    if (fromClient instanceof SerializableConsolidateMove) {
                        SerializableConsolidateMove serializableFromClient = (SerializableConsolidateMove) fromClient;
                        for (int i = 0; i < observerList.size(); i++)
                            observerList.get(i).onConsolidateMove(playerId, serializableFromClient.getWorkerId(), serializableFromClient.getNewPosition());
                    }
                    if (fromClient instanceof SerializableConsolidateBuild) {
                        SerializableConsolidateBuild serializableFromClient = (SerializableConsolidateBuild) fromClient;
                        for (int i = 0; i < observerList.size(); i++)
                           observerList.get(i).onConsolidateBuild(playerId, serializableFromClient.getNewPosition(), serializableFromClient.isForceDome());
                    }
                    if (fromClient instanceof SerializableInitializeGame) {
                        SerializableInitializeGame serializableFromClient = (SerializableInitializeGame) fromClient;
                        for (int i = 0; i < observerList.size(); i++)
                            observerList.get(i).onInitialization(playerId, serializableFromClient.getWorkerPositions(), serializableFromClient.getGodPower());
                    }
                    if (fromClient instanceof SerializableDeclineLastOptional) {
                        for (int i = 0; i < observerList.size(); i++) observerList.get(i).onEndedTurn(playerId);
                    }
                    if (fromClient instanceof Message && ((Message) fromClient).getMessage().equals("Error")) {
                        for (int i = 0; i < observerList.size(); i++) observerList.get(i).onPlayerDisconnection(playerId);
                    }
                }catch (Exception e){stopProcess();}
            }
    }
}
