package services.cache;

import entities.OS;
import services.Logger;

import java.security.KeyPair;
import java.util.LinkedList;

public class Cache {

    private final int capacity = 30;
    private final Logger logger;
    private final LinkedList<CacheEntry> cache;

    public Cache(Logger logger) {
        this.logger = logger;
        this.cache = new LinkedList<>();
    }

    public void add(OS os) {
        if (cache.size() == capacity) {
            cache.removeLast(); // remove the last element from cache (lowest priority)
            logger.log("[" + getCurrentTime() + "] Removed the first element from cache");
        }
        cache.add(new CacheEntry(os));
        logger.log("[" + getCurrentTime() + "] Added a new element to cache");
    }

    private String getCurrentTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

}