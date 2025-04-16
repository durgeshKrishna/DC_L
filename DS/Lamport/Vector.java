import java.util.*;
class VectorClock{
    int[] clock;
    VectorClock(int n){
        this.clock = new int[n];
    }
    void increment(int inc){
        clock[inc]++;
    }
    void merge(int[] receivedClock){
        for(int i=0;i<receivedClock.length;i++){
            clock[i] = Math.max(clock[i],receivedClock[i]);
        }
    }
    void print(int p){
        System.out.println("P"+p+": "+Arrays.toString(clock));
    }
}
class Vector{
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.print("No.of.Process: ");
        int n = in.nextInt();
        VectorClock[] process = new VectorClock[n];
        for(int p=0;p<n;p++){
            process[p] = new VectorClock(n);
            process[p].increment(p);
            process[p].print(p);
        }
        System.out.print("No.of mgs: "); 
        int msg = in.nextInt();
        for(int k=0;k<msg;k++){
            System.out.print("Enter the Sender and reciever Process: ");
            int sender = in.nextInt(), reciever = in.nextInt();
            process[sender].increment(sender);
            process[reciever].merge(process[sender].clock);
            process[reciever].increment(reciever);
        }
        for(int i=0;i<n;i++){
            process[i].print(i);
        }
    }
}