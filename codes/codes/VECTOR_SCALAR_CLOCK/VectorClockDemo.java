import java.util.*;
class VectorClock {
 int[] clock;
 public VectorClock(int size) {
 clock = new int[size];
 Arrays.fill(clock, 0);
 }
 public void increment(int processId) {
 clock[processId] += 1;
 }
 public void merge(int[] receivedClock) {
 for (int i = 0; i < clock.length; i++) {
 clock[i] = Math.max(clock[i], receivedClock[i]);
 }
 }
 public void printClock(int processId) {
 System.out.println("Process P" + processId + " Vector Clock: " + Arrays.toString(clock));
 }
}
public class VectorClockDemo {
 public static void main(String[] args) {
 Scanner sc = new Scanner(System.in);
 System.out.print("Enter the number of processes: ");
 int n = sc.nextInt();
 VectorClock[] processes = new VectorClock[n];
 for (int i = 0; i < n; i++) {
 processes[i] = new VectorClock(n);
 }
 System.out.println("Initial Vectors\n");
 for(int i=0; i<n; i++){
 processes[i].increment(i);
 processes[i].printClock(i);
 }
 System.out.println("\nSimulating events...\n");
 System.out.print("Enter the number of messages to be passed: ");
 int messages = sc.nextInt();
 for (int i = 0; i < messages; i++) {
 System.out.print("Enter sender process: ");
 int sender = sc.nextInt();
 processes[sender].increment(sender);
 System.out.print("Enter receiver process: ");
 int receiver = sc.nextInt();
 if (sender >= 0 && sender < n && receiver >= 0 && receiver < n) {
 System.out.println("\nP" + sender + " sends a message to P" + receiver);
 processes[receiver].merge(processes[sender].clock);
 processes[receiver].increment(receiver); 
 processes[receiver].printClock(receiver);
 } else {
 System.out.println("Invalid process IDs. Please try again.");
 }
 }
 System.out.println("Final Vectors Vectors\n");
 for(int i=0; i<n; i++){
 processes[i].printClock(i);
 }
 sc.close();
 }
}
