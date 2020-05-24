package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;

import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

// ClientStoppedWorkingException
// non distingue fra problemi di rete e client che si scollega poiché nella nostra
// architettura non fa differenza, in ogni caso il Server disconnette e termina.

public class ServerSyncReceiver extends Thread {
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
