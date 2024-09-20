package services;

import entities.OS;

// - 1 because the array is 0-indexed
public class Cache {

    public final int capacity = 20;
    private int size = 0;
    private final OSK[] table;
    private final int kIterationLimit = 10;

    private class OSK {
        public final OS os;
        public int k;

        public OSK(OS os, int k) {
            this.os = os;
            this.k = k;
        }

    }

    public Cache() {
        table = new OSK[capacity];
    }

    private int hash(int id) {
        return this.hash(id, 0);
    }

    // h(x, k) = (x * 31 + k * 17) % 20
    private int hash(int id, int k) {
        return (id * 31 + k * 17) % capacity;
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


    public void add(OS os) {
        if (!isFull()) {
            for (int k = 0; k < capacity; k++) {
                int index = hash(os.getId(), k);
                if (table[index] == null) {
                    table[index] = new OSK(os, k);
                    size++;
                    return;
                }
            }
        } else { // remove the OS in the new index placement
            int index = hash(os.getId());
            if (table[index] != null) {
                table[index] = null;
                table[index] = new OSK(os, 0);
            }
        }
    }

    public void remove(int key) {
        for (int k = 0; k < capacity; k++) {
            int index = hash(key, k);
            if (table[index] != null && table[index].os.getId() == key) {
                table[index] = null;
                size--;
                return;
            }
        }
    }

    public void remove(OS os) {
        for (int k = 0; k < capacity; k++) {
            int index = hash(os.getId(), k);
            if (table[index] != null && table[index].os.equals(os)) {
                table[index] = null;
                size--;
                return;
            }
        }
    }

    public boolean isInCache(OS os) {
        for (int k = 0; k < capacity; k++) { // iterate through all possible k values
            int index = hash(os.getId(), k);
            if (table[index] != null && table[index].os.equals(os)) {
                return true;
            }
        }
        return false;
    }

    public OS search(int key) {
        for (int k = 0; k < capacity; k++) {
            int index = hash(key, k);
            if (table[index] != null && table[index].os.getId() == key) {
                return table[index].os;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (OSK osk : table) {
            if (osk != null) {
                if (osk.os != null) {
                    sb.append(osk.os.toString()).append("\n");
                }
            }
        }
        return sb.toString();
    }

}