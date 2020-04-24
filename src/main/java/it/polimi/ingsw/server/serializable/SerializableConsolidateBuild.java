package it.polimi.ingsw.server.serializable;

import it.polimi.ingsw.Position;

public class SerializableConsolidateBuild implements java.io.Serializable {
        private Position newPosition;

        public Position getNewPosition() {
            return newPosition;
        }

        public SerializableConsolidateBuild(Position newPosition) {
            this.newPosition = newPosition;
        }
}
