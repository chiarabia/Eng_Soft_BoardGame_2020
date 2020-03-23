package it.polimi.ingsw;
import it.polimi.ingsw.Exceptions.WrongInputException;
import it.polimi.ingsw.Server.Server;
import org.json.simple.parser.ParseException;
import java.io.IOException;

/**
 * Hello world!
 *
 */

public class App
{
    public static void main( String[] args ) throws ParseException, WrongInputException, IOException {
        System.out.println("Can I build twice? " + (new Power("Triton")).getCanIBuildTwice());
        Server.startServer();
    }
}
