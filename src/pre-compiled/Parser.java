import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Parser {
    public static Job parseJob(String response) {
        Job job = null;

        try {
            String[] specs = response.split("\\s+");

            if (specs[0].equals("JOBN")) {
                job = new Job(Integer.parseInt(specs[1]), Integer.parseInt(specs[2]), Integer.parseInt(specs[3]),
                        Integer.parseInt(specs[4]), Integer.parseInt(specs[5]), Integer.parseInt(specs[6]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return job;
    }

    public static Server parseServer(String response) {
        Server server = null;

        try {
            String[] specs = response.split("\\s+");

            server = new Server(specs[0], Integer.parseInt(specs[1]), specs[2], Integer.parseInt(specs[3]),
                    Integer.parseInt(specs[4]), Integer.parseInt(specs[5]), Integer.parseInt(specs[6]),
                    Integer.parseInt(specs[7]), Integer.parseInt(specs[8]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return server;
    }

    public static ArrayList<Server> parseXMLServers() {
        ArrayList<Server> servers = new ArrayList<Server>();
        try {
            File dsSystem = new File("ds-system.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(dsSystem);
            document.getDocumentElement().normalize();
            NodeList xmlServers = document.getElementsByTagName("server");
            for (int i = 0; i < xmlServers.getLength(); i++) {
                Node node = xmlServers.item(i);
                Element specs = (Element) node;
                String type = specs.getAttribute("type");
                int limit = Integer.parseInt(specs.getAttribute("limit"));
                int bootupTime = Integer.parseInt(specs.getAttribute("bootupTime"));
                float hourlyRate = Float.parseFloat(specs.getAttribute("hourlyRate"));
                int core = Integer.parseInt(specs.getAttribute("coreCount"));
                int memory = Integer.parseInt(specs.getAttribute("memory"));
                int disk = Integer.parseInt(specs.getAttribute("disk"));
                servers.add(new Server(type, limit, bootupTime, hourlyRate, core, memory, disk));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servers;
    }
}
