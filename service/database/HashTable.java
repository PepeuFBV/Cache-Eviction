package service.database;

import entities.OS;
import exceptions.NonExistentEntryException;
import service.Logger;

import java.util.LinkedList;

public class HashTable {

    private final int primeMultiplier; // prime number for extra randomness in the hash function
    private int capacity; // number of elements the hash table can store
    private int size = 0; // number of elements currently in the hash table
    private boolean mayIncreaseCapacity = true;
    private int threshold; // percentage of the table that can be filled before increasing capacity (default is 250%)
    private LinkedList<OS>[] table;
    private final Logger logger;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this(31, 225);
        this.capacity = 7; // initial capacity of 7 indexes
        this.table = (LinkedList<OS>[]) new LinkedList[capacity];
    }

    public HashTable(int primeMultiplier) {
        this(primeMultiplier, 225);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int primeMultiplier, int threshold) {
        this.capacity = 7; // initial capacity
        this.table = (LinkedList<OS>[]) new LinkedList[capacity];
        this.primeMultiplier = (primeMultiplier > 0) ? primeMultiplier : 31;
        if (threshold > 0 ) {
            this.threshold = threshold;
        }
        this.logger = new Logger(Logger.LogOrigin.DATABASE);
    }

    public void setMayIncreaseCapacity(boolean mayIncreaseCapacity) {
        logger.log("Setting database to " + (mayIncreaseCapacity ? "increase" : "not increase") + " capacity");
        this.mayIncreaseCapacity = mayIncreaseCapacity;
    }

    // hash function using division method with prime number multiplier to reduce collisions through extra randomness
    private int hash(int id) {
        return (id * primeMultiplier) % capacity;
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

    @SuppressWarnings("unchecked")
    private LinkedList<OS>[] rehash(LinkedList<OS>[] oldTable) {
        logger.log("Rehashing Service Orders to new table");
        table = (LinkedList<OS>[]) new LinkedList[capacity];
        size = 0;
        for (LinkedList<OS> list : oldTable) { // rehashing old elements to the new table
            if (list != null) {
                for (OS os : list) {
                    add(os); // runs with the new m value
                }
            }
        }
        logger.log("Rehashing complete");
        return table;
    }

    private void increaseCapacity() {
        LinkedList<OS>[] oldTable = table;
        capacity = new LargestPrimeNumber(capacity * 2).getLargestPrime(); // get the largest prime number smaller than double of the current capacity
        logger.log("Increasing capacity to " + capacity);
        table = rehash(oldTable);
    }

    public void add(OS serviceOrder) {
        if (((float)(size) >= ((float) (capacity * threshold) / 100)) && mayIncreaseCapacity) {
            logger.log("Database is " + threshold + "% full, increasing capacity");
            increaseCapacity();
        }

        int index = hash(serviceOrder.getId());
        if (table[index] == null) { // creates a new LinkedList if the index is empty
            logger.log("Creating new LinkedList at index " + index);
            table[index] = new LinkedList<>();
        }
        else { // adds to the end of the LinkedList if the index is not empty
            for (OS os : table[index]) { // checks if the OS is already in the database
                if (os.getId() == serviceOrder.getId()) { // id is unique
                    logger.log("Service Order ID " + serviceOrder.getId() + " already exists in the database");
                    return;
                }
            }
        }
        logger.log("Adding Service Order to index " + index);
        table[index].add(serviceOrder);
        size++;

    }

    public OS search(int key) {
        logger.log("Searching for Service Order with ID " + key);
        int index = hash(key);
        if (table[index] != null) {
            for (OS os : table[index]) {
                if (os.getId() == key) {
                    logger.log("Service Order ID " + key + " found in database");
                    return os;
                }
            }
        }
        logger.log("Service Order ID " + key + " not found in database");
        return null;
    }

    public void remove(OS os) throws NonExistentEntryException {
        remove(os.getId());
    }

    public void remove(int key) throws NonExistentEntryException {
        logger.log("Removing Service Order with ID " + key);
        int index = hash(key);
        if (table[index] != null) {
            for (OS os : table[index]) {
                if (os.getId() == key) {
                    logger.log("Service Order ID " + key + " found in database, removing it");
                    table[index].remove(os);
                    size--;
                    if (table[index].isEmpty()) {
                        logger.log("LinkedList at index " + index + " is empty, removing it");
                        table[index] = null;
                    }
                    logger.log("Service Order ID " + key + " removed from database");
                    return;
                }
            }
        }
        logger.log("Service Order ID " + key + " not found in database");
        throw new NonExistentEntryException("Service Order ID " + key + " not found in database");
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
        int listI = 0;
        for (LinkedList<OS> list : table) {
            if (list != null) {
                sb.append(listI).append(": [ ");
                for (OS os : list) {
                    if (os != null) {
                        sb.append(os).append(", ");
                    }
                }
                sb.append(" ]\n");
            } else {
                sb.append(listI).append(": [ ]\n");
            }

            listI++;
        }
        return sb.toString();
    }

}
