package service.cache;

import entities.OS;
import structures.PriorityQueueEntry;

import java.util.Iterator;

public class CacheEntry extends PriorityQueueEntry implements Comparable<CacheEntry> {

    private final OS os;
    private int priority = 1;

    public CacheEntry(OS os) {
        this.os = os;
    }

    public OS getOs() {
        return os;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void increasePriority() {
        priority++;
    }

    public void resetPriority() {
        priority = 1;
    }

    public void decreasePriority() {
        priority--;
    }

    @Override
    public int getId() {
        return os.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CacheEntry other) {
            return os.equals(other.os);
        }
        return false;
    }

    public boolean equals(OS os) {
        return this.os.equals(os);
    }

    @Override
    public Iterator<PriorityQueueEntry> iterator() {
        return new Iterator<PriorityQueueEntry>() {
            private CacheEntry current = CacheEntry.this;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public PriorityQueueEntry next() {
                CacheEntry entry = current;
                current = null;
                return entry;
            }
        };
    }

    @Override
    public int compareTo(CacheEntry other) {
        return Integer.compare(priority, other.priority);
    }

    @Override
    public String toString() {
        return os.toString();
    }

}
