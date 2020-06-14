package it.polimi.ingsw.server.serializable;

import java.util.ArrayList;
import java.util.List;


/**
 * This object contains all the information about the action the player has made.
 * I due insiemi contengono le informazioni necessarie per mostrare alla view le modifiche avvenute nella board.
 * In seguito ad una moveinto verrà sempre restituito un oggetto SerializaleUpdateInfos con almeno 1 oggetto nella lista serializable UpdateMove.
 * Allo stesso modo, ci sarà almeno un oggetto in updateBuild dopo una buildup.
 * Se nella move sono stati spostati altri lavoratori, sarà presente un ulteriore oggetto SerializableUpdateMove con l'id del player avversario proprietario del worker
 * l'id del worker avversario e le posizioni iniziali e finali di quest'ultimo.
 * In seguito ad una buildup è possibile che sia presente un oggetto anche in update Move, come nel caso della buildUp di UnderWorker.
 */

public class SerializableUpdateInfos implements SerializableUpdate {
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
