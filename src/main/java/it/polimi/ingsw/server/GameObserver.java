package it.polimi.ingsw.server;

import it.polimi.ingsw.server.serializable.SerializableAnswer;
import it.polimi.ingsw.server.serializable.SerializableUpdate;

import java.io.IOException;

public interface GameObserver {
    void updateAll(SerializableUpdate update) throws IOException;
    void answerOnePlayer(SerializableAnswer answer) throws IOException;
    void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableAnswer answer) throws IOException;
    void updateAllTwiceAndAnswerOnePlayer(SerializableUpdate update1, SerializableUpdate update2, SerializableAnswer answer) throws IOException;
}
