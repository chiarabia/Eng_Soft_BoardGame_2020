package it.polimi.ingsw.server.serializable;

import java.util.ArrayList;
import java.util.List;

public class SerializableUpdateInfos implements Serializable {
        List<SerializableUpdateMove> updateMove;
        List<SerializableUpdateBuild> updateBuild;

        public SerializableUpdateInfos() {
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
        public void mergeInfos (SerializableUpdateInfos serializableUpdateInfos) {
            if (serializableUpdateInfos!=null) {
                if (serializableUpdateInfos.getUpdateBuild()!=null){
                    this.updateBuild.addAll(serializableUpdateInfos.getUpdateBuild());
                }
                if (serializableUpdateInfos.getUpdateMove()!=null){
                    this.updateMove.addAll(serializableUpdateInfos.getUpdateMove());
                }
            }
        }
    }
