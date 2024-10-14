package structures;

import exceptions.NonExistentEntryException;

import java.util.Iterator;

// priority heap
public class PriorityQueue<T extends PriorityQueueEntry> implements Iterable<T> {

    private Node first = null;
    private Node last = null;
    private int size = 0;

    private class Node {
        T entry;
        Node next;

        public Node(T entry) {
            this.entry = entry;
        }
    }

    public void insert(T entry) {
        Node newNode = new Node(entry);

        if (first == null) {
            first = newNode;
            last = newNode;
        } else if (first.entry.getPriority() < entry.getPriority()) {
            newNode.next = first;
            first = newNode;
        } else {
            Node current = first;
            while (current.next != null && current.next.entry.getPriority() >= entry.getPriority()) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
            if (newNode.next == null) {
                last = newNode;
            }
        }
        size++;
    }

    public T get(int index) {
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

    public void remove() {
        if (size() == 1 || first == null) {
            first = null;
            last = null;
            size = 0;
        } else {
            Node current = first;
            while (current.next != last) {
                current = current.next;
            }
            current.next = null;
            last = current;
            size--;
        }
    }

    public void remove(int id) throws NonExistentEntryException {
        if (first == null) {
            throw new NonExistentEntryException("Element not found in cache");
        }

        if (first.entry.getId() == id) {
            first = first.next;
            if (first == null) {
                last = null;
            }
            size--;
            return;
        }

        Node current = first;
        while (current.next != null && current.next.entry.getId() != id) {
            current = current.next;
        }

        if (current.next == null) {
            throw new NonExistentEntryException("Element not found in cache");
        }

        current.next = current.next.next;
        if (current.next == null) {
            last = current;
        }
        size--;
    }

    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T entry = current.entry;
                current = current.next;
                return entry;
            }
        };
    }
}
