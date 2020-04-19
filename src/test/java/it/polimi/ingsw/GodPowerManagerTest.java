package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

public class GodPowerManagerTest {
    @Test
    public void Test() throws ParseException, IOException {
        List<GodPower> godPowers = GodPowerManager.createGodPowers(3);
        assert (godPowers.size()==3);
        assert (godPowers.stream().map(x->x!=null).reduce(true, (a, b)->a&&b)); //Todo: ci sono valori per cui questa istruzione non è sempre vera
        assert (godPowers.stream().map(x->x.getMove()!=null).reduce(true, (a, b)->a&&b));
        assert (godPowers.stream().map(x->x.getBuild()!=null).reduce(true, (a, b)->a&&b));
        assert (godPowers.stream().map(x->x.getConsolidateMove()!=null).reduce(true, (a, b)->a&&b));
        assert (godPowers.stream().map(x->x.getConsolidateBuild()!=null).reduce(true, (a, b)->a&&b));
        assert (godPowers.stream().map(x->x.getBlockingWinConditions()!=null).reduce(true, (a, b)->a&&b));
        assert (godPowers.stream().map(x->x.getPositiveWinConditions()!=null).reduce(true, (a, b)->a&&b));
        assert (godPowers.stream().map(x->x.getLoseCondition()!=null).reduce(true, (a, b)->a&&b));
        assert (godPowers.stream().map(x->x.getNewTurn()!=null).reduce(true, (a, b)->a&&b));
    }
}
