package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;
import java.util.Set;

public class SerializableAnswerMove implements SerializableAnswer {
    private final int playerId;
    private final Set<Position> moves;

    public int getPlayerId() { return playerId; }

    public Set<Position> getMoves() {
        return moves;
    }

    public SerializableAnswerMove(int playerId, Set<Position> moves) {
        this.playerId = playerId;
        this.moves = moves;
    }
}
