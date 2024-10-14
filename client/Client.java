package client;

import service.log.Logger;
import service.Service;

public class Client {

    private Logger logger;
    private Service service;

    public Client() throws RuntimeException {
        try {
            logger = new Logger(Logger.LogOrigin.CLIENT);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void startServices() {

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
