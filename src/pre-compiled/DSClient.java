import java.util.*;
import java.net.*;
import java.io.*;

public class DSClient {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 50000;
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

    private static Socket s = null;
    private static BufferedReader in = null;
    private static DataOutputStream out = null;

    private static final ArrayList<Server> xmlServers = Parser.parseXMLServers();

    private static void send(String request) {
        try {
            out.write((request + "\n").getBytes());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            s = new Socket(DEFAULT_HOST, DEFAULT_PORT);

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new DataOutputStream(s.getOutputStream());

            String request = HELO;
            send(request);

            int nRecs = 0;
            int counter = 0;

            Job job = null;
            Server server = null;
            ArrayList<Server> servers = new ArrayList<Server>();

            while (true) {
                String response = in.readLine();
                // System.out.println(response);

                if (request.equals(HELO) && response.equals(OK)) {
                    String username = System.getProperty("user.name");

                    request = AUTH + " " + username;
                    send(request);
                    continue;
                }

                if (request.startsWith(AUTH) && response.equals(OK)) {
                    request = REDY;
                    send(request);
                    continue;
                }

                if (request.equals(REDY)) {
                    if (response.equals(NONE)) {
                        request = QUIT;
                    } else {
                        job = Parser.parseJob(response);

                        if (job != null) {
                            request = GETS_CAPACLE + " " + job.getCore() + " " + job.getMemory() + " " + job.getDisk();
                        }
                    }
                    send(request);
                    continue;
                }

                if (request.startsWith(GETS_CAPACLE)) {
                    String[] data = response.split("\\s+");

                    if (data[0].equals(DATA)) {
                        nRecs = Integer.parseInt(data[1]);

                        request = OK;
                        send(request);
                        continue;
                    }
                }

                if (request.equals(OK)) {
                    if (response.equals(DOT)) {
                        server = Scheduler.allToCheapest(servers, xmlServers);

                        servers.clear();

                        counter = 0;

                        request = SCHD + " " + job.getId() + " " + server.getType() + " " + server.getId();
                    } else {
                        servers.add(Parser.parseServer(response));

                        counter++;

                        if (counter < nRecs) {
                            continue;
                        }
                    }
                    send(request);
                    continue;
                }

                if (request.startsWith(SCHD) && response.equals(OK)) {
                    request = REDY;
                    send(request);
                    continue;
                }

                if ((request.equals(QUIT) && response.equals(QUIT)) || response.startsWith(ERR)) {
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
