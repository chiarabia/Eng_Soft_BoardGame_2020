package it.polimi.ingsw.server;

import it.polimi.ingsw.server.serializable.*;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// Thread parallelo che si mette in ascolto di un socket e notifica il controller

public class ServerAsyncReceiver extends EventGenerator {
    private final int playerId;
    private Socket socket;
    private List<ProxyObserver> observerList;
    public void addObserver(ProxyObserver observer){
        observerList.add(observer);
    }

    public void stopProcess(){
        try { socket.close(); } catch(Exception e){}
    }

    public void run() {
            try {
                while (true) {
                    ObjectInputStream fileObjectIn = new ObjectInputStream(socket.getInputStream());
                    reactToClient(fileObjectIn.readObject());
                }
            } catch (Exception e) {reactToClient( new Message("Error"));}
    }

    public ServerAsyncReceiver(Socket socket, int playerId){
        this.socket = socket;
        this.playerId = playerId;
        this.observerList = new ArrayList<>();
    }

    public void reactToClient(Object fromClient) {
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
        if (fromClient instanceof SerializableDeclineLastAction) {
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onEndedTurn(playerId);
        }
        if (fromClient instanceof Message && ((Message) fromClient).getMessage().equals("Error")) {
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onPlayerDisconnection(playerId);
        }
    }
}
