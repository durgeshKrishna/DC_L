
import java.util.Scanner;

public class Ricart_Agarwala {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        System.out.println("Enter the number of the sites:");
        int ns=in.nextInt();
        System.err.println("Enter number of sites which want to enter critical section: ");
        int ncs=in.nextInt();
        int[] ts=new int[ns];
        int[] request=new int[ncs];
        int[] mp_timestamp=new int[ncs];
        int[] mp_site=new int[ncs];
        int[] deffered=new int[ns];
        for(int i=0;i<ns;i++){
            System.err.println("Enter timestamp for site \" + (i + 1) + \": ");
            ts[i]=in.nextInt();
            deffered[i]=0;
        }
        for(int i=0;i<ncs;i++){
            System.err.println("Enter site number that wants to enter CS: ");
            int site=in.nextInt();
            request[i]=site;
            mp_timestamp[i]=ts[site-1];
            mp_site[i]=site;
        }
        System.out.println("\nSites and Timestamps:");
        for (int i = 0; i < ns; i++) {
            System.out.println("Site " + (i + 1) + " Timestamp: " + ts[i]);
        }
        for(int i=0;i<ncs;i++){
            System.out.println("\nRequest from site: " + request[i]);
            for(int j=0;j<ns;j++){
                if(request[i]!=(j+1)){
                    if(ts[j]>ts[request[i]-1]|| ts[j]==0){
                        System.out.println("Site " + (j + 1) + " Replied");
                    }
                    else {
                        System.out.println("Site " + (j + 1) + " Deferred");
                        deffered[j] = request[i];
                    }
                }
            }
        }
        for (int i = 0; i < ncs - 1; i++) {
            for (int j = 0; j < ncs - i - 1; j++) {
                if (mp_timestamp[j] > mp_timestamp[j + 1]) {
                    int temp_time = mp_timestamp[j];
                    mp_timestamp[j] = mp_timestamp[j + 1];
                    mp_timestamp[j + 1] = temp_time;

                    int temp_site = mp_site[j];
                    mp_site[j] = mp_site[j + 1];
                    mp_site[j + 1] = temp_site;
                }
            }
        }
        System.err.println("\nSites entered Critical Section:");
        for(int i=0;i<ncs;i++){
            System.out.println("Site " + mp_site[i] + " entered Critical Section");
            if(deffered[mp_site[i]-1]!=0){
                System.out.println("Site " + mp_site[i] + " then checks its deferred array and replies to Site " +
                        deffered[mp_site[i] - 1]);
                        deffered[mp_site[i] - 1]=0;
            }
        }
    }
}
