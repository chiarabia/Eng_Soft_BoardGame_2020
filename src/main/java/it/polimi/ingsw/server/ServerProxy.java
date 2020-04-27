package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.server.serializable.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerProxy implements GameObserver{
    private ServerThread serverThread;
    private List<ProxyObserver> observerList;
    public void addObserver(ProxyObserver observer){
        observerList.add(observer);
    }

    // aggiorna tutti i player rompendo il ciclo MVC
    // updateAll deve servire SOLO per comunicare il termine della partita (vittoria o disconnessione player)
    public void updateAll(SerializableUpdate update) throws IOException {
        serverThread.sendAllObject(update);
    }

    // risponde a un singolo player
    public void answerOnePlayer(SerializableAnswer answer) throws IOException {
        int playerId = answer.getPlayerId();
        try {
            Object fromClient = serverThread.sendObjectAndWaitForReply(answer, answer.getPlayerId(), 300);
            // qui il client ha risposto, ora devo gestire la risposta
            if (fromClient instanceof SerializableMove) {
                SerializableMove serializableFromClient = (SerializableMove) fromClient;
                for (int i = 0; i < observerList.size(); i++)
                    observerList.get(i).onMove(playerId, serializableFromClient.getWorkerId());
            }
            if (fromClient instanceof SerializableBuild) {
                SerializableBuild serializableFromClient = (SerializableBuild) fromClient;
                for (int i = 0; i < observerList.size(); i++)
                    observerList.get(i).onBuild(playerId, serializableFromClient.getWorkerId());
            }
            if (fromClient instanceof SerializableOptionalMove) {
                SerializableOptionalMove serializableFromClient = (SerializableOptionalMove) fromClient;
                for (int i = 0; i < observerList.size(); i++)
                    observerList.get(i).onOptionalMove(playerId, serializableFromClient.isWantToMove());
            }
            if (fromClient instanceof SerializableOptionalBuild) {
                SerializableOptionalBuild serializableFromClient = (SerializableOptionalBuild) fromClient;
                for (int i = 0; i < observerList.size(); i++)
                    observerList.get(i).onOptionalBuild(playerId, serializableFromClient.isWantToBuild());
            }
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
            if (fromClient instanceof SerializableInitializeWorkers) {
                SerializableInitializeWorkers serializableFromClient = (SerializableInitializeWorkers) fromClient;
                for (int i = 0; i < observerList.size(); i++)
                    observerList.get(i).onInitialization(playerId, serializableFromClient.getWorkerPositions());
            }
            if (fromClient instanceof SerializableReady) {
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onReady(playerId);
            }
        } catch (ClientStoppedWorkingException e){
            if (e.isWasItTimeOut()){
                // il giocatore non ha risposto entro il tempo stabilito, quindi ha perso

            } else {
                // il giocatore si è disconnesso, quindi la partita termina
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onPlayerDisconnection(playerId);
            }
        }
    }

    // aggiorna tutti i player e risponde a un singolo player
    public void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableAnswer answer) throws IOException {
        serverThread.sendAllObject(update);
        answerOnePlayer(answer);
    }

    // aggiorna tutti i player due volte e risponde a un singolo player
    public void updateAllTwiceAndAnswerOnePlayer(SerializableUpdate update1, SerializableUpdate update2, SerializableAnswer answer) throws IOException {
        serverThread.sendAllObject(update1);
        serverThread.sendAllObject(update2);
        answerOnePlayer(answer);
    }

    public ServerProxy(ServerThread serverThread) {
        this.serverThread = serverThread;
        observerList = new ArrayList();
    }
}
