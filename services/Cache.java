package services;

import entities.OS;

// - 1 because the array is 0-indexed
public class Cache {

    public final int capacity = 20;
    private int size = 0;
    private final OS[] table;
    private final int kIterationLimit = 100;
    private final Logger logger;

    public Cache(Logger logger) {
        table = new OS[capacity];
        this.logger = logger;
    }

    // uses linear probing to find the next available index
    private int hash(int id) {
        return id % 20;
    }

    private int linearProbing(int id, int k) {
        return (hash(id) + k) % 20;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public void add(OS os) {
        if (isInCache(os)) {
            logger.log("Service Order already exists in cache");
        } else {
            if (isFull()) {
                logger.log("Cache is full, substituting the index with the corresponding Service Order");
                int index = hash(os.getId());
                table[index] = os; // substitutes the index with the new OS
            } else { // needs to find an empty index for the new OS
                int index = hash(os.getId());

                // linear probing until an empty index is found
                int iterations = 0;
                while (table[index] != null) {
                    index = linearProbing(os.getId(), ++iterations);
                    if (iterations > kIterationLimit) {
                        logger.log("Reached the iteration limit, can't add the Service Order to the cache");
                        return;
                    }
                }
                table[index] = os;
                size++;
            }
        }
    }

    public void remove(int key) {
        for (int k = 0; k < capacity; k++) {
            int index = linearProbing(key, k);
            if (table[index] != null && table[index].getId() == key) {
                table[index] = null;
                size--;
                return;
            } else {
                logger.log("Service Order ID " + key + " not found in cache (k = " + k + "), increasing k value");
            }
        }
    }

    public void remove(OS os) {
        remove(os.getId());
    }

    public boolean isInCache(OS os) {
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].equals(os)) {
                return true;
            }
        }
        logger.log("OS not found in cache");
        return false;
    }

    public OS search(int key) {
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].getId() == key) {
                return table[i];
            }
        }
        logger.log("Service Order ID " + key + " not found in cache");
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        boolean first = true;
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(table[i].getId());
                first = false;
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

}