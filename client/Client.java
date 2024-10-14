package client;

import compression.Compressor;
import service.Service;
import service.log.Logger;
import java.util.Scanner;

public class Client {

    private Logger logger;
    private Service service;
    private Compressor compressor;

    public Client(Service service) throws RuntimeException {
        try {
            logger = new Logger(Logger.LogOrigin.CLIENT);
            compressor = new Compressor(); // inicializar compressor
            this.service = service;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            // todo: exception handling
        }
    }

    public void startServices() {
        boolean exit = false;
        while (!exit) {
            int option = 0;
            while (option != 8) {
                option = getOption();
                String message = pickOption(option);
                sendMessage(message);
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
        String compressedMessage = compressor.compress(message);
        service.receiveMessage(compressedMessage);
        System.out.println("Client sent compressed message: " + compressedMessage);
    }

    private int getOption() {
        showOptions();
        System.out.print("--> ");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        try {
            option = scanner.nextInt();
            if (option < 1 || option > 8) {
                System.out.println("Invalid option. Try again\n");
            }
            return option;
        } catch (Exception e) {
            System.out.println("Invalid option. Try again\n");
        }
        return option;
    }

    private String pickOption(int option) {
        return switch (option) {
            case 1 -> {
                System.out.println("Creating a new Service Order...");
                yield "CREATE SERVICE ORDER";
            }
            case 2 -> {
                System.out.println("Searching for a Service Order...");
                yield "SEARCH SERVICE ORDER";
            }
            case 3 -> {
                System.out.println("Listing all Service Orders...");
                yield "LIST SERVICE ORDERS";
            }
            case 4 -> {
                System.out.println("Altering a Service Order...");
                yield "ALTER SERVICE ORDER";
            }
            case 5 -> {
                System.out.println("Seeing the cache...");
                yield "SEE CACHE";
            }
            case 6 -> {
                System.out.println("Removing a Service Order...");
                yield "REMOVE SERVICE ORDER";
            }
            case 7 -> {
                System.out.println("Clearing the log...");
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
