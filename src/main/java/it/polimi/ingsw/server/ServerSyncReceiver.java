package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;

import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;


public class ServerSyncReceiver extends Thread {
    /**
     * This method waits for an object to be received by a client
     * @return Object
     * @param socket client socket
     * @throws ClientStoppedWorkingException if client is disconnected or no answer is received within one second
     */
    public Object receiveObject(Socket socket) throws ClientStoppedWorkingException {
        Object object = null;
        try {
            socket.setSoTimeout(1000);
            ObjectInputStream fileObjectIn = new ObjectInputStream(socket.getInputStream());
            object = fileObjectIn.readObject();
            socket.setSoTimeout(0);
        } catch (Exception e) {}
        if (object!=null) return object;
        throw new ClientStoppedWorkingException();
    }
}
