import java.util.*;
class Process extends Thread {
    int processId;
    int timestamp;
    boolean requestingCS;
    int repliesReceived;
    List<Integer> deferredQueue;
    int executionTicks;
    static int logicalClock = 0;
    static int numProcesses;
    static Process[] processes;
    public Process(int id, int executionTicks) {
        this.processId = id;
        this.timestamp = 0;
        this.requestingCS = false;
        this.repliesReceived = 0;
        this.executionTicks = executionTicks;
        this.deferredQueue = new ArrayList<>();
    }
    synchronized static void updateClock(int receivedTimestamp) {
        logicalClock = Math.max(logicalClock, receivedTimestamp) + 1;
    }
    synchronized void printDeferredQueue() {
        System.out.print("Deferred Queue for Process " + processId + ": ");
        System.out.println(deferredQueue.isEmpty() ? "Empty" : deferredQueue);
    }
    boolean canEnterCS() {
        return repliesReceived == numProcesses - 1;
    }
    void sendRequest() {
        synchronized (this) {
            this.timestamp = ++logicalClock;
            this.requestingCS = true;
            this.repliesReceived = 0;
            System.out.println("\nProcess " + processId + " requests CS at timestamp " + timestamp);
            for (Process p : processes) {
                if (p.processId != this.processId) {
                    System.out.println("Process " + p.processId + " received REQUEST from Process " + processId);
                    if (!p.requestingCS || (this.timestamp < p.timestamp) || 
                        (this.timestamp == p.timestamp && this.processId < p.processId)) {
                        System.out.println("Process " + p.processId + " grants REPLY to Process " + processId);
                        repliesReceived++;
                    } else {
                        System.out.println("Process " + p.processId + " defers REPLY to Process " + processId);
                        p.deferredQueue.add(processId);
                        p.printDeferredQueue();
                    }}}}}
    void executeCS() {
        while (!canEnterCS()) {
            try { Thread.sleep(50); } catch (InterruptedException e) {}
        }
        synchronized (this) {
            System.out.println("\nProcess " + processId + " is executing in CS for " + executionTicks + " ticks.");
            try { Thread.sleep(executionTicks * 100); } catch (InterruptedException e) {}
            System.out.println("Process " + processId + " exits CS.");
            synchronized (this) {
                if (!deferredQueue.isEmpty()) {
                    System.out.println("Process " + processId + " granting replies to deferred queue: " + deferredQueue);
                }
                while (!deferredQueue.isEmpty()) {
                    int deferredProcess = deferredQueue.remove(0);
                    System.out.println("Process " + processId + " now grants REPLY to Process " + deferredProcess);
                    processes[deferredProcess].repliesReceived++;
                }
                printDeferredQueue();
            }
            requestingCS = false;
        }}
    @Override
    public void run() {
        sendRequest();
        executeCS();
    }
    static void initializeProcesses() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        numProcesses = sc.nextInt();
        if (numProcesses > 10) {
            System.out.println("Error: Maximum number of processes is 10");
            System.exit(1);
        }
        processes = new Process[numProcesses];
        for (int i = 0; i < numProcesses; i++) {
            System.out.print("Enter execution ticks for Process " + i + ": ");
            int ticks = sc.nextInt();
            processes[i] = new Process(i, ticks);
        }}
    public static void main(String[] args) {
        initializeProcesses();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("\nEnter process IDs that want to enter CS (-1 to exit): ");
            String input = sc.nextLine();
            if (input.contains("-1")) {
                System.out.println("Exiting CS request loop.");
                break;
            }
            String[] tokens = input.split(" ");
            List<Thread> threads = new ArrayList<>();
            for (String token : tokens) {
                try {
                    int processId = Integer.parseInt(token);
                    if (processId >= 0 && processId < numProcesses) {
                        Thread t = new Thread(processes[processId]);
                        threads.add(t);
                        t.start();
                    } else {
                        System.out.println("Invalid process ID: " + token);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: " + token);
                }}
            for (Thread t : threads) {
                try { t.join(); } catch (InterruptedException e) {}
            }
        }
    }
}
