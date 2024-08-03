package services;

import entities.OS;
import java.util.List;
import java.util.Queue;

// uses FIFO logic when the cache is full (20 elements)
public class Cache {

    // stores the last 20 service orders
    private Queue<OS> cache = new java.util.LinkedList<>();
    final int maxSize = 20;

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
        if (cache.isEmpty()) {
            return null;
        }
        for (OS os : cache) {
            if (os.getId() == id) {
                return os;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public boolean hasSpace() {
        return getSize() < maxSize;
    }

    public void remove(int id) {
        if (cache.isEmpty()) {
            return;
        }
        for (OS os : cache) {
            if (os.getId() == id) {
                cache.remove(os);
                return;
            }
        }
    }

    public int getSize() {
        return cache.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (OS os : cache) {
            sb.append(os.toString()).append("\n");
        }
        return sb.toString();
    }

}
