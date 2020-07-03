package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.server.serializable.*;


import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerAsyncReceiver extends EventGenerator {
    private final int playerId;
    private Socket socket;
    private List<ProxyObserver> observerList;
    public void addObserver(ProxyObserver observer){
        observerList.add(observer);
    }

    /**
     * Closes the connection to the client, generates an error for the Controller
     * and closes the application
     */
    public void stopProcess(){
        try { socket.close(); } catch(Exception e){}
    }

    /**
     * Waits for the received objects from the client or for connection errors
     * and reports them to the Controller.
     * After an error the process stops, otherwise it continues to listen to the client
     */
    public void run() {
            try {
                while (true) {
                    ObjectInputStream fileObjectIn = new ObjectInputStream(socket.getInputStream());
                    reactToClient(fileObjectIn.readObject());
                }
            } catch (Exception e) { reactToClient( new Message("Error"));}
    }

    /**
     * Notifies the Controller about an error or a received object from the client
     * @param fromClient object from client
     */
    public void reactToClient(Object fromClient) {
        if (fromClient instanceof SerializableConsolidateMove) {
            SerializableConsolidateMove serializableFromClient = (SerializableConsolidateMove) fromClient;
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onConsolidateMove(playerId, serializableFromClient.getWorkerId(), serializableFromClient.getNewPosition());
        }
        if (fromClient instanceof SerializableConsolidateBuild) {
            SerializableConsolidateBuild serializableFromClient = (SerializableConsolidateBuild) fromClient;
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onConsolidateBuild(playerId, serializableFromClient.getWorkerId(), serializableFromClient.getNewPosition(), serializableFromClient.isForceDome());
        }
        if (fromClient instanceof SerializableInitializeGodPower) {
            SerializableInitializeGodPower serializableFromClient = (SerializableInitializeGodPower) fromClient;
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onGodPowerInitialization(playerId, serializableFromClient.getGodPower());
        }
        if (fromClient instanceof SerializableInitializeWorkerPositions) {
            SerializableInitializeWorkerPositions serializableFromClient = (SerializableInitializeWorkerPositions) fromClient;
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onWorkerPositionsInitialization(playerId, serializableFromClient.getWorkerPositions());
        }
        if (fromClient instanceof SerializableDeclineLastAction) {
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onEndedTurn(playerId);
        }
        if (fromClient instanceof Message && ((Message) fromClient).getMessage().equals("Error")) {
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onPlayerDisconnection(playerId);
        }
    }

    public ServerAsyncReceiver(Socket socket, int playerId){
        this.socket = socket;
        this.playerId = playerId;
        this.observerList = new ArrayList<>();
    }
}
