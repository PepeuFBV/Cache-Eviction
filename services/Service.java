package services;

import entities.OS;
import exceptions.treeExceptions.DuplicateEntryException;
import exceptions.treeExceptions.NonExistentEntryException;
import services.database.AVLTree;

import java.io.IOException;

public class Service {

    private final Cache cache;
    private final Logger logger;
    private final AVLTree avlTree;

    public Service() {
        this.logger = Logger.getInstance();
        logger.log("\n\nStarting services at " + showDateAndTime());
        this.cache = new Cache();
        logger.log("[" + getCurrentTime() + "] Cache created");
        this.avlTree = new AVLTree();
        logger.log("[" + getCurrentTime() + "] AVL Tree created");

        // setting id for the next service order
        avlTree.getHighestId();
        OS.nextId = avlTree.getHighestId() + 1;
    }

    public void stopServices() {
        logger.log("Stopping services at " + showDateAndTime());
    }

    public void addNewServiceOrder(OS os) throws DuplicateEntryException {
        if (isInCache(os)) {
            logger.log("[" + getCurrentTime() + "] Service Order already exists");
            throw new DuplicateEntryException("Service Order already exists");
        } else {
            if (isInTree(os)) {
                logger.log("[" + getCurrentTime() + "] Service Order already exists");
                throw new DuplicateEntryException("Service Order already exists");
            } else {
                logger.log("[" + getCurrentTime() + "] Creating new Service Order");
                if (cache.hasSpace()) { // adds to cache if there is extra space
                    cache.add(os);
                    logger.log("[" + getCurrentTime() + "] Service Order added to cache");
                }
                avlTree.insert(os);
                logger.log("[" + getCurrentTime() + "] Service Order added to tree");
            }
        }
        logData(true);
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
        if (avlTree.isEmpty()) {
            return "Database is empty\n";
        }
        logData(true);
        return avlTree + "There are a total of " + avlTree.getSize() + " Service Orders in the database\n";
    }

    public String seeCache() {
        logger.log("[" + getCurrentTime() + "] Listing all Service Orders in the cache");
        if (cache.isEmpty()) {
            return "Cache is empty\n";
        }
        logData(true);
        return cache.toString();
    }

    public OS searchServiceOrder(int id) {
        logger.log("[" + getCurrentTime() + "] Searching for Service Order with ID " + id);
        if (avlTree.isEmpty()) {
            logger.log("[" + getCurrentTime() + "] Database is empty");
            return null;
        }

        if (cache.get(id) != null) { // OS exists in the cache
            logger.log("[" + getCurrentTime() + "] Service Order found in cache");
            logData(true);
            return cache.get(id); // returns the OS
        }

        if (avlTree.search(id)) { // OS exists in the tree
            logger.log("[" + getCurrentTime() + "] Service Order found in tree");
            cache.add(avlTree.searchOS(id)); // adds to cache
            logData(true);
            return avlTree.searchOS(id); // returns the OS
        }
        logger.log("[" + getCurrentTime() + "] Service Order not found in tree");
        return null;
    }

    public void removeServiceOrder(int id) throws NonExistentEntryException {
        if (avlTree.isEmpty()) {
            logger.log("[" + getCurrentTime() + "] Database is empty, can't remove Service Order");
            throw new NonExistentEntryException("Database is empty, can't remove Service Order");
        } else {
            logger.log("[" + getCurrentTime() + "] Removing Service Order with ID " + id);

            if (cache.get(id) != null) { // OS exists in the cache
                logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " found in cache");
                OS os = cache.get(id);
                cache.remove(id);
                logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " removed from cache");
                try {
                    avlTree.remove(id);
                    logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " removed from database");
                } catch (NonExistentEntryException e) {
                    logger.log("[" + getCurrentTime() + "] " + e.getMessage());
                    throw new NonExistentEntryException(e.getMessage());
                }
                logData(true);
                return;
            } else { // OS is not in the cache
                if (avlTree.search(id)) { // OS exists in the tree
                    logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " found in database");
                    OS os = avlTree.searchOS(id);
                    cache.remove(id); // removes the OS from the cache, due to the search method's default behavior
                    logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " removed from cache");
                    try {
                        avlTree.remove(id);
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

    public void alterServiceOrder(int id, OS newServiceOrderData) { // assumes the tree has the Service Order
        logger.log("[" + getCurrentTime() + "] Altering Service Order with ID " + id);

        if (cache.get(id) != null) { // OS exists in the cache
            logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " found in cache");
            cache.remove(id);
            logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " removed from cache");
        }

        OS serviceOrder = avlTree.searchOS(id);
        serviceOrder.setName(newServiceOrderData.getName());
        serviceOrder.setDescription(newServiceOrderData.getDescription());
        logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " altered");

        cache.add(avlTree.searchOS(id));
        logger.log("[" + getCurrentTime() + "] Service Order ID " + id + " added to cache");

        logData(true);
    }

    private void logData(boolean logCache) {
        logger.log("[" + getCurrentTime() + "] - T - Tree height: " + avlTree.getHeight());
        if (logCache) {
            logger.log("[" + getCurrentTime() + "] - C - Cache content: " + cache.toString());
        }
    }

    private boolean isInCache(OS os) {
        return cache.get(os.getId()) != null;
    }

    private boolean isInTree(OS os) {
        return avlTree.search(os.getId());
    }

    private String showDateAndTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

}
