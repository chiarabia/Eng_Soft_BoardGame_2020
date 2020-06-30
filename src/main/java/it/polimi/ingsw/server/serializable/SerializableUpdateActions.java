package it.polimi.ingsw.server.serializable;

import java.util.ArrayList;
import java.util.List;


/** This object contains information about the actions the player has made stored in a moves list and a builds list */

public class SerializableUpdateActions implements SerializableUpdate {
        List<SerializableUpdateMove> updateMove;
        List<SerializableUpdateBuild> updateBuild;

        public SerializableUpdateActions() {
            this.updateMove = new ArrayList<>();
            this.updateBuild = new ArrayList<>();
        }

        public List<SerializableUpdateMove> getUpdateMove() {
            return updateMove;
        }

        public void addMoveAction(SerializableUpdateMove updateMove) {
            if (this.updateMove!=null) {
                this.updateMove.add(updateMove);
            }
        }

        public List<SerializableUpdateBuild> getUpdateBuild() {
            return updateBuild;
        }

        public void addBuildAction(SerializableUpdateBuild updateBuild) {
            if (this.updateBuild!=null) {
                this.updateBuild.add(updateBuild);
            }
        }
        public void mergeInfos (SerializableUpdateActions serializableUpdateActions) {
            if (serializableUpdateActions !=null) {
                if (serializableUpdateActions.getUpdateBuild()!=null){
                    this.updateBuild.addAll(serializableUpdateActions.getUpdateBuild());
                }
                if (serializableUpdateActions.getUpdateMove()!=null){
                    this.updateMove.addAll(serializableUpdateActions.getUpdateMove());
                }
            }
        }
    }
