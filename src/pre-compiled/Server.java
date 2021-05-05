public class Server {
    // all server attributes (both from ds-system.xml and server)
	private int id;
	private String type;
	private int limit;
	private int bootupTime;
	private float hourlyRate;
	private int core;
	private int memory;
	private int disk;
	private String state;
	private int curStartTime;
    private int waitingJobs;
    private int runningJobs;

    // constructor for an XML server in ds-system.xml
	public Server(String t, int l, int bt, float hr, int c, int m, int d) {
		type = t;
		limit = l;
		bootupTime = bt;
		hourlyRate = hr;
		core = c;
		memory = m;
		disk = d;
	}

    // constructor for a server received from server
	public Server(String t, int i, String s, int cst, int c, int m, int d, int wj, int rj) {
		type = t;
		id = i;
		state = s;
		curStartTime = cst;
		core = c;
		memory = m;
		disk = d;
        waitingJobs = wj;
        runningJobs = rj;
	}

    // getter methods
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getLimit() {
        return limit;
    }

    public int getBootupTime() {
        return bootupTime;
    }

    public float getHourlyRate() {
        return hourlyRate;
    }

    public int getCore() {
        return core;
    }

    public int getMemory() {
        return memory;
    }

    public int getDisk() {
        return disk;
    }

    public String getState() {
        return state;
    }

    public int getCurStartTime() {
        return curStartTime;
    }

    public int getWaitingJobs() {
        return waitingJobs;
    }

    public int getRunningJobs() {
        return runningJobs;
    }
}
