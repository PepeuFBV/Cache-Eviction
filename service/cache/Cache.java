package service.cache;

import entities.OS;
import exceptions.NonExistentEntryException;
import service.Logger;

// capacity: 30
public class Cache {

    private static String getCurrentTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private final Logger logger;
    private final PriorityQueue cache;

    public Cache(Logger logger) {
        this.logger = logger;
        this.cache = new PriorityQueue();
    }

    public int getSize() {
        return cache.size();
    }

    public int getCapacity() {
        return 30;
    }

    public void add(OS os) {
        int capacity = 30;
        if (cache.size() == capacity) {
            cache.remove(); // remove the last element from cache (lowest priority)
            logger.log("[" + getCurrentTime() + "] Removed the first element from cache");
        }
        cache.insert(new CacheEntry(os));
        logger.log("[" + getCurrentTime() + "] Added a new element to cache");
    }

    public boolean isInCache(OS os) {
        for (CacheEntry cacheEntry : cache) {
            if (cacheEntry.getOs() == os) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCache(int id) {
        for (CacheEntry cacheEntry : cache) {
            if (cacheEntry.getOs().getId() == id) {
                return true;
            }
        }
        return false;
    }

    public OS search(int id) {
        for (CacheEntry cacheEntry : cache) {
            if (cacheEntry.getOs().getId() == id) {
                logger.log("[" + getCurrentTime() + "] Searched for element in cache");
                return cache.get(id).getOs();
            }
        }
        logger.log("[" + getCurrentTime() + "] Element not found in cache");
        return null;
    }

    // method only for using when explicitly removing an element from the cache
    public void remove(int id) throws NonExistentEntryException {
        try {
            cache.remove(id);
            logger.log("[" + getCurrentTime() + "] Removed element from cache");
        } catch (NonExistentEntryException e) {
            logger.log("[" + getCurrentTime() + "] " + e.getMessage());
            throw new NonExistentEntryException(e.getMessage());
        }
    }

    public void clearCache() {
        cache.clear();
        logger.log("[" + getCurrentTime() + "] Cache cleared");
    }

    public boolean isEmpty() {
        return cache.isEmpty();
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