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
        return id % 20; // 19 is a prime number
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
        int index = hash(os.getId());

        // linear probing until the OS is found or an empty index is found
        int iterations = 0;
        while (table[index] != null) {
            if (table[index].equals(os)) {
                return true;
            }
            index = linearProbing(index, ++iterations);
            if (iterations > kIterationLimit) {
                logger.log("Reached the iteration limit, OS not found in cache");
                return false;
            }
        }
        return false;
    }

    public OS search(int key) {
        int index = hash(key);

        // linear probing to search for the OS
        int iterations = 0;
        while (table[index] != null) {
            if (table[index].getId() == key) {
                return table[index];
            }
            index = linearProbing(key, ++iterations);
            if (iterations > kIterationLimit) {
                logger.log("Reached the iteration limit, OS not found in cache");
                return null;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                sb.append(table[i]).append("\n");
            }
        }
        return sb.toString();
    }

}