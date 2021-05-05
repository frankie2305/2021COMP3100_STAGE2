public class Job {
    // all job attributes from server
    private int submitTime;
    private int id;
    private int estRuntime;
    private int core;
    private int memory;
    private int disk;

    // constructor for a job received from server
    public Job(int s, int i, int e, int c, int m, int d) {
        submitTime = s;
        id = i;
        estRuntime = e;
        core = c;
        memory = m;
        disk = d;
    }

    // getter methods
    public int getSubmitTime() {
        return submitTime;
    }

    public int getId() {
        return id;
    }

    public int getEstRuntime() {
        return estRuntime;
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
}
