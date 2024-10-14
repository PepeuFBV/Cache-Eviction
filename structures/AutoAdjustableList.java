package structures;

import java.util.Arrays;

public class AutoAdjustableList<T> implements Iterable<T> {

    private static final int INITIAL_CAPACITY = 3;
    private Object[] elements;
    private int size = 0;

    public AutoAdjustableList() {
        elements = new Object[INITIAL_CAPACITY];
    }

    public void add(T element) {
        if (isFull()) {
            increaseCapacity();
        }
        elements[size++] = element;
    }

    public boolean remove(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                elements[i] = elements[--size];
                elements[size] = null;
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return (T) elements[index];
    }

    private void increaseCapacity() {
        int newCapacity = elements.length * 2;
        elements = Arrays.copyOf(elements, newCapacity);
    }

    public int size() {
        return size;
    }

    public boolean isFull() {
        return size == elements.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                return get(index++);
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]).append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

}
