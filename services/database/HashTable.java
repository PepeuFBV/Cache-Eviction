package services.database;

import entities.OS;
import exceptions.NonExistentEntryException;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class HashTable {

    private int m; // number for division method
    private final int primeMultiplier; // prime number for hash function
    private int capacity; // number of elements the hash table can store
    private int size = 0; // number of elements in the hash table
    private boolean mayIncreaseCapacity = true;
    private LinkedList<OS>[] table;

    public HashTable() {
        capacity = 127; // 0 - 126
        m = 127;
        primeMultiplier = 31;
        table = new LinkedList[capacity];
    }

    public HashTable(int capacity) {
        this.capacity = new LargestPrimeNumber(capacity).getLargestPrime();
        m = capacity;
        primeMultiplier = 31;
        table = new LinkedList[capacity];
    }

    public HashTable(int capacity, int primeMultiplier) {
        this.capacity = new LargestPrimeNumber(capacity).getLargestPrime();
        m = capacity;
        if (primeMultiplier < 0) {
            primeMultiplier = 31;
        } else if (primeMultiplier > capacity) {
            primeMultiplier = new LargestPrimeNumber(capacity).getLargestPrime(); // get the largest prime number smaller than capacity
        }
        this.primeMultiplier = primeMultiplier;
        table = new LinkedList[capacity];
    }

    public void setMayIncreaseCapacity(boolean mayIncreaseCapacity) {
        this.mayIncreaseCapacity = mayIncreaseCapacity;
    }

    // hash function using division method
    private int hash(int id) {
        return (id * primeMultiplier) % m;
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

    private void increaseCapacity() {
        LinkedList<OS>[] oldTable = table;
        capacity = new LargestPrimeNumber(capacity * 2).getLargestPrime();
        this.m = capacity;
        table = new LinkedList[capacity];
        size = 0;
        for (LinkedList<OS> list : oldTable) { // rehashing old elements to the new table
            if (list != null) {
                for (OS os : list) {
                    add(os); // runs with the new m value
                }
            }
        }
    }

    public void add(OS serviceOrder) {
        if (isFull() && mayIncreaseCapacity) {
            increaseCapacity();
        }

        int index = hash(serviceOrder.getId());
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        for (OS os : table[index]) {
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
        } else {
            throw new NonExistentEntryException("Service Order ID " + key + " not found in database");
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
                        sb.append(os.toString()).append(", ");
                    }
                }
                sb.append(" ]\n");
            }

            listI++;
        }
        return sb.toString();
    }
}