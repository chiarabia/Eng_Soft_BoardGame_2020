package it.polimi.ingsw;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * Hello world!
 *
 */

public class App
{
    private final static String ROOT = "src/main/java/it/polimi/ingsw/Configuration.json";
    public static void main( String[] args ) throws InterruptedException {
        try {
            FileReader fileReader = new FileReader(ROOT);
            JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fileReader);
            boolean server = (jsonObject.get("role")).equals("server");
            boolean GUI = (jsonObject.get("ui")).equals("GUI");
            String ip = (String) jsonObject.get("ip");
            int port = Math.toIntExact((Long) jsonObject.get("port"));

            //todo:inserire lettura da args[] che EVENTUALMENTE modifica i quattro valori appena impostati

            if (server) (new Server()).startServer(port);
            else (new Client()).startClient(port, ip, GUI);
        }catch(Exception e){ System.out.println("An error occurred");}
    }
}
