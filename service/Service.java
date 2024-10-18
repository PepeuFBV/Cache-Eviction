package service;

import compression.Compressor;
import entities.OS;
import exceptions.DuplicateEntryException;
import exceptions.NonExistentEntryException;
import service.cache.Cache;
import service.database.HashTable;
import service.log.Logger;
import java.io.IOException;
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

    public void receiveMessage(String compressedMessage) {
        String decompressedMessage = compressor.decompress(compressedMessage);
        // todo: switch case for method calling
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

    private void clearLog() throws RuntimeException {
        try {
            logger.clearLog();
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear log");
        }
    }

    private boolean seeAllServiceOrders() throws RuntimeException {
        logger.log("Listing all Service Orders");
        if (cache.isEmpty() || hashTable.isEmpty()) { // checks for faster answer if cache is empty
            if (hashTable.isEmpty()) {
                hashTable.log("Database is empty");
            }
            return false;
        }
        logContent(Logger.LogOrigin.DATABASE);
        return true;
    }

    private boolean seeCache() {
        logger.log("Listing all Service Orders in the cache");
        if (cache.isEmpty()) {
            cache.log("Cache is empty");
            return false;
        }
        logContent(Logger.LogOrigin.CACHE);
        return true;
    }

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

    private void removeServiceOrder(int id) throws NonExistentEntryException {
        if (hashTable.isEmpty()) {
            logger.log("Database is empty, can't remove Service Order");
            throw new NonExistentEntryException("Database is empty, can't remove Service Order");
        } else {
            logger.log("Removing Service Order with ID " + id);
            try {
                cache.remove(id);
                hashTable.remove(id);
                logger.log("Service Order ID " + id + " removed from cache and database");
                return; // success on removal
            } catch (NonExistentEntryException e) {
                cache.log("Service Order ID " + id + " not found in cache");
                try {
                    hashTable.remove(id);
                    logger.log("Service Order ID " + id + " removed from database");
                    return; // success on removal
                } catch (NonExistentEntryException e2) {
                    hashTable.log("Service Order ID " + id + " not found in database");
                }
            }
        }
        throw new NonExistentEntryException("Service Order ID " + id + " not found in database");
    }

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
