package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import java.net.Socket;
import java.util.Scanner;

public class ServerReciever extends Thread {
    private static String message;
    private static boolean error;
    public static String recieve(Socket socket, int timeLimit) throws ClientStoppedWorkingException {
        Thread thread = new Thread(()-> {
                try {
                    Scanner in = new Scanner (socket.getInputStream());
                    message = in.nextLine();
                } catch (Exception e) {error = true;}
            });

        error = false;
        message = null;
        thread.start();
        for (int i = 0; i < timeLimit * 100; i++) {
            try {
                sleep(10);
                if (message!=null) return message;
                if (error) {
                    thread.interrupt();
                    throw new ClientStoppedWorkingException(false);
                }
            } catch (InterruptedException e) {break;}
        }
        thread.interrupt();
        throw new ClientStoppedWorkingException(true);
    }
}
