import java.util.*;
class RicartAgrawala {
    private int processId;
    private int totalProcesses;
    private boolean isInCriticalSection = false;
    private int timestamp = 0;
    private Set<Integer> repliesReceived = new HashSet<>();
    private PriorityQueue<Request> requestQueue = new PriorityQueue<>(Comparator.comparingInt(r -> r.timestamp));
    private static class Request {
        int processId;
        int timestamp;
        Request(int processId, int timestamp) {
            this.processId = processId;
            this.timestamp = timestamp;
        }
    }
    public RicartAgrawala(int processId, int totalProcesses) {
        this.processId = processId;
        this.totalProcesses = totalProcesses;
    }
    public void requestCriticalSection() {
        timestamp++;
        System.out.println("Process " + processId + " requesting CS with timestamp " + timestamp);
        requestQueue.add(new Request(processId, timestamp));
        for (int i = 0; i < totalProcesses; i++) {
            if (i != processId) {
                sendMessage(i, "REQUEST " + processId + " " + timestamp);
            }
        }
        while (repliesReceived.size() < totalProcesses - 1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        enterCriticalSection();
    }
    private void enterCriticalSection() {
        isInCriticalSection = true;
        System.out.println("Process " + processId + " ENTERED critical section.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exitCriticalSection();
    }
    private void exitCriticalSection() {
        isInCriticalSection = false;
        System.out.println("Process " + processId + " EXITED critical section.");

        while (!requestQueue.isEmpty()) {
            Request request = requestQueue.poll();
            if (request.processId != processId) {
                sendMessage(request.processId, "REPLY " + processId);
            }
        }
        repliesReceived.clear();
    }
    private void handleIncomingMessage(String message) {
        String[] parts = message.split(" ");
        String messageType = parts[0];
        int senderId = Integer.parseInt(parts[1]);

        if (messageType.equals("REQUEST")) {
            int senderTimestamp = Integer.parseInt(parts[2]);
            requestQueue.add(new Request(senderId, senderTimestamp));

            if (!isInCriticalSection || (senderTimestamp < timestamp)) {
                sendMessage(senderId, "REPLY " + processId);
            }
        } else if (messageType.equals("REPLY")) {
            repliesReceived.add(senderId);
        }
    }
    private void sendMessage(int receiverId, String message) {
        System.out.println("Process " + processId + " sending message to " + receiverId + ": " + message);
    }
}
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter total number of processes: ");
        int totalProcesses = scanner.nextInt();
        List<RicartAgrawala> processes = new ArrayList<>();
        for (int i = 0; i < totalProcesses; i++) {
            processes.add(new RicartAgrawala(i, totalProcesses));
        }
        while (true) {
            System.out.print("Enter process ID to request CS (0 to " + (totalProcesses - 1) + ", -1 to exit): ");
            int processId = scanner.nextInt();
            if (processId == -1) break;

            if (processId >= 0 && processId < totalProcesses) {
                new Thread(() -> processes.get(processId).requestCriticalSection()).start();
            } else {
                System.out.println("Invalid process ID. Try again.");
            }
        }
        scanner.close();
    }
}
