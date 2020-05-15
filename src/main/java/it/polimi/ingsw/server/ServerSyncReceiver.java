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
    private boolean error;
    private Object object;
    public Object receiveObject(Socket socket, int timeLimit) throws ClientStoppedWorkingException {
        Thread thread = new Thread(()-> {
            try {
                ObjectInputStream fileObjectIn = new ObjectInputStream(socket.getInputStream());
                object = (Object) fileObjectIn.readObject();
            } catch (Exception e) {error = true;}
        });

        object = null;
        error = false;
        thread.start();
        for (int i = 0; i < timeLimit * 100; i++) {
            try {
                sleep(10);
                if (object!=null) {
                    return object;
                }
                if (error) {
                    thread.interrupt();
                    throw new ClientStoppedWorkingException();
                }
            } catch (InterruptedException e) {break;}
        }
        thread.interrupt();
        throw new ClientStoppedWorkingException();
    }
}
