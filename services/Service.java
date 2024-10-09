package services;

import entities.OS;
import exceptions.DuplicateEntryException;
import exceptions.NonExistentEntryException;
import services.cache.Cache;
import services.database.HashTable;
import java.io.IOException;

public class Service {

    private final Cache cache;
    private final Logger logger;
    private final HashTable hashTable;

    public Service() {
        this.logger = Logger.getInstance();
        logger.log("\n\nStarting services at " + showDateAndTime());
        this.cache = new Cache(logger);
        logger.log("[" + getCurrentTime() + "] Cache created");
        this.hashTable = new HashTable(logger);
        logger.log("[" + getCurrentTime() + "] HashTable created");
    }

    public void turnOffIncreaseCapacity() {
        hashTable.setMayIncreaseCapacity(false);
    }

    public void turnOnIncreaseCapacity() {
        hashTable.setMayIncreaseCapacity(true);
    }

    public void stopServices() {
        logger.log("Stopping services at " + showDateAndTime());
    }

    public void addNewServiceOrder(OS os) throws DuplicateEntryException {
        logger.log("[" + getCurrentTime() + "] Creating new Service Order");
        if (isInCache(os)) { // check if service order already exists in cache
            logger.log("[" + getCurrentTime() + "] Service Order already exists in cache");
            throw new DuplicateEntryException("Service Order already exists in cache");
        } else if (isInDatabase(os)) { // check if service order already exists in database
            logger.log("[" + getCurrentTime() + "] Service Order already exists in database");
            throw new DuplicateEntryException("Service Order already exists in database");
        } else { // service order doesn't exist in cache or database
            hashTable.add(os);
            logger.log("[" + getCurrentTime() + "] Service Order added to database");
            cache.add(os);
            logger.log("[" + getCurrentTime() + "] Service Order added to cache");
            logData(true);
        }
    }

    private String getCurrentTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String clearLog() throws IOException {
        try {
            return logger.clearLog();
        } catch (IOException e) {
            throw new IOException("Failed to clear log");
        }
    }

    public String seeAllServiceOrders() {
        logger.log("[" + getCurrentTime() + "] Listing all Service Orders");
        if (cache.isEmpty()) { // checks for faster answer if cache is empty
            return "Database is empty\n";
        }
        else if (hashTable.isEmpty()) {
            return "Database is empty\n";
        } else {
            logData(true);
            return hashTable + "The database is " + String.format("%.2f", ((float) hashTable.getSize() / hashTable.getCapacity() * 100)) + "% full\n" + "There are a total of " + hashTable.getSize() + " Service Orders in the database, with a total size of " + hashTable.getCapacity() + " indexes\n";
        }
    }

    public String seeCache() {
        logger.log("[" + getCurrentTime() + "] Listing all Service Orders in the cache");
        if (cache.isEmpty()) {
            return "Cache is empty\n";
        }
        logData(true);
        return cache + "\nThere are a total of " + cache.getSize() + " Service Orders in the cache, with a total size of " + cache.getCapacity() + "\n";
    }

    public OS searchServiceOrder(int id) {
        logger.log("[" + getCurrentTime() + "] Searching for Service Order with ID " + id);
        if (hashTable.isEmpty()) {
            logger.log("[" + getCurrentTime() + "] Database is empty");
        } else {

            if (cache.search(id) != null) { // OS exists in the cache
                logger.log("[" + getCurrentTime() + "] Service Order found in cache");
                cache.add(hashTable.search(id)); // adds to the end of the cache
                logData(true);
                return cache.search(id); // returns the OS
            } else if (hashTable.search(id) != null) { // OS exists in the tree
                logger.log("[" + getCurrentTime() + "] Service Order found in database");
                cache.add(hashTable.search(id)); // adds to cache
                logData(true);
                return hashTable.search(id); // returns the OS
            }
            logger.log("[" + getCurrentTime() + "] Service Order not found in database");
        }
        return null;
    }

    public void removeServiceOrder(int id) throws NonExistentEntryException {
        if (hashTable.isEmpty()) {
            logger.log("[" + getCurrentTime() + "] Database is empty, can't remove Service Order");
            throw new NonExistentEntryException("Database is empty, can't remove Service Order");
        } else {
            logger.log("[" + getCurrentTime() + "] Removing Service Order with ID " + id);

            if (cache.search(id) != null) { // OS exists in the cache
                logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " found in cache");
                OS os = cache.search(id);
                cache.remove(id);
                logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " removed from cache");
                try {
                    hashTable.remove(id);
                    logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " removed from database");
                } catch (NonExistentEntryException e) {
                    logger.log("[" + getCurrentTime() + "] " + e.getMessage());
                    throw new NonExistentEntryException(e.getMessage());
                }
                logData(true);
                return;
            } else { // OS is not in the cache
                if (hashTable.search(id) != null) { // OS exists in the tree
                    logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " found in database");
                    OS os = hashTable.search(id);
                    cache.remove(id); // removes the OS from the cache, due to the search method's default behavior
                    logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " removed from cache");
                    try {
                        hashTable.remove(id);
                        logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " removed from database");
                    } catch (NonExistentEntryException e) {
                        logger.log("[" + getCurrentTime() + "] " + e.getMessage());
                        throw new NonExistentEntryException(e.getMessage());
                    }
                    logData(true);
                    return;
                }
            }
        }
        logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " not found in database");
        throw new NonExistentEntryException("Service Order ID " + id + " not found in database");
    }

    public void alterServiceOrder(int id, OS newServiceOrder) {
        logger.log("[" + getCurrentTime() + "] Altering Service Order with ID " + id);

        if (cache.search(id) != null) { // OS exists in the cache
            logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " found in cache");
            cache.remove(id);
            logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " removed from cache");
        }

        OS serviceOrder = hashTable.search(id);
        serviceOrder.setName(newServiceOrder.getName());
        serviceOrder.setDescription(newServiceOrder.getDescription());
        logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " altered");

        cache.add(hashTable.search(id));
        logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " added to cache");


        logData(true);
    }

    private void logData(boolean logCache) {
        if (logCache) {
            logger.log("[" + getCurrentTime() + "] - C - Cache content: " + cache.toString());
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
