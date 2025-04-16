import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

class VectorClock {
    private int[] clock;
    
    public VectorClock(int numProcesses) {
        this.clock = new int[numProcesses];
        Arrays.fill(this.clock, 0);
    }
    
    public void tick(int processIndex) {
        clock[processIndex]++; // Internal Event
    }
    
    public void sendEvent(int processIndex) {
        clock[processIndex]++; // Sending message increments sender's clock
    }
    
    public void receiveEvent(int processIndex, int[] receivedClock) {
        for (int i = 0; i < clock.length; i++) {
            clock[i] = Math.max(clock[i], receivedClock[i]); // Take max of both clocks
        }
        clock[processIndex]++; // Increment receiver's clock after receiving
    }
    
    public int[] getTime() {
        return clock.clone();
    }
}

public class VectorClockSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();
        
        Map<Integer, VectorClock> processes = new HashMap<>();
        for (int i = 0; i < numProcesses; i++) {
            processes.put(i, new VectorClock(numProcesses));
        }
        
        while (true) {
            System.out.print("\nEnter process number (p_i) to perform an event or -1 to exit: ");
            int processNum = scanner.nextInt();
            
            if (processNum == -1) {
                System.out.println("Exiting...");
                break;
            }
            
            int processIndex = processNum - 1; // Adjust for zero-based index
            
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
                processes.get(processIndex).tick(processIndex);
                System.out.println("Process " + processNum + " performed an internal event. New Clock: " + Arrays.toString(processes.get(processIndex).getTime()));
            } else if (choice == 2) {
                System.out.print("Enter receiver process number: ");
                int receiver = scanner.nextInt();
                
                int receiverIndex = receiver - 1; // Adjust for zero-based index
                
                if (processes.containsKey(receiverIndex)) {
                    processes.get(processIndex).sendEvent(processIndex);
                    int[] senderClock = processes.get(processIndex).getTime(); // Get updated sender's clock
                    processes.get(receiverIndex).receiveEvent(receiverIndex, senderClock); // Receiver updates its clock
                    System.out.println("Process " + processNum + " sent a message to Process " + receiver);
                } else {
                    System.out.println("Invalid receiver process number.");
                }
            } else if (choice == 3) {
                System.out.print("Enter sender process number: ");
                int sender = scanner.nextInt();
                
                int senderIndex = sender - 1; // Adjust for zero-based index
                
                if (processes.containsKey(senderIndex)) {
                    int[] receivedTime = processes.get(senderIndex).getTime();
                    processes.get(processIndex).receiveEvent(processIndex, receivedTime);
                    System.out.println("Process " + processNum + " received a message from Process " + sender + ". Updated Clock: " + Arrays.toString(processes.get(processIndex).getTime()));
                } else {
                    System.out.println("Invalid sender process number.");
                }
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        
        System.out.println("\nFinal Vector Clocks for All Processes:");
        System.out.println("-----------------------------------");
        System.out.println("Process | Vector Clock");
        System.out.println("-----------------------------------");
        for (int i = 0; i < numProcesses; i++) {
            System.out.println("   P" + (i + 1) + "    |     " + Arrays.toString(processes.get(i).getTime()));
        }
        
        scanner.close();
    }
}
