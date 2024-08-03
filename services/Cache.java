package services;

import entities.OS;
import java.util.List;

// uses FIFO logic when the cache is full (20 elements)
public class Cache {

    // stores the last 20 service orders
    private List<OS> cache;

    public Cache(List<OS> cache) {
        this.setCache(cache);
    }

    private void setCache(List<OS> cache) {
        this.cache = cache;
    }

    public List<OS> getCache() {
        return cache;
    }

    public int getCacheSize() {
        return cache.size();
    }

    public void add(OS os) {
        if (cache.size() == 20) {
            cache.remove(0);
        }
        cache.add(os);
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
