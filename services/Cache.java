package services;

import entities.OS;
import services.database.LargestPrimeNumber;

import java.time.LocalDateTime;

// uses hash logic to store up to 20 service orders
public class Cache {

    // stores the last 20 service orders
    public final int maxSize = 20;
    private int m;
    private int primeMultiplier;
    private int capacity;
    private int size = 0;
    private OS[] table;

    public Cache() {
        capacity = 20;
        m = 20;
        primeMultiplier = new LargestPrimeNumber(20).getLargestPrime();
        table = new OS[capacity];
    }

    // hash function using division method
    private int hash(int id) {
        return (id * primeMultiplier) % m;
    }

    public boolean isFull() {
        return size == maxSize;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    // doesn't care if the cache is full, will overwrite the service order in the index
    public void add(OS os) {
        int index = hash(os.getId());
        if (table[index] != null) {
            size--;
        }
        table[index] = os;
        size++;
    }

    public void remove(int key) {
        int index = hash(key);
        if (table[index] != null) {
            table[index] = null;
            size--;
        }
    }

    public boolean isInCache(OS os) {
        int index = hash(os.getId());
        return table[index] != null && table[index].equals(os);
    }

    public OS search(int key) {
        int index = hash(key);
        return table[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (OS os : table) {
            if (os != null) {
                sb.append(os.toString()).append("\n");
            }
        }
        return sb.toString();
    }

}
