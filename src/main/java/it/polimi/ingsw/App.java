package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.InputStream;

public class App
{
    private static String getRoot(){
        String root = ClassLoader.getSystemClassLoader().getResource("configurations/Configuration.json").getPath();
        if (root.substring(2,3).equals(":")) return root.substring(3, root.length()-18) + "cards/";
        else return root.substring(0, root.length()-18);
    }
    public static void main( String[] args ) throws InterruptedException {
        try {
            FileReader fileReader = new FileReader(getRoot());
            JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fileReader);
            boolean server = (jsonObject.get("role")).equals("server");
            boolean GUI = (jsonObject.get("ui")).equals("GUI");
            String ip = (String) jsonObject.get("ip");
            int port = Math.toIntExact((Long) jsonObject.get("port"));

            try {
                for (int i = 0; i < args.length; i++) {
                    String argument = args[i];
                    switch (argument) {
                        case "--role":
                            server = args[i + 1].equals("server");
                            break;
                        case "--ui":
                            GUI = args[i + 1].equals("GUI");
                            break;
                        case "--ip":
                            ip = args[i + 1];
                            break;
                        case "--port":
                            port = Integer.parseInt(args[i + 1]);
                            break;
                    }
                }
            } catch (Exception e){}

            //server = true;
            if (server) (new Server()).startServer(port);
            else (new Client()).startClient(port, ip, GUI);
        }catch(Exception e){ System.out.println("An error occurred");
        e.printStackTrace();}
    }
}
