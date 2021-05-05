import java.util.*;
import java.net.*;
import java.io.*;

public class DSClient {
    // constants for the default host and port of socket connection
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 50000;

    // constants for ds-sim commands
    private static final String HELO = "HELO";
    private static final String AUTH = "AUTH";
    private static final String QUIT = "QUIT";
    private static final String REDY = "REDY";
    private static final String DATA = "DATA";
    private static final String NONE = "NONE";
    private static final String GETS_CAPACLE = "GETS Capable";
    private static final String SCHD = "SCHD";
    private static final String ERR = "ERR";
    private static final String OK = "OK";
    private static final String DOT = ".";

    // socket connection, input and output streams
    private static Socket s = null;
    private static BufferedReader in = null;
    private static DataOutputStream out = null;

    // add "\n" to the end of the request and send it to server in bytes
    private static void send(String request) {
        try {
            out.write((request + "\n").getBytes());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            // create socket connection with the default host and port
            s = new Socket(DEFAULT_HOST, DEFAULT_PORT);

            // input and output streams of socket connection
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new DataOutputStream(s.getOutputStream());

            // client starts by sending "HELO" to server
            String request = HELO;
            send(request);

            // variables to keep track of records received from server
            int nRecs = 0;
            int counter = 0;

            // variable to store the job received from server
            Job job = null;

            // variable to store the server to schedule the job to
            Server server = null;

            // variable to store capable servers received from server
            ArrayList<Server> servers = new ArrayList<Server>();

            // variable to store all servers in ds-system.xml
            ArrayList<Server> xmlServers = new ArrayList<Server>();

            while (true) {
                // parse the response from server as a string
                String response = in.readLine();

                // client sent "HELO" and received "OK" from server
                if (request.equals(HELO) && response.equals(OK)) {
                    // current username of the system
                    String username = System.getProperty("user.name");

                    // client sends "AUTH <username>" to server
                    request = AUTH + " " + username;
                    send(request);
                    continue;
                }

                // client sent "AUTH..." and received "OK" from server
                if (request.startsWith(AUTH) && response.equals(OK)) {
                    // all servers in ds-system.xml are parsed into xmlServers
                    xmlServers = Parser.parseXMLServers();

                    // client sends "REDY"
                    request = REDY;
                    send(request);
                    continue;
                }

                // client sent "REDY"
                if (request.equals(REDY)) {
                    // server sent "NONE" for no more jobs
                    if (response.equals(NONE)) {
                        // request becomes "QUIT"
                        request = QUIT;
                    } else {
                        // otherwise the response is parsed into job
                        job = Parser.parseJob(response);

                        // request becomes "GETS Capable <core> <memory> <disk>" if job is valid,
                        // otherwise it remains "REDY"
                        if (job != null) {
                            request = GETS_CAPACLE + " " + job.getCore() + " " + job.getMemory() + " " + job.getDisk();
                        }
                    }
                    // client sends the appropriate request to server based on conditions above
                    send(request);
                    continue;
                }

                // client sent "GETS Capable..."
                if (request.startsWith(GETS_CAPACLE)) {
                    // response is split into a string array
                    String[] data = response.split("\\s+");

                    // if response starts with "DATA"
                    if (data[0].equals(DATA)) {
                        // the second string is parsed into nRecs
                        nRecs = Integer.parseInt(data[1]);

                        // client sends "OK" to server
                        request = OK;
                        send(request);
                        continue;
                    }
                }

                // client sent "OK"
                if (request.equals(OK)) {
                    // server sent "." for no more records
                    if (response.equals(DOT)) {
                        // get the server to schedule the job to based on allToCheapest algorithm
                        server = Scheduler.allToCheapest(servers, xmlServers);

                        // clear the list of capable servers
                        servers.clear();

                        // reset the counter for the number of records
                        counter = 0;

                        // request becomes "SCHD <jobId> <serverType> <serverId>"
                        request = SCHD + " " + job.getId() + " " + server.getType() + " " + server.getId();
                    } else {
                        // otherwise add the new server parsed to the list of capable servers
                        servers.add(Parser.parseServer(response));

                        // increment the counter for the number of records
                        counter++;

                        // if there are still records left then continue parsing
                        if (counter < nRecs) {
                            continue;
                        }
                    }
                    // client sends the request to server after all records are parsed
                    send(request);
                    continue;
                }

                // client sent "SCHD..." and server sent "OK"
                if (request.startsWith(SCHD) && response.equals(OK)) {
                    // client sends "REDY" to get the next job
                    request = REDY;
                    send(request);
                    continue;
                }

                // both client and server sent "QUIT" or the server's response was an error
                if ((request.equals(QUIT) && response.equals(QUIT)) || response.startsWith(ERR)) {
                    // break the while loop to close the socket connection
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("Close: " + e.getMessage());
                }
            }
        }
    }
}
