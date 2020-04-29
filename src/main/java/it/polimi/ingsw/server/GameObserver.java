package it.polimi.ingsw.server;

import it.polimi.ingsw.server.serializable.SerializableRequest;
import it.polimi.ingsw.server.serializable.SerializableUpdate;

import java.io.IOException;

public interface GameObserver {
    void updateAll(SerializableUpdate update) throws IOException;
    void answerOnePlayer(SerializableRequest request) throws IOException;
    void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableRequest request) throws IOException;
    void updateAllTwiceAndAnswerOnePlayer(SerializableUpdate update1, SerializableUpdate update2, SerializableRequest request) throws IOException;
}
