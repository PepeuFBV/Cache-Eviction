import services.Service;
import java.util.Scanner;
import entities.OS;

public class Main {

    public static void main(String[] args) {
        Service service = new Service();
        //service.turnOffIncreaseCapacity();

        showOptions();
        createXOS(service, 200);
        Scanner scanner = new Scanner(System.in);
        System.out.print("-> ");
        int option = scanner.nextInt();
        while (option != 8) {
            switch (option) {
                case 1:
                    System.out.println("Creating a new Service Order...");
                    System.out.print("Name: ");
                    String name = scanner.next();
                    System.out.print("Description: ");
                    String description = scanner.next();
                    scanner.nextLine();  // clear the buffer
                    OS os = new OS(name, description, java.time.LocalDateTime.now());
                    try {
                        service.addNewServiceOrder(os);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("Service Order created\n");
                    break;
                case 2:
                    System.out.println("Searching for a Service Order...");
                    System.out.print("Enter the ID: ");
                    int id = scanner.nextInt();
                    try {
                        OS found = service.searchServiceOrder(id);
                        if (found != null) System.out.println(found.getName() + " - " + found.getDescription() + " - " + found.getSolicitationTime() + "\n");
                        else System.out.println("Service Order not found\n");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Listing all Service Orders...");
                    System.out.println(service.seeAllServiceOrders());
                    break;
                case 4:
                    System.out.println("Altering a Service Order...");
                    System.out.print("Enter the ID: ");
                    int idToAlter = scanner.nextInt();
                    OS found = service.searchServiceOrder(idToAlter);
                    if (found != null) {
                        System.out.println(found.getName() + " - " + found.getDescription() + " - " + found.getSolicitationTime());
                        System.out.println("Are you sure you want to alter this Service Order? (y/n)");
                        String answer = scanner.next();
                        if (answer.equals("y")) {
                            System.out.println("Insert the new data:");
                            System.out.print("Name: ");
                            String newName = scanner.next();
                            System.out.print("Description: ");
                            String newDescription = scanner.next();
                            scanner.nextLine();  // clear the buffer
                            OS newOS = new OS(newName, newDescription, java.time.LocalDateTime.now());
                            try {
                                service.alterServiceOrder(idToAlter, newOS);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                            System.out.println("Service Order altered\n");
                        } else {
                            System.out.println("Service Order not altered.\n");
                        }
                    } else {
                        System.out.println("Service Order not found\n");
                        break;
                    }
                    break;
                case 5:
                    System.out.println("Seeing the cache...");
                    System.out.println(service.seeCache());
                    break;
                case 6:
                    System.out.println("Removing a Service Order...");
                    System.out.print("Enter the ID: ");
                    int idToRemove = scanner.nextInt();
                    try {
                        service.removeServiceOrder(idToRemove);
                        System.out.println("Service Order removed\n");
                    } catch (Exception e) {
                        System.out.println(e.getMessage() + "\n");
                    }
                    break;
                case 7:
                    System.out.println("Clearing the log...");
                    try {
                        System.out.println(service.clearLog());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid option. Try again");
                    break;
            }
            showOptions();
            System.out.print("-> ");
            option = scanner.nextInt();
        }
        System.out.println("Exiting...");
        service.stopServices();
    }

    private static void showOptions() {
        System.out.println("[1] - Create a new Service Order");
        System.out.println("[2] - Search for a Service Order");
        System.out.println("[3] - List all Service Orders");
        System.out.println("[4] - Alter a Service Order");
        System.out.println("[5] - See the cache");
        System.out.println("[6] - Remove a Service Order");
        System.out.println("[7] - Clear the log");
        System.out.println("[8] - Exit");
    }

    private static void createXOS(Service service, int x) {
        for (int i = 0; i < x; i++) {
            OS os = new OS("OS" + i, "Description", java.time.LocalDateTime.now());
            try {
                service.addNewServiceOrder(os);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(x + " Service Orders created\n");
    }

}
