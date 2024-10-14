package structures;

public abstract class PriorityQueueEntry implements Iterable<PriorityQueueEntry> {

    public abstract int getPriority();
    public abstract void increasePriority();
    public abstract int getId();

}