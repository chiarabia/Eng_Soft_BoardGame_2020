package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.server.Server;
import org.json.simple.JSONObject;

public class App{
    /**
     * This method starts a new Server/Client session, reading preferences from
     * arguments and configuration file about role, port, IP address and UI.
     * @param args (--role server/client, --ui CLI/GUI, --ip localhost, --port 555)
     */
        public static void main( String[] args ) {
        try {
            JSONObject jsonObject = JSONManager.readMyJSONAsText("configurations/Configuration.json");

            boolean server = (jsonObject.get("role")).equals("server");
            boolean GUI = (jsonObject.get("ui")).equals("GUI");
            String ip = (String) jsonObject.get("ip");
            int port = Math.toIntExact((Long) jsonObject.get("port"));

            try {
                for (int i = 0; i < args.length; i++) {
                    String argument = args[i];
                    switch (argument) {
                        case "--role":
                            server = args[i + 1].equals("server");
                            break;
                        case "--ui":
                            GUI = args[i + 1].equals("GUI");
                            break;
                        case "--ip":
                            ip = args[i + 1];
                            break;
                        case "--port":
                            port = Integer.parseInt(args[i + 1]);
                            break;
                    }
                }
            } catch (Exception e){}

            if (server) (new Server()).startServer(port);
            else (new Client()).startClient(port, ip, GUI);
        }catch(Exception e){ System.out.println("An error occurred");}
    }


}

