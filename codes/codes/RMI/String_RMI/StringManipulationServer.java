import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class StringManipulationServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Start RMI registry
            StringManipulationServiceImpl service = new StringManipulationServiceImpl();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("StringManipulationService", service);
            System.out.println("String Manipulation Server is ready.");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Server started. Enter 'exit' to stop.");

            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
