package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;

import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerReciever extends Thread {
    private static boolean clientError;
    private static boolean IOError;
    private static Object object;
    public static Object receiveObject(Socket socket, int timeLimit) throws ClientStoppedWorkingException, IOException {
        Thread thread = new Thread(()-> {
            try {
                ObjectInputStream fileObjectIn = new ObjectInputStream(socket.getInputStream());
                object = (Object) fileObjectIn.readObject();
            }  catch (IOException e){IOError = true;}
            catch (Exception e) {clientError = true;}
        });

        object = null;
        clientError = false;
        IOError = false;
        thread.start();
        for (int i = 0; i < timeLimit * 100; i++) {
            try {
                sleep(10);
                if (object!=null) {
                    return object;
                }
                if (clientError) {
                    thread.interrupt();
                    throw new ClientStoppedWorkingException(false);
                }
                if (IOError) {
                    thread.interrupt();
                    throw new IOException();
                }
            } catch (InterruptedException e) {break;}
        }
        thread.interrupt();
        throw new ClientStoppedWorkingException(true);
    }
}
