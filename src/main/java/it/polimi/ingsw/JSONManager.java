package it.polimi.ingsw;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

 public class JSONManager {
     /**
      * Reads a text file and converts it into JSONObject
      * @param fname name of file
      * @return JSONObject
      * @throws ParseException ParseException
      */
     public static JSONObject readMyJSONAsText(String fname) throws ParseException {
         InputStream is = App.class.getClassLoader().getResourceAsStream(fname);
         BufferedReader buf;
         try {
             buf = new BufferedReader(new InputStreamReader(is));
         } catch (NullPointerException e){return null;}

        String line = null;
        try {
            line = buf.readLine();
<<<<<<< HEAD
        } catch (IOException e) {e.printStackTrace();}
=======
        } catch (IOException e) {}
>>>>>>> upstream/new_assets

        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            try {
                line = buf.readLine();
<<<<<<< HEAD
            } catch (IOException e) {
                e.printStackTrace();
            }
=======
            } catch (IOException e) {}
>>>>>>> upstream/new_assets
        }

        String fileAsString = sb.toString();
        return (JSONObject) new JSONParser().parse(fileAsString);
    }
}
