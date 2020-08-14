package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.server.serializable.SerializableRequest;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdate;

import java.util.List;

/**
 * This interface implements the model observers (like ServerView)
 */

public interface GameObserver {
    void justUpdateAll(SerializableUpdate update);
    void justUpdateAll(List<SerializableUpdate> updates);
    void answerOnePlayer(SerializableRequest request);
    void updateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableRequest request);
    void updateAllAndAnswerOnePlayer(List<SerializableUpdate> updates, SerializableRequest request);
}
