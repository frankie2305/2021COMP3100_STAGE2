import java.util.*;

public class Scheduler {
    public static Server allToCheapest(ArrayList<Server> servers, ArrayList<Server> xmlServers) {
        ArrayList<Server> tempServers = new ArrayList<Server>();

        for (Server xmlServer : xmlServers) {
            for (Server server : servers) {
                if (server.getType().equals(xmlServer.getType())) {
                    tempServers.add(xmlServer);
                    break;
                }
            }
        }

        Server xmlCheapest = tempServers.get(0);

        for (Server xmlServer : tempServers) {
            if (xmlServer.getHourlyRate() / xmlServer.getCore() < xmlCheapest.getHourlyRate() / xmlCheapest.getCore()) {
                xmlCheapest = xmlServer;
            }
        }

        Server cheapest = servers.get(0);

        for (Server server : servers) {
            if (server.getType().equals(xmlCheapest.getType()) && server.getId() <= cheapest.getId()) {
                cheapest = server;
            }
        }

        return cheapest;
    }
}
