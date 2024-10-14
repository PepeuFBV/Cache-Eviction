package service.cache;

import entities.OS;
import exceptions.NonExistentEntryException;
import service.log.Logger;

// capacity: 30
public class Cache {

    private final Logger logger;
    private final PriorityQueue cache;

    public Cache() {
        this.logger = new Logger(Logger.LogOrigin.CACHE);
        this.cache = new PriorityQueue();
    }

    public int getSize() {
        return cache.size();
    }

    public int getCapacity() {
        return 30;
    }

    public void add(OS os) {
        OS found = search(os.getId());
        if (found != null) { // if the element is already in the cache, don't add it again, just update its priority
            logger.log("Element already in cache");
            increasePriority(os.getId());
        } else {
            logger.log("Adding a new element to cache with ID " + os.getId());
            int capacity = 30;
            if (cache.size() == capacity) {
                logger.log("Cache is full");
                cache.remove(); // remove the last element from cache (lowest priority)
                logger.log("Removed the last element from cache");
            }
            cache.insert(new CacheEntry(os));
            logger.log("Added the new element to cache");
        }
    }

    private void increasePriority(int id) {
        for (CacheEntry cacheEntry : cache) {
            if (cacheEntry.getOs().getId() == id) {
                cacheEntry.increasePriority();
                logger.log("Increased priority of element in cache with ID " + id);
                return;
            }
        }
    }

    public boolean isInCache(OS os) {
        logger.log("Checking if element is in cache");
        for (CacheEntry cacheEntry : cache) {
            if (cacheEntry.getOs() == os) {
                logger.log("Element found in cache");
                return true;
            }
        }
        logger.log("Element not found in cache");
        return false;
    }

    public boolean isInCache(int id) {
        logger.log("Checking if element is in cache with ID " + id);
        for (CacheEntry cacheEntry : cache) {
            if (cacheEntry.getOs().getId() == id) {
                logger.log("Element found in cache");
                return true;
            }
        }
        logger.log("Element not found in cache");
        return false;
    }

    public OS search(int id) {
        logger.log("Searching for element in cache with ID " + id);
        for (CacheEntry cacheEntry : cache) {
            if (cacheEntry.getOs().getId() == id) {
                logger.log("Found element in cache");
                return cache.get(id).getOs();
            }
        }
        logger.log("Element not found in cache");
        return null;
    }

    // method only for using when explicitly removing an element from the cache
    public void remove(int id) throws NonExistentEntryException {
        logger.log("Removing element from cache with ID " + id);
        try {
            cache.remove(id);
            logger.log("Removed element from cache");
        } catch (NonExistentEntryException e) {
            logger.log("Element not found in cache");
            throw new NonExistentEntryException(e.getMessage());
        }
    }

    public void clearCache() {
        logger.log("Clearing cache");
        cache.clear();
        logger.log("Cache cleared");
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public void logContent() throws RuntimeException {
        try {
            logger.log(toString());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void log(String message) throws RuntimeException {
        try {
            logger.log(message);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CacheEntry cacheEntry : cache) {
            sb.append(cacheEntry.getOs().getName()).append(" - ").append(cacheEntry.getOs().getDescription()).append(" - ").append(cacheEntry.getOs().getSolicitationTime()).append("\n");
        }
        return sb.toString();
    }

}