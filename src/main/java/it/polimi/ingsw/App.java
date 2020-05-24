package it.polimi.ingsw;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Hello world!
 *
 */

public class App
{
    public static void main( String[] args ) throws InterruptedException {
        //Server.startServer(555);
        Client.startClient(555,  "localhost");
    }
}
