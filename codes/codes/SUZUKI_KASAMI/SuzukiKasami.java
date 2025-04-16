import java.util.*;

public class SuzukiKasami{
    static int numProcesses;
    static int tokenHolder = 0;
    static int[] RN;
    static int[] LN;
    static List<Integer> tokenQueue = new ArrayList<>();
    static Map<Integer, Integer> executionTicks = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        numProcesses = scanner.nextInt();

        RN = new int[numProcesses];
        LN = new int[numProcesses];
        for (int i = 0; i < numProcesses; i++) {
            RN[i] = 0;
            LN[i] = 0;
        }

        for (int i = 0; i < numProcesses; i++) {
            System.out.print("Enter execution ticks for Process " + i + ": ");
            executionTicks.put(i, scanner.nextInt());
        }

        System.out.println("Process 0 starts with the token.");
        printState();
        scanner.nextLine();

        while (true) {
            System.out.print("Enter process IDs that want to enter CS (space-separated, -1 to exit): ");
            String input = scanner.nextLine();
            if (input.equals("-1")) {
                System.out.println("Exit");
                break;
            }

            try {
                String[] processIDs = input.split("\\s+");
                List<Integer> newRequests = new ArrayList<>();
                for (String idStr : processIDs) {
                    int processID = Integer.parseInt(idStr);
                    if (processID >= 0 && processID < numProcesses) {
                        newRequests.add(processID);
                    } else {
                        System.out.println("Invalid process ID: " + processID);
                    }
                }
                handleRequests(newRequests);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + input);
            }
        }

        scanner.close();
    }

    public static void handleRequests(List<Integer> processes) {
        for (int processID : processes) {
            RN[processID]++;
            System.out.println("Process " + processID + " requests CS (Request Number: " + RN[processID] + ")");
            if (!tokenQueue.contains(processID)) {
                tokenQueue.add(processID);
            }
        }
        printState();

        while (!tokenQueue.isEmpty()) {
            int nextProcess = tokenQueue.get(0);
            if (tokenHolder == nextProcess) {
                executeCS(nextProcess);
            } else {
                System.out.println("Checking if Process " + tokenHolder + " should pass the token...");
                if (RN[nextProcess] > LN[nextProcess]) {
                    passToken(nextProcess);
                } else {
                    System.out.println("Process " + nextProcess + " doesn't need token anymore, removing from queue.");
                    tokenQueue.remove(0);
                }
            }
        }
    }

    public static void passToken(int toProcess) {
        System.out.println("Token is being PASSED from Process " + tokenHolder + " to Process " + toProcess);
        tokenHolder = toProcess;
        tokenQueue.remove(Integer.valueOf(toProcess));
        printState();
        executeCS(toProcess);
    }

    public static void executeCS(int processID) {
        System.out.println(">>> Process " + processID + " ENTERING CS for " + executionTicks.get(processID) + " ticks.");
        for (int i = 1; i <= executionTicks.get(processID); i++) {
            System.out.println("Tick " + i + "...");
        }
        System.out.println("<<< Process " + processID + " EXITING CS.");
        LN[processID] = RN[processID];
        printState();
        checkWaitingProcesses();
    }

    public static void checkWaitingProcesses() {
        while (!tokenQueue.isEmpty()) {
            int nextProcess = tokenQueue.get(0);
            if (RN[nextProcess] > LN[nextProcess]) {
                passToken(nextProcess);
                return;
            } else {
                System.out.println("Process " + nextProcess + " is waiting but doesn't need token anymore.");
                tokenQueue.remove(0);
            }
        }
        System.out.println("No processes waiting for token.");
    }

    public static void printState() {
        System.out.println("-- Current State --");
        System.out.println("RN: " + Arrays.toString(RN));
        System.out.println("LN: " + Arrays.toString(LN));
        System.out.println("Token Queue: " + tokenQueue);
        System.out.println("Token is with Process: " + tokenHolder);
        System.out.println("-------------------");
    }
}
