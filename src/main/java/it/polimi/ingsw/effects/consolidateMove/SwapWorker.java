package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.*;
import it.polimi.ingsw.server.serializable.SerializableUpdate;
import it.polimi.ingsw.server.serializable.SerializableUpdateInfos;
import it.polimi.ingsw.server.serializable.SerializableUpdateMove;


public class SwapWorker extends StandardConsolidateMove {
        public SerializableUpdateInfos moveInto (Board board, Position workerPosition, Position destinationPosition){
            Cell workerCell = board.getCell(workerPosition);
            Cell destinationCell = board.getCell(destinationPosition);


            // se nella cella di arrivo non c'è un lavoratore, eseguo il metodo della classe padre
            if (destinationCell.isFree()) return super.moveInto(board, workerPosition, destinationPosition);
            else {
                int myWorkerId = workerCell.getWorkerId();
                int myPlayerId = workerCell.getPlayerId();
                int enemyWorkerId = destinationCell.getWorkerId();
                int enemyPlayerId = destinationCell.getPlayerId();
                Worker tempWorker = workerCell.getWorker();
                workerCell.setWorker(destinationCell.getWorker());
                destinationCell.setWorker(tempWorker);

                SerializableUpdateMove myWorkerInfos = new SerializableUpdateMove(workerPosition,destinationPosition, myPlayerId, myWorkerId);
                SerializableUpdateMove enemyWorkerInfos = new SerializableUpdateMove(destinationPosition, workerPosition, enemyPlayerId, enemyWorkerId);

                SerializableUpdateInfos serializableUpdateInfos = new SerializableUpdateInfos();
                serializableUpdateInfos.addMoveAction(myWorkerInfos);
                serializableUpdateInfos.addMoveAction(enemyWorkerInfos);
                return serializableUpdateInfos;
            }
        }
    }