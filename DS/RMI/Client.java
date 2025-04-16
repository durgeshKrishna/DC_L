import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.Arrays;

public class Client {
    public static void main(String[] args)throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 8089);
        Ticket t = (Ticket)registry.lookup("TicketService");
        Scanner in = new Scanner(System.in);
        System.out.println("List of Movies: \n"+Arrays.toString(t.moveList()));
        System.out.print("Movie Name: ");
        String movie = in.next();
        System.out.print("Seat No.: ");
        int seat = in.nextInt();
        System.out.println(t.Bookmovie(movie,seat));
    }
}
