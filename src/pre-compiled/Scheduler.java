import java.util.*;

public class Scheduler {
    /**
     * 
     * @param servers    the list of capable servers returned from server
     * @param xmlServers the list of XML servers in ds-system.xml
     * @return the cheapest capable server based on hourly rate / core count
     */
    public static Server allToCheapest(ArrayList<Server> servers, ArrayList<Server> xmlServers) {
        // get all XML servers from xmlServers that are in the list of capable servers
        ArrayList<Server> tempServers = new ArrayList<Server>();
        for (Server xmlServer : xmlServers) {
            for (Server server : servers) {
                if (server.getType().equals(xmlServer.getType())) {
                    tempServers.add(xmlServer);
                    break;
                }
            }
        }

        // get the cheapest XML server in tempServers based on hourly rate / core count
        Server xmlCheapest = tempServers.get(0);
        for (Server xmlServer : tempServers) {
            if (xmlServer.getHourlyRate() / xmlServer.getCore() < xmlCheapest.getHourlyRate() / xmlCheapest.getCore()) {
                xmlCheapest = xmlServer;
            }
        }

        // get the server in the list of capable servers with the same type as xmlCheapest and smallest id
        Server cheapest = servers.get(0);
        for (Server server : servers) {
            if (server.getType().equals(xmlCheapest.getType()) && server.getId() <= cheapest.getId()) {
                cheapest = server;
            }
        }

        return cheapest;
    }
}
