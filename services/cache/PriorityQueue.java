package services.cache;

import java.util.Iterator;

// priority heap
public class PriorityQueue implements Iterable<CacheEntry> {

    private Node first = null;
    private Node last = null;
    private int size = 0;

    private static class Node {
        CacheEntry entry;
        Node next;

        public Node(CacheEntry entry) {
            this.entry = entry;
        }
    }

    public void insert(CacheEntry entry) {
        Node newNode = new Node(entry);

        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
        size++;
    }

    // will also increase the priority of the element
    public CacheEntry get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.entry.increasePriority();

        if (index != 0) {
            Node previous = first;
            for (int i = 0; i < index - 1; i++) {
                previous = previous.next;
            }
            previous.next = current.next;
            if (current == last) {
                last = previous;
            }
            current.next = first;
            first = current;
        }
        return current.entry;
    }

    // removes the last element from the heap (lowest priority)
    public void remove() {
        if (size() == 1 || first == null) { // if there's only one element or none
            first = null;
            last = null;
            size = 0;
        } else { // more than one element
            Node current = first;
            while (current.next != last) {
                current = current.next;
            }
            current.next = null;
            last = current;
            size--;
        }
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<CacheEntry> iterator() {
        return new Iterator<CacheEntry>() {
            private Node current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public CacheEntry next() {
                CacheEntry entry = current.entry;
                current = current.next;
                return entry;
            }
        };
    }

}