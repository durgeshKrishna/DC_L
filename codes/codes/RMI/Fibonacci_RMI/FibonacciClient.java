import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class FibonacciClient{
    public static void main(String[] args) {
        try {
           String serverIp="10.10.18.181";
            Registry registry = LocateRegistry.getRegistry(serverIp, 1099);
            Fibonacci fibonacci = (Fibonacci) registry.lookup("FibonacciService");

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("1. Get Fibonacci Number");
                System.out.println("2. Exit");
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter n to calculate Fibonacci(n): ");
                        int n = scanner.nextInt();
                        int result = fibonacci.getFibonacci(n);
                        System.out.println("Fibonacci(" + n + ") = " + result);
                        break;

                    case 2:
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } while (choice != 2);

            scanner.close();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
