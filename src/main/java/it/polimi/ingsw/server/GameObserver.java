package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.server.serializable.SerializableRequest;
import it.polimi.ingsw.server.serializable.SerializableUpdate;

import java.io.IOException;
import java.util.List;

public interface GameObserver {
    void justUpdateAll(SerializableUpdate update);
    void justUpdateAll(List<SerializableUpdate> updates);
    void answerOnePlayer(SerializableRequest request);
    void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableRequest request);
    void updateAllAndAnswerOnePlayer(List<SerializableUpdate> updates, SerializableRequest request);
}
