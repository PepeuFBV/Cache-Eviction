package services.cache;

import entities.OS;

public class CacheEntry {
    private OS os;
    private int priority = 1;

    public CacheEntry(OS os) {
        this.os = os;
    }

    public OS getOs() {
        return os;
    }

    public int getPriority() {
        return priority;
    }

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
    public boolean equals(Object obj) {
        if (obj instanceof CacheEntry) {
            CacheEntry other = (CacheEntry) obj;
            return os.equals(other.os);
        }
        return false;
    }

    @Override
    public String toString() {
        return os.toString();
    }

}
