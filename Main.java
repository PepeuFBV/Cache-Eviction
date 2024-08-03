import services.Cache;
import services.Logger;
import services.Service;
import services.database.AVLTree;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Service service = new Service();
        showOptions();
        Scanner scanner = new Scanner(System.in);
        System.out.print("-> ");
        int option = scanner.nextInt();
        while (option != 6) {
            switch (option) {
                case 1:
                    System.out.println("Creating a new Service Order...");
                    break;
                case 2:
                    System.out.println("Searching for a Service Order...");
                    break;
                case 3:
                    System.out.println("Listing all Service Orders...");
                    break;
                case 4:
                    System.out.println("Seeing the cache...");
                    break;
                case 5:
                    System.out.println("Clearing the log...");
                    break;
                case 6:
                    System.out.println("Exiting...");
                    service.stopServices();
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
            showOptions();
            System.out.print("-> ");
            option = scanner.nextInt();
        }
    }

    private static void showOptions() {
        System.out.println("[1] - Create a new Service Order");
        System.out.println("[2] - Search for a Service Order");
        System.out.println("[3] - List all Service Orders");
        System.out.println("[4] - See the cache");
        System.out.println("[5] - Clear the log");
        System.out.println("[6] - Exit");
    }

}
