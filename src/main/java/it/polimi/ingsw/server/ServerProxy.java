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
    // justUpdateAll deve servire SOLO per comunicare il termine della partita (vittoria o disconnessione player)
    public void justUpdateAll(SerializableUpdate update) throws IOException {
        serverThread.sendAllObject(update);
    }

    public void justUpdateAll(List<SerializableUpdate> updates) throws IOException {
        for (SerializableUpdate update : updates) serverThread.sendAllObject(update);
    }

    // risponde a un singolo player
    public void answerOnePlayer(SerializableRequest request) throws IOException{
        int playerId = request.getPlayerId();
        try {
            Object fromClient = serverThread.sendObjectAndWaitForReply(request, request.getPlayerId() - 1, 300);
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
            if (fromClient instanceof SerializableInitializeGame) {
                SerializableInitializeGame serializableFromClient = (SerializableInitializeGame) fromClient;
                for (int i = 0; i < observerList.size(); i++)
                    observerList.get(i).onInitialization(playerId, serializableFromClient.getWorkerPositions(), serializableFromClient.getGodPower());
            }
            if (fromClient instanceof SerializableDeclineLastOptional) {
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onEndedTurn(playerId);
            }
        } catch (ClientStoppedWorkingException e){
            if (e.isWasItTimeOut()){
                // il giocatore non ha risposto entro il tempo stabilito, quindi ha perso
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onPlayerLoss(playerId);
            } else {
                // il giocatore si è disconnesso, quindi la partita termina
                for (int i = 0; i < observerList.size(); i++) observerList.get(i).onPlayerDisconnection(playerId);
            }
        }
    }

    // aggiorna tutti i player e risponde a un singolo player
    public void updateAllAndAnswerOnePlayer(List<SerializableUpdate> updates, SerializableRequest request) throws IOException{
        for (SerializableUpdate update : updates) serverThread.sendAllObject(update);
        answerOnePlayer(request);
    }

    public void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableRequest request) throws IOException{
        List <SerializableUpdate> updates = new ArrayList<>();
        updates.add(update);
        updateAllAndAnswerOnePlayer(updates, request);
    }

    public ServerProxy(ServerThread serverThread) {
        this.serverThread = serverThread;
        observerList = new ArrayList();
    }
}
