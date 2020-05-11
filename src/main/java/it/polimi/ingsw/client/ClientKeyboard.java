package it.polimi.ingsw.client;

import it.polimi.ingsw.Position;

import java.util.*;

public class ClientKeyboard {
    private final static Scanner keyboard = new Scanner(System.in);

    public static String askForGodPower (List<String> godPowers){
        String godPower = "";
        if(godPowers.size()==1) return godPowers.get(0);
        while (true) {
            for (String s : godPowers) System.out.print(s + " ");
            godPower = askForString("left, choose God Power: ");
            String finalGodPower = godPower;
            if  (godPowers.stream().anyMatch(x -> x.equals(finalGodPower))) break;
        }
        return godPower;
    }

    public static List <Position> askForWorkersInitialPositions (){
        int myWorker1x = askForInt("Worker 1 X: ");
        int myWorker1y = askForInt("Worker 1 Y: ");
        int myWorker2x = askForInt("Worker 2 X: ");
        int myWorker2y = askForInt("Worker 2 Y: ");
        List<Position> myWorkerPositions = new ArrayList<>();
        myWorkerPositions.add(new Position(myWorker1x, myWorker1y, 0));
        myWorkerPositions.add(new Position(myWorker2x, myWorker2y, 0));
        return myWorkerPositions;
    }

    public static Position askForPosition(){
        int x = askForInt("x: ");
        int y = askForInt("y: ");
        int z = askForInt("z: ");
        return new Position(x, y, z);
    }

    public static int askForInt(String request){
        try {
            System.out.print(request);
            int fromKeyboard = keyboard.nextInt();
            return fromKeyboard;
        } catch(Exception e){return askForInt(request);}
    }

    public static String askForString(String request){
        System.out.print(request);
        String fromKeyboard = keyboard.next();
        return fromKeyboard;
    }

    public static boolean askForBoolean(String request){
        String fromKeyboard;
        while (true) {
            System.out.print(request);
            fromKeyboard = keyboard.next();
            if (fromKeyboard.toLowerCase().equals("y") || fromKeyboard.toLowerCase().equals("n")) break;
        }
        return fromKeyboard.toLowerCase().equals("y");
    }
}
