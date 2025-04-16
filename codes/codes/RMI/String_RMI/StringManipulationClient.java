import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class StringManipulationClient {
    public static void main(String[] args) {
        try {
            String serverIP = "192.168.56.1";
            Registry registry = LocateRegistry.getRegistry(serverIP, 1099);
            StringManipulationService service = (StringManipulationService) registry.lookup("StringManipulationService");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Choose an operation:");
                System.out.println("1. To Uppercase");
                System.out.println("2. To Lowercase");
                System.out.println("3. Reverse");
                System.out.println("4. Count Words");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (choice == 0) {
                    break;
                }

                System.out.print("Enter a string: ");
                String inputString = scanner.nextLine();

                String result = "";

                switch (choice) {
                    case 1:
                        result = service.toUpperCase(inputString);
                        break;
                    case 2:
                        result = service.toLowerCase(inputString);
                        break;
                    case 3:
                        result = service.reverseString(inputString);
                        break;
                    case 4:
                        result = "Word Count: " + service.countWords(inputString);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        continue;
                }

                System.out.println("Result: " + result);
                System.out.println();
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
