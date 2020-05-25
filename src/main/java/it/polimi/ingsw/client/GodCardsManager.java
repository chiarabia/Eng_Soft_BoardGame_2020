package it.polimi.ingsw.client;


import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GodCardsManager {
    private final static String ROOT = "src/resources/godDescriptions/";

    public static GodCard getCard (String nameOfFile) throws IOException, ParseException {
        FileReader fileReader = new FileReader(ROOT + nameOfFile);
        JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fileReader);

        String name = (String) jsonObject.get("name");
        String description = (String) jsonObject.get("description");
        String image = (String) jsonObject.get("image code");

        GodCard card = new GodCard(name, description, image);

        return card;
    }
}
