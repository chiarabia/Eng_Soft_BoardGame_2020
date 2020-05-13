package it.polimi.ingsw.server;

import it.polimi.ingsw.server.serializable.Message;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// Thread parallelo che si mette in ascolto di un socket
// Un oggetto ricevuto viene messo in coda alla lista di oggetti-evento
// In caso di errore (problemi di rete/client disconnesso) aggiunge alla lista un oggetto Message("Error")

public class ServerAsyncReceiver extends EventGenerator {
    private List<Object> objects;
    private Socket socket;
    private synchronized void addToObjectsList(Object recievedObject){ objects.add(recievedObject); }
    public synchronized Object getNewEvent(){
        if (objects.size()==0) return null;
        return objects.remove(0);
    }
    public void run() {
            try {
                while (true) {
                    ObjectInputStream fileObjectIn = new ObjectInputStream(socket.getInputStream());
                    addToObjectsList(fileObjectIn.readObject());
                }
            } catch (Exception e) { addToObjectsList( new Message("Error"));}
    }
    public ServerAsyncReceiver(Socket socket){
        this.socket = socket;
        objects = new ArrayList<>();
    }
}
