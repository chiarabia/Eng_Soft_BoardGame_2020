package it.polimi.ingsw;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

 public class JSONManager {

     public static JSONObject readMyJSONAsText(String fname) throws ParseException {
         InputStream is = App.class.getClassLoader().getResourceAsStream(fname);
         BufferedReader buf;
         try {
             buf = new BufferedReader(new InputStreamReader(is));
         } catch (NullPointerException e){return null;}

        String line = null;
        try {
            line = buf.readLine();
        } catch (IOException e) {e.printStackTrace();}

        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            try {
                line = buf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String fileAsString = sb.toString();
        return (JSONObject) new JSONParser().parse(fileAsString);
    }
}
