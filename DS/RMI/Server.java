import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args)throws Exception {
        TicketImpl t = new TicketImpl();
        Registry registry = LocateRegistry.createRegistry(8089);
        registry.bind("TicketService", t);
        System.out.println("Server is running.........");
    }
}
