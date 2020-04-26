package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.server.serializable.SerializableAnswer;
import it.polimi.ingsw.server.serializable.SerializableUpdate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerProxy implements GameObserver{
    private ServerThread serverThread;
    private List<ProxyObserver> observerList;
    public void addObserver(ProxyObserver observer){
        observerList.add(observer);
    }

    // risponde a un singolo player
    public void answerOnePlayer(SerializableAnswer answer) throws IOException {
        try {
            Object fromClient = serverThread.sendObjectAndWaitForReply(answer, answer.getPlayerId(), 300);
            // qui il client ha risposto
            // ora devo gestire la risposta
        } catch (ClientStoppedWorkingException e){
            // il client non risponde o si è disconnesso
        }
    }

    // aggiorna tutti i player
    public void updateAll(SerializableUpdate update) throws IOException {
        serverThread.sendAllObject(update);
    }

    // aggiorna tutti i player e risponde a un singolo player
    public void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableAnswer answer) throws IOException {
        updateAll(update);
        answerOnePlayer(answer);
    }

    public ServerProxy(ServerThread serverThread) {
        this.serverThread = serverThread;
        observerList = new ArrayList();
    }
}
