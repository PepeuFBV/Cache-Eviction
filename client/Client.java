package client;

import compression.Compressor;
import service.Service;
import service.log.Logger;
import java.util.Scanner;

public class Client {

    private Logger logger;
    private Service service;
    private Compressor compressor;

    public Client(Service service, Compressor compressor) throws RuntimeException {
        try {
            logger = new Logger(Logger.LogOrigin.CLIENT);
            this.compressor = compressor;
            this.service = service;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            // todo: exception handling
        }
    }

    public void startServices() {
        boolean exit = false;
        while (!exit) {
            int option = 1;
            while (true) {
                option = getOption();
                try {
                    String message = pickOption(option);
                    sendMessage(message);
                } catch (Exception e) {
                    break;
                }
            }
            System.out.println("Are you sure you want to exit? (y/n)");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            if (answer.equals("y")) {
                exit = true;
            }
        }
    }

    private void sendMessage(String message) {
        System.out.println("\n" + service.receiveMessage(compressor.compress(message)) + "\n");
    }

    private int getOption() {
        showOptions();
        System.out.print("--> ");
        Scanner scanner = new Scanner(System.in);
        int option = 1;
        option = scanner.nextInt();
        return option;
    }

    private String pickOption(int option) {
        return switch (option) {
            case 1 -> {
                logger.log("Creating a new Service Order");
                System.out.println("\nEnter the data for the new Service Order");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Description: ");
                String description = scanner.nextLine();
                yield "CREATE " + name + "," + description;
            }
            case 2 -> {
                logger.log("Searching for a Service Order");
                System.out.println("Enter the ID of the Service Order");
                Scanner scanner = new Scanner(System.in);
                System.out.print("ID: ");
                int id = scanner.nextInt();
                yield "SEARCH " + id;
            }
            case 3 -> {
                logger.log("Listing all Service Orders");
                yield "LIST DATABASE";
            }
            case 4 -> {
                logger.log("Altering a Service Order");
                System.out.println("Enter the ID of the Service Order");
                Scanner scanner = new Scanner(System.in);
                System.out.print("ID: ");
                int id = scanner.nextInt();
                scanner.nextLine(); // consume the newline
                System.out.println("Enter the new data for the Service Order");
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Description: ");
                String description = scanner.nextLine();
                yield "ALTER " + id + "," + name + "," + description;
            }
            case 5 -> {
                logger.log("Seeing the cache");
                yield "LIST CACHE";
            }
            case 6 -> {
                logger.log("Removing a Service Order");
                System.out.println("Enter the ID of the Service Order");
                Scanner scanner = new Scanner(System.in);
                System.out.print("ID: ");
                int id = scanner.nextInt();
                yield "REMOVE " + id;
            }
            case 7 -> {
                logger.log("Clearing the log");
                yield "CLEAR LOG";
            }
            default -> throw new IllegalStateException("Unexpected value: " + option);
        };
    }

    private void showOptions() {
        System.out.println("""
                [1] - Create a new Service Order
                [2] - Search for a Service Order
                [3] - List all Service Orders
                [4] - Alter a Service Order
                [5] - See the cache
                [6] - Remove a Service Order
                [7] - Clear the log
                [8] - Exit""");
    }

}
