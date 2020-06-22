package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.server.serializable.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * This class manages the connection between client and server
 */

public class ClientCommunicator extends Thread {
    private final Socket serverSocket;
    private List<Client> observerList = new ArrayList<>();
    public void addObserver(Client client){observerList.add(client);}

    public ClientCommunicator(int port, String IP) throws IOException {
        serverSocket = new Socket(IP, port);
    }

    /**
     * This method waits for an object to be received from server
     * @return Object
     * @throws IOException IOException
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public Object waitForObject () throws IOException, ClassNotFoundException {
        ObjectInputStream fileObjectIn = new ObjectInputStream(serverSocket.getInputStream());
        return fileObjectIn.readObject();
    }

    /**
     * This method sends an object to server
     * @param object Object
     */
    public void sendObject (Object object) {
        try {
            ObjectOutputStream fileObjectOut = new ObjectOutputStream(serverSocket.getOutputStream());
            fileObjectOut.writeObject(object);
            fileObjectOut.flush();
        } catch (Exception e){}
    }

    /**
     * This method sends a message to server
     * @param message text
     */
    public void sendMessage (String message) {
        sendObject(new Message(message));
    }

    /** This method closes the connection with server */
    public void stopProcess(){
        try { serverSocket.close(); } catch (Exception e){}
    }

    /** This method waits for objects from server or errors and reacts to them */
    public void run(){
        try {
            while (true) { reactToServer(waitForObject()); }
        } catch (GameEndedException e) {stopProcess();
        } catch (Exception e) {
            for (Client client : observerList) client.onError();
            stopProcess();
            e.printStackTrace();
        }
    }

/**
 * This method notifies the client about received objects or errors
 * @param object Object
 * @throws Exception Exception
 */
    private void reactToServer(Object object) throws Exception {
        if (object instanceof SerializableUpdateInitializeWorkerPositions) {
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onUpdateInitializeWorkerPositions((SerializableUpdateInitializeWorkerPositions) object);
        }
        if (object instanceof SerializableUpdateInitializeGodPower) {
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onUpdateInitializeGodPower((SerializableUpdateInitializeGodPower) object);
        }
        if (object instanceof SerializableRequestInitializeGodPower) {
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onRequestInitializeGodPower((SerializableRequestInitializeGodPower) object);
        }
        if (object instanceof SerializableRequestInitializeWorkerPositions) {
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onRequestInitializeWorkerPositions();
        }
        if (object instanceof SerializableUpdateActions) {
            for (Client client : observerList) client.onUpdateAction((SerializableUpdateActions) object);
        }
        if (object instanceof SerializableRequestAction) {
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onRequestAction((SerializableRequestAction) object);
        }
        if (object instanceof SerializableUpdateTurn) {
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onUpdateTurn((SerializableUpdateTurn) object);
        }
        if (object instanceof SerializableUpdateInitializeNames){
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onUpdateInitializeNames((SerializableUpdateInitializeNames) object);
        }
        if (object instanceof SerializableUpdateLoser) {
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onUpdateLoser ((SerializableUpdateLoser) object);
        }
        if (object instanceof SerializableUpdateWinner) {
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onUpdateWinner((SerializableUpdateWinner) object);
        }
        if (object instanceof SerializableUpdateDisconnection) {
            for(int i = 0; i<observerList.size(); i++)observerList.get(i).onUpdateDisconnection((SerializableUpdateDisconnection) object);
        }
        if (object instanceof Message){
            if (((Message) object).getMessage().equals("HELLO")) {
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onHello();
            }
            else if (((Message) object).getMessage().equals("ERROR_NOT_VALID_NAME")) {
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onRestart(1);
            }
            else if (((Message) object).getMessage().equals("ERROR_NOT_VALID_NUM_OF_PLAYERS")) {
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onRestart(2);
            }
            else if (((Message) object).getMessage().substring(0, 7).equals("PLAYER_")) {
                for(int i = 0; i<observerList.size(); i++)observerList.get(i).onPlayerIdAssigned(((Message) object).getMessage());
            }
        }
    }
}
