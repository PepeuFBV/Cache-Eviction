package service.cache;

import exceptions.NonExistentEntryException;

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

    // method only for using when explicitly removing an element from the heap
    public void remove(int id) throws NonExistentEntryException {
        if (first == null) { // if there's no element
            throw new NonExistentEntryException("Element not found in cache");
        }

        if (first.entry.getId() == id) { // if the element to remove is the first one
            first = first.next;
            if (first == null) { // if it was the only element
                last = null;
            }
            size--;
            return;
        }

        Node current = first;
        while (current.next != null && current.next.entry.getId() != id) {
            current = current.next;
        }

        if (current.next == null) { // if the element was not found
            throw new NonExistentEntryException("Element not found in cache");
        }

        current.next = current.next.next;
        if (current.next == null) { // if the removed element was the last one
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