package it.polimi.ingsw.server;

import it.polimi.ingsw.server.serializable.SerializableAnswer;
import it.polimi.ingsw.server.serializable.SerializableUpdate;

import java.io.IOException;

public interface GameObserver {
    void answerOnePlayer(SerializableAnswer answer) throws IOException;
    void updateAll(SerializableUpdate update) throws IOException;
    void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableAnswer answer) throws IOException;
}
