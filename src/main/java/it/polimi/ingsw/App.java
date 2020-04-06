package it.polimi.ingsw;
import it.polimi.ingsw.Effects.GodPowerManager;
import it.polimi.ingsw.Server.Server;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Hello world!
 *
 */

public class App
{
    public static void main( String[] args ) throws IOException, ParseException {
        Server.startServer();
    }
}
