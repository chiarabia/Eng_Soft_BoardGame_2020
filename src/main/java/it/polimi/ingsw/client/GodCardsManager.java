package it.polimi.ingsw.client;


import java.io.FileReader;
import java.io.IOException;

import it.polimi.ingsw.JSONManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GodCardsManager {

    public static GodCard getCard (String nameOfFile) throws IOException, ParseException {
        JSONObject jsonObject = JSONManager.readMyJSONAsText("godDescriptions/" + nameOfFile + ".json");

        String name = (String) jsonObject.get("name");
        String description = (String) jsonObject.get("description");
        String image = (String) jsonObject.get("image code");

        return new GodCard(name, description, image);
    }
}
