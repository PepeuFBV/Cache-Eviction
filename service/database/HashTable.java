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
    public HashTable(Logger logger) {
        this(31, 225, logger);
        this.capacity = 7; // initial capacity of 7 indexes
        this.table = (LinkedList<OS>[]) new LinkedList[capacity];
    }

    public HashTable(int primeMultiplier, Logger logger) {
        this(primeMultiplier, 225, logger);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int primeMultiplier, int threshold, Logger logger) {
        this.capacity = 7; // initial capacity
        this.table = (LinkedList<OS>[]) new LinkedList[capacity];
        this.primeMultiplier = (primeMultiplier > 0) ? primeMultiplier : 31;
        if (threshold > 0 ) {
            this.threshold = threshold;
        }
        this.logger = logger;
    }

    public void setMayIncreaseCapacity(boolean mayIncreaseCapacity) {
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
        table = (LinkedList<OS>[]) new LinkedList[capacity];
        size = 0;
        for (LinkedList<OS> list : oldTable) { // rehashing old elements to the new table
            if (list != null) {
                for (OS os : list) {
                    add(os); // runs with the new m value
                }
            }
        }
        return table;
    }

    private void increaseCapacity() {
        LinkedList<OS>[] oldTable = table;
        capacity = new LargestPrimeNumber(capacity * 2).getLargestPrime(); // get the largest prime number smaller than double of the current capacity
        logger.log("Database is full, increasing capacity to " + capacity);
        table = rehash(oldTable);
        logger.log("Rehashed old Service Orders to the new table");
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
            table[index].add(serviceOrder);
            size++;
        }
        else { // adds to the end of the LinkedList if the index is not empty
            for (OS os : table[index]) { // checks if the OS is already in the database
                if (os.getId() == serviceOrder.getId()) { // id is unique
                    logger.log("Service Order ID " + serviceOrder.getId() + " already exists in the database");
                    return;
                }
            }
            table[index].add(serviceOrder);
            size++;
        }

    }

    public OS search(int key) {
        int index = hash(key);
        if (table[index] != null) {
            for (OS os : table[index]) {
                if (os.getId() == key) {
                    return os;
                }
            }
        }
        return null;
    }

    public void remove(OS os) throws NonExistentEntryException {
        remove(os.getId());
    }

    public void remove(int key) throws NonExistentEntryException {
        int index = hash(key);
        if (table[index] != null) {
            for (OS os : table[index]) {
                if (os.getId() == key) {
                    table[index].remove(os);
                    size--;
                    if (table[index].isEmpty()) {
                        logger.log("LinkedList at index " + index + " is empty, removing it");
                        table[index] = null;
                    }
                    return;
                }
            }
        }
        throw new NonExistentEntryException("Service Order ID " + key + " not found in database");
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
