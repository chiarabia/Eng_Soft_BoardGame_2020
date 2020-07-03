package it.polimi.ingsw.client;

import it.polimi.ingsw.JSONManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * This class manages the JSON files with the information to create the GodCards
 */
public class GodCardsManager {
    /**
     * Reads and stores god cards information from its JSON file
     *@param  nameOfFile name of JSON file (ex. GodCard1)
     *@throws ParseException ParseException
     *@return GodCard
     */
    public static GodCard getCard (String nameOfFile) throws ParseException {
        JSONObject jsonObject = JSONManager.readMyJSONAsText("godDescriptions/" + nameOfFile + ".json");

        String name = (String) jsonObject.get("name");
        String description = (String) jsonObject.get("description");
        String image = (String) jsonObject.get("image code");

        return new GodCard(name, description, image);
    }
}
