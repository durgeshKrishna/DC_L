import java.util.*;
class Process{
    private int privateLabel;
    int publicLabel;
    int id;
    List<Process> waitingFor;

    public Process(int id) {
        this.id = id;
        this.privateLabel = id;
        this.publicLabel = id;
        this.waitingFor = new ArrayList<>();
    }

    public void block(Process other) {
        waitingFor.add(other);
    }

    public void activate(Process other) {
        waitingFor.remove(other);
    }

    public void transmit() {
        for (Process p : waitingFor) {
            if (p.publicLabel < this.publicLabel) {
                p.publicLabel = this.publicLabel + 1;
                p.transmit();
            }
        }
    }

    public boolean detect() {
        Set<Integer> visited = new HashSet<>();
        return detectCycle(this, visited);
    }

    private boolean detectCycle(Process current, Set<Integer> visited) {
        if (visited.contains(current.id)) {
            return true; // Cycle detected
        }
        visited.add(current.id);
        for (Process p : current.waitingFor) {
            if (detectCycle(p, new HashSet<>(visited))) {
                return true;
            }
        }
        return false;
    }
}

public class MitchellMerrittAlgorithm {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int numProcesses = scanner.nextInt();
        Process[] processes = new Process[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            processes[i] = new Process(i + 1); // IDs start from 1
        }

        System.out.print("Enter number of blocking relationships: ");
        int numBlocks = scanner.nextInt();
        System.out.println("Enter blocking pairs (e.g., '1 2' means P1 blocks P2):");
        for (int i = 0; i < numBlocks; i++) {
            int p1 = scanner.nextInt();
            int p2 = scanner.nextInt();
            processes[p1 - 1].block(processes[p2 - 1]);
        }

        System.out.print("Enter number of processes to call transmit: ");
        int numTransmits = scanner.nextInt();
        System.out.println("Enter process IDs that should call transmit:");
        for (int i = 0; i < numTransmits; i++) {
            int pId = scanner.nextInt();
            processes[pId - 1].transmit();
        }

        boolean deadlockDetected = false;
        for (Process p : processes) {
            if (p.detect()) {
                deadlockDetected = true;
                break;
            }
        }

        if (deadlockDetected) {
            System.out.println("Deadlock detected!");
        } else {
            System.out.println("No deadlock detected.");
        }

        scanner.close();
    }
}
