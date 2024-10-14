package client;

import service.log.Logger;
import service.Service;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    private Logger logger;
    private Service service;

    // starts class and services
    public Client() throws RuntimeException {
        try {
            logger = new Logger(Logger.LogOrigin.CLIENT);
        } catch (RuntimeException e) {
            // todo: implement exception handling
        }
        try {
            service = new Service();
        } catch (RuntimeException e) {
            // todo: implement exception handling
        }
    }

    public void startServices() {
        try {
            int option = 0;
            while (option != 8) {
                option = getOption();
            }
        } catch (IOException e) {
            // todo: implement exception handling
        }
    }

    private int getOption() throws IOException {
        showOptions();
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        try {
            option = scanner.nextInt();
            if (option < 1 || option > 8) {
                throw new IOException("Invalid option");
            }
        } catch (Exception e) {
            throw new IOException("Invalid option");
        }
        return option; // Add this line
    }

    private void pickOption(int option) throws IOException { // Change method signature
        switch (option) { // Use the parameter instead of calling getOption
            case 1:
                System.out.println("Creating a new Service Order...");
                sendMessage("CREATE SERVICE ORDER");
                break;
            case 2:
                System.out.println("Searching for a Service Order...");
                sendMessage("SEARCH SERVICE ORDER");
                break;
            case 3:
                System.out.println("Listing all Service Orders...");
                sendMessage("SEE ALL SERVICE ORDERS");
                break;
            case 4:
                System.out.println("Altering a Service Order...");
                sendMessage("ALTER SERVICE ORDER");
                break;
            case 5:
                System.out.println("Seeing the cache...");
                sendMessage("SEE CACHE");
                break;
            case 6:
                System.out.println("Removing a Service Order...");
                sendMessage("REMOVE SERVICE ORDER");
                break;
            case 7:
                System.out.println("Clearing the log...");
                sendMessage("CLEAR LOG");
                break;
            default:
                System.out.println("Invalid option. Try again");
                throw new IOException("Invalid option");
        }
    }

    private void sendMessage(String message) {
        // todo: implement message sending, coding the message to be sent
    }

    private boolean receiveMessage(String message) {
        // todo: implement message receiving, decoding the message received
        return true;
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
