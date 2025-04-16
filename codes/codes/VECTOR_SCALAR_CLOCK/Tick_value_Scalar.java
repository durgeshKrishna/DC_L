import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class LamportClock {
    private int clock;
    private int tickValue;

    public LamportClock(int tickValue) {
        this.clock = 0; // Initialize clock to zero
        this.tickValue = tickValue;
    }

    public void tick() {
        clock += tickValue; // Increment based on tick value
    }

    public int sendEvent() {
        clock += tickValue; // Sending a message increments the clock based on tick value
        return clock;
    }

    public void receiveEvent(int receivedTime) {
        if (receivedTime > clock) { // Conflict detected
            clock = Math.max(clock, receivedTime) + 1;
            updateFutureEvents(clock);
        } else {
            clock += tickValue;
        }
    }

    private void updateFutureEvents(int newClock) {
        clock = newClock; // Update clock with new value
    }

    public int getTime() {
        return clock;
    }
}

public class Tick_value_Scalar {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();
        
        Map<Integer, LamportClock> processes = new HashMap<>();
        
        for (int i = 0; i < numProcesses; i++) {
            System.out.print("Enter tick value for Process " + (i + 1) + ": ");
            int tickValue = scanner.nextInt();
            processes.put(i, new LamportClock(tickValue));
        }
        
        while (true) {
            System.out.print("\nEnter process number (p_i) to perform an event or -1 to exit: ");
            int processNum = scanner.nextInt();
            
            if (processNum == -1) {
                System.out.println("Exiting...");
                break;
            }
            
            int processIndex = processNum - 1;
            
            if (!processes.containsKey(processIndex)) {
                System.out.println("Invalid process number. Try again.");
                continue;
            }
            
            System.out.println("Choose an event for Process " + processNum + ":");
            System.out.println("1. Internal Event");
            System.out.println("2. Send Message");
            System.out.println("3. Receive Message");
            int choice = scanner.nextInt();
            
            if (choice == 1) {
                processes.get(processIndex).tick();
                System.out.println("Process " + processNum + " performed an internal event. New Clock: " + processes.get(processIndex).getTime());
            } else if (choice == 2) {
                System.out.print("Enter receiver process number: ");
                int receiver = scanner.nextInt();
                
                int receiverIndex = receiver - 1;
                
                if (processes.containsKey(receiverIndex)) {
                    int sentTime = processes.get(processIndex).sendEvent();
                    processes.get(receiverIndex).receiveEvent(sentTime);
                    System.out.println("Process " + processNum + " sent a message to Process " + receiver);
                } else {
                    System.out.println("Invalid receiver process number.");
                }
            } else if (choice == 3) {
                System.out.print("Enter sender process number: ");
                int sender = scanner.nextInt();
                
                int senderIndex = sender - 1;
                
                if (processes.containsKey(senderIndex)) {
                    int receivedTime = processes.get(senderIndex).getTime();
                    processes.get(processIndex).receiveEvent(receivedTime);
                    System.out.println("Process " + processNum + " received a message from Process " + sender + ". Updated Clock: " + processes.get(processIndex).getTime());
                } else {
                    System.out.println("Invalid sender process number.");
                }
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        
        System.out.println("\nFinal Lamport Clocks for All Processes:");
        System.out.println("-----------------------------------");
        System.out.println("Process | Lamport Clock");
        System.out.println("-----------------------------------");
        for (int i = 0; i < numProcesses; i++) {
            System.out.println("   P" + (i + 1) + "    |     " + processes.get(i).getTime());
        }
        
        scanner.close();
    }
}
