package service;

import compression.Compressor;
import entities.OS;
import exceptions.DuplicateEntryException;
import exceptions.NonExistentEntryException;
import service.cache.Cache;
import service.database.HashTable;
import service.log.Logger;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

// todo: make flag for logging whole database or cache content at every operation
// todo: test all methods

public class Service {

    private final Cache cache;
    private final Logger logger;
    private final HashTable hashTable;
    private final Compressor compressor;
    private HashMap<String, String> dictionary;

    public Service(Compressor compressor) {
        try {
            logger = new Logger(Logger.LogOrigin.SERVICE);
            cache = new Cache();
            hashTable = new HashTable();
            this.compressor = compressor;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String receiveMessage(String compressedMessage) throws RuntimeException {
        String message = compressor.decompress(compressedMessage);
        String[] parts = message.split(" "); // message format:
        String command = parts[0];
        logger.log("Received message: " + message);

        try {
            switch (command) {
                case "CREATE":
                    String[] creating = parts[1].split(",");
                    OS newServiceOrder = new OS(creating[0], creating[1], LocalDateTime.now()); // Adjust as necessary
                    addNewServiceOrder(newServiceOrder);
                    return "Service Order with ID " + newServiceOrder.getId() + " created";
                case "SEARCH":
                    int searchId = Integer.parseInt(parts[1]);
                    OS foundOrder = searchServiceOrder(searchId);
                    if (foundOrder != null) {
                        logger.log("Service Order found: " + foundOrder.toString());
                        return "Found Service Order: " + foundOrder.toString();
                    } else {
                        logger.log("Service Order not found");
                        return "Service Order not found";
                    }
                case "ALTER":
                    String[] altering = parts[1].split(",");
                    OS alteredOrder = new OS(altering[1], altering[2], LocalDateTime.now()); // Adjust as necessary
                    alterServiceOrder(Integer.parseInt(altering[1]), alteredOrder);
                    return "Service Order with ID " + altering[0] + " altered";
                case "LIST":
                    if (parts[1].equals("CACHE")) {
                        return seeCache();
                    } else if (parts[1].equals("DATABASE")) {
                        return seeAllServiceOrders();
                    } else {
                        throw new RuntimeException("Invalid command");
                    }
                case "REMOVE":
                    int removeId = Integer.parseInt(parts[1]);
                    return removeServiceOrder(removeId);
                case "CLEAR":
                    if (parts[1].equals("LOG")) {
                        return clearLog();
                    } else {
                        throw new RuntimeException("Invalid command");
                    }
                default:
                    throw new IllegalStateException("Unexpected message: " + command);
            }
        } catch (DuplicateEntryException e) {
            logger.log("Duplicate Entry exception for:" + message);
            return "Duplicate Service Order for: " + message;
        } catch (RuntimeException e) {
            logger.log("Runtime Exception for: " + message);
            return "Runtime Exception for: " + message;
        } catch (NonExistentEntryException e) {
            logger.log("NonExistentEntry exception for: " + message + " - " + e.getMessage());
            return "NonExistent Service Order for: " + message + " - " + e.getMessage();
        }
    }

    private void turnOffDatabaseIncreaseCapacity() {
        hashTable.setMayIncreaseCapacity(false);
    }

    private void turnOnDatabaseIncreaseCapacity() {
        hashTable.setMayIncreaseCapacity(true);
    }

    private void stopServices() {
        logger.log("Stopping services at " + showDateAndTime());
    }

    // client calling this method
    private void addNewServiceOrder(OS os) throws DuplicateEntryException {
        logger.log("Creating new Service Order");
        if (isInCache(os)) { // check if service order already exists in cache
            logger.log("Service Order already exists in cache");
            throw new DuplicateEntryException("Service Order already exists in cache");
        } else if (isInDatabase(os)) { // check if service order already exists in database
            logger.log("Service Order already exists in database");
            throw new DuplicateEntryException("Service Order already exists in database");
        } else { // service order doesn't exist in cache or database
            hashTable.add(os);
            logger.log("Service Order added to database");
            cache.add(os);
            logger.log("Service Order added to cache");
        }
    }

    // client calling this method
    private String clearLog() throws RuntimeException {
        try {
            logger.clearLog();
            return "Log cleared";
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear log");
        }
    }

    // client calling this method
    private String seeAllServiceOrders() throws RuntimeException {
        logger.log("Listing all Service Orders");
        if (cache.isEmpty() || hashTable.isEmpty()) { // checks for faster answer if cache is empty
            if (hashTable.isEmpty()) {
                hashTable.log("Database is empty");
                return "Database is empty";
            }
        }
        logContent(Logger.LogOrigin.DATABASE);
        return getContent(Logger.LogOrigin.DATABASE);
    }

    // client calling this method
    private String seeCache() {
        logger.log("Listing all Service Orders in the cache");
        if (cache.isEmpty()) {
            cache.log("Cache is empty");
            return "Cache is empty";
        }
        logContent(Logger.LogOrigin.CACHE);
        return getContent(Logger.LogOrigin.CACHE);
    }

    // client calling this method
    private OS searchServiceOrder(int id) {
        logger.log("Searching for Service Order with ID " + id);
        if (hashTable.isEmpty()) {
            hashTable.log("Database is empty, can't search for Service Order");
        } else {
            OS os = cache.search(id);
            if (os != null) { // OS exists in the cache
                return os;
            } else { // OS is not in the cache
                os = hashTable.search(id);
                if (os != null) {
                    cache.add(os); // adds to cache
                    return os;
                }
            }
            logger.log("Service Order not found in database");
        }
        return null;
    }

    // client calling this method
    private String removeServiceOrder(int id) throws NonExistentEntryException {
        if (hashTable.isEmpty()) {
            logger.log("Database is empty, can't remove Service Order");
            throw new NonExistentEntryException("Database is empty, can't remove Service Order");
        } else {
            logger.log("Removing Service Order with ID " + id);
            try {
                cache.remove(id);
                hashTable.remove(id);
                logger.log("Service Order ID " + id + " removed from cache and database");
                return "Service Order ID " + id + " removed from cache and database";
            } catch (NonExistentEntryException e) {
                cache.log("Service Order ID " + id + " not found in cache");
                try {
                    hashTable.remove(id);
                    logger.log("Service Order ID " + id + " removed from database");
                    return "Service Order ID " + id + " removed from database";
                } catch (NonExistentEntryException e2) {
                    hashTable.log("Service Order ID " + id + " not found in database");
                    throw new NonExistentEntryException("Service Order ID " + id + " not found in database");
                }
            }
        }
    }

    // client calling this method
    private void alterServiceOrder(int id, OS newServiceOrder) throws NonExistentEntryException {
        logger.log("Altering Service Order with ID " + id);

        OS serviceOrder = hashTable.search(id);
        serviceOrder.setName(newServiceOrder.getName());
        serviceOrder.setDescription(newServiceOrder.getDescription());
        logger.log("Service Order ID " + id + " altered");

        cache.add(serviceOrder);
        logger.log("Service Order ID " + id + " added to cache");
    }

    private void logContent(Logger.LogOrigin origin) {
        if (origin == Logger.LogOrigin.DATABASE) {
            hashTable.logContent();
        } else if (origin == Logger.LogOrigin.CACHE) {
            cache.logContent();
        }
    }

    private String getContent(Logger.LogOrigin origin) {
        if (origin == Logger.LogOrigin.DATABASE) {
            return hashTable.toString();
        } else if (origin == Logger.LogOrigin.CACHE) {
            return cache.toString();
        }
        return null; // todo: throw exception
    }

    private boolean isInCache(OS os) {
        return cache.search(os.getId()) != null;
    }

    private boolean isInDatabase(OS os) {
        return hashTable.search(os.getId()) != null;
    }

    private String showDateAndTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

}
