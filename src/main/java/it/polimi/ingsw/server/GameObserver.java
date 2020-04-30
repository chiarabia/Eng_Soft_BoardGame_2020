package it.polimi.ingsw.server;

import it.polimi.ingsw.server.serializable.SerializableRequest;
import it.polimi.ingsw.server.serializable.SerializableUpdate;

import java.io.IOException;
import java.util.List;

public interface GameObserver {
    void justUpdateAll(SerializableUpdate update) throws IOException;
    void answerOnePlayer(SerializableRequest request) throws IOException;
    void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableRequest request) throws IOException;
    void updateAllAndAnswerOnePlayer(List<SerializableUpdate> updates, SerializableRequest request) throws IOException;
}
