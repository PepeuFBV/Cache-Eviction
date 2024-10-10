package services.cache;

import entities.OS;
import services.Logger;
import java.util.LinkedList;

// capacity: 30
public class Cache {

    private static String getCurrentTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private final Logger logger;
    private final LinkedList<CacheEntry> cache;

    public Cache(Logger logger) {
        this.logger = logger;
        this.cache = new LinkedList<>();
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
            cache.removeLast(); // remove the last element from cache (lowest priority)
            logger.log("[" + getCurrentTime() + "] Removed the first element from cache");
        }
        cache.add(new CacheEntry(os));
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