import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Parser {
    // constant for the ds-sim command JOBN
    private static final String JOBN = "JOBN";

    // constant for the ds-system.xml filename
    private static final String DS_SYSTEM = "ds-system.xml";

    // constant for the <server> tag in ds-system.xml
    private static final String SERVER = "server";

    // constants for the XML attributes in ds-system.xml
    private static final String TYPE = "type";
    private static final String LIMIT = "limit";
    private static final String BOOTUP_TIME = "bootupTime";
    private static final String HOURLY_RATE = "hourlyRate";
    private static final String CORE = "coreCount";
    private static final String MEMORY = "memory";
    private static final String DISK = "disk";

    // parse the string response into a Job object
    public static Job parseJob(String response) {
        // initialize the Job object
        Job job = null;

        try {
            // response is split into a string array
            String[] specs = response.split("\\s+");

            // parse the string array into the Job object it it is valid
            if (specs[0].equals(JOBN)) {
                job = new Job(Integer.parseInt(specs[1]), Integer.parseInt(specs[2]), Integer.parseInt(specs[3]),
                        Integer.parseInt(specs[4]), Integer.parseInt(specs[5]), Integer.parseInt(specs[6]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return job;
    }

    // parse the string response into a Server object
    public static Server parseServer(String response) {
        // initialize the Server object
        Server server = null;

        try {
            // response is split into a string array
            String[] specs = response.split("\\s+");

            // parse the string array into the Server object
            server = new Server(specs[0], Integer.parseInt(specs[1]), specs[2], Integer.parseInt(specs[3]),
                    Integer.parseInt(specs[4]), Integer.parseInt(specs[5]), Integer.parseInt(specs[6]),
                    Integer.parseInt(specs[7]), Integer.parseInt(specs[8]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return server;
    }

    // parse all servers from ds-system.xml into a list of XML servers
    public static ArrayList<Server> parseXMLServers() {
        // initialize the list to store the XML servers
        ArrayList<Server> servers = new ArrayList<Server>();

        try {
            // process "ds-system.xml"
            File dsSystem = new File(DS_SYSTEM);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(dsSystem);
            document.getDocumentElement().normalize();

            // get all nodes with the <server> tag
            NodeList xmlServers = document.getElementsByTagName(SERVER);

            // loop through all the extracted nodes
            for (int i = 0; i < xmlServers.getLength(); i++) {
                // get the node and typecast it to an XML element
                Node node = xmlServers.item(i);
                Element specs = (Element) node;

                // get all the attributes from the XML element
                String type = specs.getAttribute(TYPE);
                int limit = Integer.parseInt(specs.getAttribute(LIMIT));
                int bootupTime = Integer.parseInt(specs.getAttribute(BOOTUP_TIME));
                float hourlyRate = Float.parseFloat(specs.getAttribute(HOURLY_RATE));
                int core = Integer.parseInt(specs.getAttribute(CORE));
                int memory = Integer.parseInt(specs.getAttribute(MEMORY));
                int disk = Integer.parseInt(specs.getAttribute(DISK));

                // parse all the extracted attributes into a new Server object
                // and add it to the list of XML servers
                servers.add(new Server(type, limit, bootupTime, hourlyRate, core, memory, disk));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servers;
    }
}
