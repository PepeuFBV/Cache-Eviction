package services.database;

import entities.OS;
import exceptions.NonExistentEntryException;
import java.util.LinkedList;

public class HashTable {

    private final int primeMultiplier; // prime number for extra randomness in the hash function
    private int capacity; // number of elements the hash table can store
    private int size = 0; // number of elements currently in the hash table
    private boolean mayIncreaseCapacity = true;
    private int threshold = 250; // percentage of the table that can be filled before increasing capacity (default is 250%)
    private LinkedList<OS>[] table;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this(31, 250);
        this.capacity = 7; // initial capacity of 7 indexes
        this.table = (LinkedList<OS>[]) new LinkedList[capacity];
    }

    public HashTable(int primeMultiplier) {
        this(primeMultiplier, 250);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int primeMultiplier, int threshold) {
        this.capacity = 7; // initial capacity
        this.table = (LinkedList<OS>[]) new LinkedList[capacity];
        this.primeMultiplier = (primeMultiplier > 0) ? primeMultiplier : 31;
        if (threshold > 0 ) {
            this.threshold = threshold;
        }
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
        table = rehash(oldTable);
    }

    public void add(OS serviceOrder) {
        if (((float)(size) >= ((float) (capacity * threshold) / 100)) && mayIncreaseCapacity) {
            System.out.println("Size: " + size + " Capacity: " + capacity + " Threshold: " + threshold);
            increaseCapacity();
        }

        int index = hash(serviceOrder.getId());
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        for (OS os : table[index]) { // check if service order already exists index List
            if (os.getId() == serviceOrder.getId()) {
                return;
            }
        }

        table[index].add(serviceOrder);
        size++;
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
