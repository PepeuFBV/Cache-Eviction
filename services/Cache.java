package services;

import entities.OS;
import java.util.List;
import java.util.Queue;

// uses FIFO logic when the cache is full (20 elements)
public class Cache {

    // stores the last 20 service orders
    private Queue<OS> cache;

    private void setCache(Queue<OS> cache) {
        this.cache = cache;
    }

    public Queue<OS> getCache() {
        return cache;
    }

    public int getCacheSize() {
        return cache.size();
    }

    public void add(OS os) {
        if (cache.size() == 20) {
            cache.poll(); // removes the first element
        }
        cache.add(os); // adds the new element to the end
    }

    // returns null if the service order is not in the cache/cache is empty
    public OS get(int id) {
        for (OS os : cache) {
            if (os.getId() == id) {
                return os;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Cache{" +
                "cache=" + cache +
                '}';
    }

}
