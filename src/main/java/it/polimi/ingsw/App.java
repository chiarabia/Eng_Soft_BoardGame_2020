package it.polimi.ingsw;
import it.polimi.ingsw.Exceptions.WrongInputException;
import org.json.simple.parser.ParseException;
import java.io.IOException;

/**
 * Hello world!
 *
 */

public class App
{
    public static void main( String[] args ) throws ParseException, WrongInputException, IOException {
        Power p = new Power("Triton");
        System.out.println("Can I build twice? " + p.getCanIBuildTwice());
    }
}
